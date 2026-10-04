package com.blue.bluesclient.modmixins.somanyenchantments;

import com.blue.bluesclient.config.BCConfig;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.shultrea.rin.enchantments.weapon.crits.EnchantmentCriticalStrike;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(EnchantmentCriticalStrike.class)
public class EnchantmentCriticalStrikeMixin {
//    @WrapOperation(
//            method = "onCriticalHitEvent",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Ljava/util/Random;nextInt(I)I",
//                    ordinal = 0
//            ),
//            remap = false
//    )
//    private int forceSuccess(Random instance, int bound, Operation<Integer> original) {
//        if (BCConfig.MixinDebug.getBooleanValue()) {
//            return 0;
//        }
//        return original.call(instance, bound);
//    }
//
//    @Inject(method = "onCriticalHitEvent", at = @At("HEAD"),remap = false, cancellable = true)
//    private void forceCritical(CriticalHitEvent event, CallbackInfo ci) {
//        if(!BCConfig.MixinDebug.getBooleanValue()) ci.cancel();
//        EntityLivingBase attacker = event.getEntityLiving();
//        if (attacker == null) return;
//        if (!(event.getTarget() instanceof EntityLivingBase)) return;
//
//        ItemStack stack = attacker.getHeldItemMainhand();
//        if (stack.isEmpty()) return;
//
//        int level = EnchantmentHelper.getEnchantmentLevel((Enchantment)(Object)this, stack);
//        if (level <= 0) return;
//
//        if (event.getResult() != Event.Result.ALLOW && !event.isVanillaCritical()) {
//            event.setResult(Event.Result.ALLOW);
//        }
//    }
//
//    @WrapOperation(
//            method = "onCriticalHitEvent",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Ljava/util/Random;nextFloat()F",
//                    ordinal = 0
//            ),
//            remap = false
//    )
//    private float fixedFloat(Random instance, Operation<Float> original) {
//        if (BCConfig.MixinDebug.getBooleanValue()) {
//            return 0.5F;
//        }
//        return original.call(instance);
//    }
}
