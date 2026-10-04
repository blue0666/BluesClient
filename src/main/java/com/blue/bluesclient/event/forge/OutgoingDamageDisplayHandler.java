package com.blue.bluesclient.event.forge;

import com.blue.bluesclient.config.BCConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

//原始伤害：ForgeHooks.onLivingHurt 进入总线之前（Mixin HEAD）
//实际伤害：ForgeHooks.onLivingDamage 整次总线之后（Mixin RETURN）
public abstract class OutgoingDamageDisplayHandler {

    private static int lastTargetId = -1;
    private static long tick0WorldTime = -1L;
    private static int lastTickLabel = 0;

    private static String greenTag(String tag) {
        return TextFormatting.GREEN + "[" + tag + "]" + TextFormatting.RESET;
    }

    private static void resetCombo() {
        lastTargetId = -1;
        tick0WorldTime = -1L;
        lastTickLabel = 0;
    }

    private static boolean ensureDisplayEnabled() {
        if (!BCConfig.DamageDisplayAttack.getBooleanValue()) {
            resetCombo();
            return false;
        }
        return true;
    }

    @SideOnly(Side.CLIENT)
    private static boolean isLocalPlayer(EntityPlayer player) {
        Minecraft mc = Minecraft.getMinecraft();
        return mc.player != null && mc.player.getUniqueID().equals(player.getUniqueID());
    }

    private static boolean isLocalAttacker(DamageSource source) {
        if (source == null) return false;
        Entity trueSrc = source.getTrueSource();
        if (!(trueSrc instanceof EntityPlayer)) return false;
        return isLocalPlayer((EntityPlayer) trueSrc);
    }

    private static EntityPlayer localAttacker(DamageSource source) {
        return (EntityPlayer) source.getTrueSource();
    }

    private static int worldTickLabel(EntityLivingBase target) {
        int id = target.getEntityId();
        long now = target.world.getTotalWorldTime();

        if (id != lastTargetId || tick0WorldTime < 0L) {
            lastTargetId = id;
            tick0WorldTime = now;
        }
        return (int) (now - tick0WorldTime);
    }

    public static void onRawOutgoing(EntityLivingBase target, DamageSource source, float amount) {
        if (!ensureDisplayEnabled()) return;
        if (target == null || source == null) return;
        if (target instanceof EntityPlayer) return;
        if (target.world == null || target.world.isRemote) return;
        if (!isLocalAttacker(source)) return;
        if (amount <= 0.0F) return;

        EntityPlayer attacker = localAttacker(source);
        if (attacker == null) return;

        lastTickLabel = worldTickLabel(target);

        String type = source.getDamageType();
        String msg = greenTag("原始输出") + String.format(
                " tick%d | 数值:%.2f | 类型:%s | 目标:%s",
                lastTickLabel, amount, type, target.getName());
        attacker.sendMessage(new TextComponentString(msg));
    }

    public static void onFinalOutgoing(EntityLivingBase target, DamageSource source, float finalAmount) {
        if (!ensureDisplayEnabled()) return;
        if (target == null || source == null) return;
        if (target instanceof EntityPlayer) return;
        if (target.world == null || target.world.isRemote) return;
        if (!isLocalAttacker(source)) return;
        if (target.getEntityId() != lastTargetId || tick0WorldTime < 0L) return;

        EntityPlayer attacker = localAttacker(source);
        if (attacker == null) return;

        int tickN = lastTickLabel;
        String type = source.getDamageType();
        String msg = greenTag("实际输出") + String.format(
                " tick%d | 数值:%.2f | 类型:%s | 目标:%s",
                tickN, finalAmount, type, target.getName());
        attacker.sendMessage(new TextComponentString(msg));
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        EntityLivingBase e = event.getEntityLiving();
        if (e.getEntityId() == lastTargetId) {
            resetCombo();
            return;
        }
        if (e instanceof EntityPlayer && isLocalPlayer((EntityPlayer) e)) {
            resetCombo();
        }
    }

    @SubscribeEvent
    public static void onChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (isLocalPlayer(event.player)) {
            resetCombo();
        }
    }

    @SubscribeEvent
    public static void onLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (isLocalPlayer(event.player)) {
            resetCombo();
        }
    }

}