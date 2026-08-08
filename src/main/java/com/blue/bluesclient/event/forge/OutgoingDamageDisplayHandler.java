package com.blue.bluesclient.event.forge;

import com.blue.bluesclient.config.BCConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * 玩家打怪伤害显示。
 * tickN = (当前世界时间 - 本连段首次命中时的 tick0) + 1
 * 同一游戏刻多次输出共用同一 tickN；中间空过的游戏刻不发消息，下次成功会跳号（如 tick1 → tick5）。
 */
public abstract class OutgoingDamageDisplayHandler {

    private static int lastTargetId = -1;
    /** 本连段第一次成功输出时的世界时间（相对原点） */
    private static long tick0WorldTime = -1L;

    private static String greenTag(String tag) {
        return TextFormatting.GREEN + "[" + tag + "]" + TextFormatting.RESET;
    }

    private static void resetCombo() {
        lastTargetId = -1;
        tick0WorldTime = -1L;
    }

    private static boolean ensureDisplayEnabled() {
        if (!BCConfig.DamageDisplay.getBooleanValue()) {
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
        return (int) (now - tick0WorldTime) ;
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onLivingHurt(LivingHurtEvent event) {
        if (!ensureDisplayEnabled()) return;

        EntityLivingBase target = event.getEntityLiving();
        if (target instanceof EntityPlayer) return;
        if (target.world.isRemote) return;
        if (!isLocalAttacker(event.getSource())) return;

        float amount = event.getAmount();
        if (amount <= 0.0F) return;

        int tickN = worldTickLabel(target);
        DamageSource source = event.getSource();
        String type = source != null ? source.getDamageType() : "unknown";
        EntityPlayer attacker = localAttacker(source);
        if (attacker == null) return;

        String msg = greenTag("原始输出") + String.format(
                " tick%d | 数值:%.2f | 类型:%s | 目标:%s",
                tickN, amount, type, target.getName());
        attacker.sendMessage(new TextComponentString(msg));
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onLivingDamage(LivingDamageEvent event) {
        if (!ensureDisplayEnabled()) return;

        EntityLivingBase target = event.getEntityLiving();
        if (target instanceof EntityPlayer) return;
        if (target.world.isRemote) return;
        if (!isLocalAttacker(event.getSource())) return;
        if (target.getEntityId() != lastTargetId || tick0WorldTime < 0L) return;

        float amount = event.getAmount();
        int tickN = worldTickLabel(target);
        DamageSource source = event.getSource();
        String type = source != null ? source.getDamageType() : "unknown";
        EntityPlayer attacker = localAttacker(source);
        if (attacker == null) return;

        String msg = greenTag("实际输出") + String.format(
                " tick%d | 数值:%.2f | 类型:%s | 目标:%s",
                tickN, amount, type, target.getName());
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

    /** 目标卸载 / 已死时清连段 */
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (!ensureDisplayEnabled()) return;
        if (lastTargetId < 0) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.world == null || mc.player == null) {
            resetCombo();
            return;
        }
        Entity e = mc.world.getEntityByID(lastTargetId);
        if (e == null || !e.isEntityAlive()) {
            resetCombo();
        }
    }
}