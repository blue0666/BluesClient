package com.blue.bluesclient.modmixins.somanyenchantments;

import bettercombat.mod.event.RLCombatModifyDamageEvent;
import com.blue.bluesclient.config.BCConfig;
import com.shultrea.rin.enchantments.weapon.damagemultiplier.EnchantmentReviledBlade;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EnchantmentReviledBlade.class, remap = false)
public abstract class EnchantmentReviledBladeMixin {
    @Inject(
            method = "onLivingDamageEvent",
            at = @At("HEAD"),
            cancellable = true,
            require = 1
    )
    private void bluesclient$skipModern(
            LivingDamageEvent event, CallbackInfo ci) {

        if (BCConfig.EnchantmentDebug.getBooleanValue()) {
            ci.cancel();
        }
    }

    @Unique
    @SubscribeEvent(priority = EventPriority.LOW)
    public void bluesclient$legacyReviledBlade(
            RLCombatModifyDamageEvent.Post event) {

        if (!BCConfig.EnchantmentDebug.getBooleanValue()) {
            return;
        }

        if (event.getEntityPlayer() == null) {
            return;
        }

        ItemStack stack = event.getStack();

        if (stack.isEmpty()
                || !(event.getTarget() instanceof EntityLivingBase)) {
            return;
        }

        EntityLivingBase target =
                (EntityLivingBase) event.getTarget();

        int level = EnchantmentHelper.getEnchantmentLevel(
                (Enchantment) (Object) this,
                stack
        );

        if (level <= 0 || target.getMaxHealth() <= 0.0F) {
            return;
        }

        float missingHealth = 1.0F - MathHelper.clamp(
                target.getHealth() / target.getMaxHealth(),
                0.0F,
                1.0F
        );

        float extraDamage = Math.max(
                0.0F,
                event.getBaseDamage() + event.getDamageModifier()
        ) * missingHealth * (level / 8.0F);

        event.setDamageModifier(
                event.getDamageModifier() + extraDamage
        );
    }
}
