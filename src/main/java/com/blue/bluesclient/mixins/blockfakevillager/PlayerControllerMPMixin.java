package com.blue.bluesclient.mixins.blockfakevillager;

import com.blue.bluesclient.config.BCConfig;
import com.blue.bluesclient.feat.blockfakevillager.FakeVillagerHandler;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerControllerMP.class)
public class PlayerControllerMPMixin {
    @Inject(
            method = "interactWithEntity(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/entity/Entity;Lnet/minecraft/util/EnumHand;)Lnet/minecraft/util/EnumActionResult;",
            at = @At("HEAD"),
            cancellable = true
    )
    private void bluesBlockFakeVillager(EntityPlayer player, Entity target, EnumHand hand,
                                        CallbackInfoReturnable<EnumActionResult> cir) {
        if (shouldBlock(target)) {
            cir.setReturnValue(EnumActionResult.FAIL);
        }
    }
    @Inject(
            method = "interactWithEntity(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/RayTraceResult;Lnet/minecraft/util/EnumHand;)Lnet/minecraft/util/EnumActionResult;",
            at = @At("HEAD"),
            cancellable = true
    )
    private void bluesBlockFakeVillagerAt(EntityPlayer player, Entity target, RayTraceResult ray, EnumHand hand,
                                          CallbackInfoReturnable<EnumActionResult> cir) {
        if (shouldBlock(target)) {
            cir.setReturnValue(EnumActionResult.FAIL);
        }
    }

    @Unique
    private static boolean shouldBlock(Entity target) {
        return BCConfig.BlockFakeLibrarianTrade.getBooleanValue()
                && FakeVillagerHandler.isTrapVillager(target);
    }
}
