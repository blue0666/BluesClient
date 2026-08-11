package com.blue.bluesclient.modmixins.firstaid;

import com.blue.bluesclient.config.BCConfig;
import ichttt.mods.firstaid.common.DataManagerWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = DataManagerWrapper.class, remap = false)
public abstract class DataManagerWrapper_Mixin {
    @Final
    @Shadow
    private EntityPlayer player;

    @Inject(method = "set", at = @At("HEAD"), remap = true, require = 1)
    private void bluesclient$watchSetHealth(DataParameter<?> key, Object value, CallbackInfo ci) {
        if (!BCConfig.DamageDisplay.getBooleanValue()) return;
        if (key != EntityLivingBase.HEALTH) return;
        if (player == null || player.world == null || player.world.isRemote) return;
        if (!(value instanceof Float)) return;

        float target = (Float) value;
        float orig = player.getHealth();
        float delta = target - orig;
        if (Math.abs(delta) < 0.001F) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player == null || !mc.player.getUniqueID().equals(player.getUniqueID())) return;

        player.sendMessage(new TextComponentString(
                TextFormatting.RED + "[setHealth类]" + TextFormatting.RESET
                        + String.format(" 目标:%.2f | 原血:%.2f | 数值:%.2f",
                        target, orig, delta)));
    }
}