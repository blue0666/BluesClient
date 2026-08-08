package com.blue.bluesclient.event.forge;

import com.blue.bluesclient.config.BCConfig;
import ichttt.mods.firstaid.api.CapabilityExtendedHealthSystem;
import ichttt.mods.firstaid.api.damagesystem.AbstractDamageablePart;
import ichttt.mods.firstaid.api.damagesystem.AbstractPlayerDamageModel;
import ichttt.mods.firstaid.api.enums.EnumPlayerPart;
import ichttt.mods.firstaid.api.event.FirstAidLivingDamageEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

//本段内容由于过于过长，经过AI辅助修订，可能存在bug
public class DamageDisplayHandler {

    private static boolean shownActualThisHit = false;
    private static float lastRawAmount = 0.0F;
    private static float lastRemainingHp = 0.0F; // 受伤前大约还能扣多少
    private static String lastType = "unknown";
    private static String lastFrom = "-";

    private static String redTag(String tag) {
        return TextFormatting.RED + "[" + tag + "]" + TextFormatting.RESET;
    }

    private static void send(EntityPlayer player, String msg) {
        player.sendMessage(new TextComponentString(msg));
    }

    /** 受伤前各部位血量合计；失败则退回 getHealth() */
    private static float sumPartHealth(EntityPlayer player) {
        try {
            AbstractPlayerDamageModel model =
                    player.getCapability(CapabilityExtendedHealthSystem.INSTANCE, null);
            if (model == null) return player.getHealth();
            float sum = 0.0F;
            for (EnumPlayerPart part : EnumPlayerPart.VALUES) {
                AbstractDamageablePart p = model.getFromEnum(part);
                if (p != null) sum += p.currentHealth;
            }
            return sum;
        } catch (Throwable t) {
            return player.getHealth();
        }
    }

    private static boolean isFaInstantKillAmount(float amount) {
        return amount == Float.MAX_VALUE
                || Float.isNaN(amount)
                || amount == Float.POSITIVE_INFINITY;
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onLivingHurt(LivingHurtEvent event) {
        if (!BCConfig.DamageDisplay.getBooleanValue()) return;
        if (!(event.getEntityLiving() instanceof EntityPlayer)) return;
        EntityPlayer player = (EntityPlayer) event.getEntityLiving();

        // 服务器中不生效，因为伤害在服务端计算
        if (player.world.isRemote) return;
        if (!isLocalPlayer(player)) return;

        float amount = event.getAmount();
        if (amount <= 0.0F) return;

        DamageSource source = event.getSource();
        String type = source != null ? source.getDamageType() : "unknown";
        String from = describeSource(source);

        shownActualThisHit = false;
        lastRawAmount = amount;
        lastRemainingHp = sumPartHealth(player);
        lastType = type;
        lastFrom = from;

        send(player, redTag("原始伤害") + String.format(
                " 数值:%.2f | 类型:%s | 来源:%s",
                amount, type, from));

        // FirstAid 秒杀分支不会发 FirstAidLivingDamageEvent
        if (isFaInstantKillAmount(amount)) {
            float actual = lastRemainingHp; // 部位被清空，实际≈受伤前全部可扣
            send(player, redTag("实际伤害") + String.format(
                    " 数值:%.2f | 类型:%s | 未分摊:- | 各部位:ALL | 来源:%s",
                    actual, type, from));
            shownActualThisHit = true;
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onFirstAidLivingDamage(FirstAidLivingDamageEvent event) {
        if (!BCConfig.DamageDisplay.getBooleanValue()) return;

        EntityPlayer player = event.getEntityPlayer();
        if (player.world.isRemote) return;
        if (!isLocalPlayer(player)) return;

        AbstractPlayerDamageModel before = event.getBeforeDamage();
        AbstractPlayerDamageModel after = event.getAfterDamage();

        float totalTaken = 0.0F;
        StringBuilder parts = new StringBuilder();
        for (EnumPlayerPart part : EnumPlayerPart.VALUES) {
            AbstractDamageablePart b = before.getFromEnum(part);
            AbstractDamageablePart a = after.getFromEnum(part);
            if (b == null || a == null) continue;
            float delta = b.currentHealth - a.currentHealth;
            if (delta > 0.001F) {
                totalTaken += delta;
                if (parts.length() > 0) parts.append(", ");
                parts.append(part.name()).append(':').append(String.format("%.1f", delta));
            }
        }

        float left = event.getUndistributedDamage();
        DamageSource source = event.getSource();
        String type = source != null ? source.getDamageType() : "unknown";

        send(player, redTag("实际伤害") + String.format(
                " 数值:%.2f | 类型:%s | 未分摊:%.2f | 各部位:%s",
                totalTaken,
                type,
                left,
                parts.length() == 0 ? "-" : parts.toString()));
        shownActualThisHit = true;
    }

    /** 合并：真死了但本次没打出「实际」时再补一句（带估算数值） */
    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (!BCConfig.DamageDisplay.getBooleanValue()) return;
        if (!(event.getEntityLiving() instanceof EntityPlayer)) return;
        EntityPlayer player = (EntityPlayer) event.getEntityLiving();
        if (player.world.isRemote) return;
        if (!isLocalPlayer(player)) return;
        if (shownActualThisHit) return; // FA 已打过，不重复

        // 实际 ≈ min(原始, 受伤前剩余)；溢出部分扣不进去
        float actual = Math.min(lastRawAmount, lastRemainingHp);
        if (actual < 0.0F) actual = 0.0F;

        DamageSource source = event.getSource();
        String type = source != null ? source.getDamageType() : lastType;
        String from = describeSource(source);
        if ("-".equals(from) || from == null) from = lastFrom;

        send(player, redTag("实际伤害") + String.format(
                " 数值:%.2f | 类型:%s | 未分摊:- | 各部位:-(致死补全) | 来源:%s",
                actual, type, from));
        shownActualThisHit = true;
    }

    @SideOnly(Side.CLIENT)
    public static boolean isLocalPlayer(EntityPlayer player) {
        Minecraft mc = Minecraft.getMinecraft();
        return mc.player != null && mc.player.getUniqueID().equals(player.getUniqueID());
    }

    public static String describeSource(DamageSource source) {
        if (source == null) return "-";
        Entity trueSrc = source.getTrueSource();
        if (trueSrc != null) {
            return trueSrc.getName();
        }
        Entity immediate = source.getImmediateSource();
        if (immediate != null) {
            return immediate.getName();
        }
        return source.getDamageType();
    }
}