package com.blue.bluesclient.event.forge.legacy;

import com.blue.bluesclient.config.BCConfig;
import com.blue.bluesclient.feat.everythingnunchaku.NunchakuConfigProvider;
import com.mujmajnkraft.bettersurvival.capabilities.nunchakucombo.INunchakuCombo;
import com.mujmajnkraft.bettersurvival.capabilities.nunchakucombo.NunchakuComboProvider;
import com.mujmajnkraft.bettersurvival.packet.BetterSurvivalPacketHandler;
import com.mujmajnkraft.bettersurvival.packet.MessageNunchakuSpinClient;
import meldexun.reachfix.hook.client.EntityRendererHook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class RLCombatOffhandLegacy {
    private static final Field ENABLE_OFFHAND;
    private static final Method IS_ITEM_USABLE;
    private static final Field TUTO_CAP;
    private static final Method GET_OH_CD;
    private static final Method CLEAR_MOD;
    private static final Method ADD_MOD;
    private static final Method ON_RIGHT_CLICK;
    private static final boolean OK;

    static {
        Field enable = null, tuto = null;
        Method usable = null, cd = null, clear = null, add = null, right = null;
        boolean ok = false;
        try {
            Class<?> ch = Class.forName("bettercombat.mod.util.ConfigurationHandler");
            enable = ch.getField("enableOffHandAttack");
            usable = ch.getMethod("isItemAttackUsable", Item.class);

            Class<?> eh = Class.forName("bettercombat.mod.handler.EventHandlers");
            tuto = eh.getField("TUTO_CAP");

            Class<?> cap = Class.forName("bettercombat.mod.capability.CapabilityOffhandCooldown");
            cd = cap.getMethod("getOffhandCooldown");

            Class<?> helpers = Class.forName("bettercombat.mod.util.Helpers");
            clear = helpers.getMethod("clearOldModifiers", EntityLivingBase.class, ItemStack.class);
            add = helpers.getMethod("addNewModifiers", EntityLivingBase.class, ItemStack.class);

            right = Class.forName("bettercombat.mod.client.handler.EventHandlersClient")
                    .getMethod("onMouseRightClick");
            ok = true;
        } catch (Throwable ignored) { }
        ENABLE_OFFHAND = enable;
        IS_ITEM_USABLE = usable;
        TUTO_CAP = tuto;
        GET_OH_CD = cd;
        CLEAR_MOD = clear;
        ADD_MOD = add;
        ON_RIGHT_CLICK = right;
        OK = ok;
    }

    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (!OK) return;
        if (!BCConfig.EverythingNunchaku.getBooleanValue()) return;
        if (!BCConfig.RLCombatOffhand.getBooleanValue()) return;
        try {
            if (!ENABLE_OFFHAND.getBoolean(null)) return;
        } catch (Throwable t) { return; }

        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayerSP player = mc.player;
        Entity rvEntity = mc.getRenderViewEntity();
        if (player == null || rvEntity == null || player.isSpectator()) return;
        if (!player.getActiveItemStack().isEmpty()) return;
        if (player.getHeldItemMainhand().getItem() instanceof ItemShield) return;

        Item off = player.getHeldItemOffhand().getItem();
        if (!isClientNunchakuOffhand(off)) return;
        if (!mc.gameSettings.keyBindUseItem.isKeyDown()) return;

        INunchakuCombo ncap = player.getCapability(NunchakuComboProvider.NUNCHAKUCOMBO_CAP, null);
        if (ncap != null && !ncap.isSpinning() && BCConfig.RLCombatOffhandNunchaku.getBooleanValue()) {
            BetterSurvivalPacketHandler.NETWORK.sendToServer(new MessageNunchakuSpinClient(true));
            ncap.setSpinning(true);
        }

        try {
            CLEAR_MOD.invoke(null, player, player.getHeldItemMainhand());
            ADD_MOD.invoke(null, player, player.getHeldItemOffhand());

            RayTraceResult mov = EntityRendererHook.pointedObject(
                    rvEntity, player, EnumHand.OFF_HAND, mc.world, mc.getRenderPartialTicks());

            CLEAR_MOD.invoke(null, player, player.getHeldItemOffhand());
            ADD_MOD.invoke(null, player, player.getHeldItemMainhand());

            @SuppressWarnings("unchecked")
            Capability<Object> token = (Capability<Object>) TUTO_CAP.get(null);
            Object coh = token == null ? null : player.getCapability(token, null);
            int remaining = coh == null ? 1 : (Integer) GET_OH_CD.invoke(coh);

            if (remaining <= 0 && mov != null && mov.entityHit != null && mov.entityHit != player) {
                ON_RIGHT_CLICK.invoke(null);
            }
        } catch (Throwable ignored) { }
    }

    public static boolean isClientNunchakuOffhand(Item item) {
        try {
            return (Boolean) IS_ITEM_USABLE.invoke(null, item)
                    && NunchakuConfigProvider.isClientNunchaku(item);
        } catch (Throwable t) {
            return false;
        }
    }
}