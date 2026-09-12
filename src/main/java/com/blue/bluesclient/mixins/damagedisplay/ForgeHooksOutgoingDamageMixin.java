package com.blue.bluesclient.mixins.damagedisplay;

import com.blue.bluesclient.event.forge.OutgoingDamageDisplayHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.ForgeHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ForgeHooks.class, remap = false)
public abstract class ForgeHooksOutgoingDamageMixin {

    @Inject(method = "onLivingHurt", at = @At("HEAD"), remap = false)
    private static void bluesclient$rawOutgoing(
            EntityLivingBase entity,
            DamageSource src,
            float amount,
            CallbackInfoReturnable<Float> cir) {
        OutgoingDamageDisplayHandler.onRawOutgoing(entity, src, amount);
    }

    @Inject(method = "onLivingDamage", at = @At("RETURN"), remap = false)
    private static void bluesclient$finalOutgoing(
            EntityLivingBase entity,
            DamageSource src,
            float amount,
            CallbackInfoReturnable<Float> cir) {
        Float ret = cir.getReturnValue();
        float finalAmount = ret == null ? 0.0F : ret;
        OutgoingDamageDisplayHandler.onFinalOutgoing(entity, src, finalAmount);
    }
}
