package com.blue.bluesclient.modmixins.somanyenchantments;

import com.blue.bluesclient.config.BCConfig;
import com.shultrea.rin.enchantments.base.EnchantmentBase;
import com.shultrea.rin.enchantments.tool.EnchantmentReinforcedSharpness;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EntityDamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(
        value = EnchantmentReinforcedSharpness.class,
        remap = false
)
public abstract class EnchantmentReinforcedSharpnessMixin
        extends EnchantmentBase {

    protected EnchantmentReinforcedSharpnessMixin(
            String name, Rarity rarity, EntityEquipmentSlot... slots) {
        super(name, rarity, slots);
    }

    @Inject(
            method = "calcDamageByCreature",
            at = @At("HEAD"),
            cancellable = true,
            remap = true,
            require = 1
    )
    private void bluesclient$legacyDamage(
            int level,
            EnumCreatureAttribute creatureType,
            CallbackInfoReturnable<Float> cir) {

        if (!BCConfig.EnchantmentDebug.getBooleanValue()) {
            return;
        }

        cir.setReturnValue(
                this.isEnabled() ? 0.75F * level + 0.5F : 0.0F
        );
    }

    @Override
    public void onEntityDamagedAlt(
            EntityLivingBase user,
            Entity target,
            ItemStack stack,
            int level) {

        if (!BCConfig.EnchantmentDebug.getBooleanValue()) {
            super.onEntityDamagedAlt(user, target, stack, level);
            return;
        }

        if (!this.isEnabled()
                || !(user instanceof EntityPlayer)
                || !(target instanceof EntityLivingBase)) {
            return;
        }

        EntityLivingBase victim = (EntityLivingBase) target;

        int armor = Math.min(victim.getTotalArmorValue(), 20);
        int cappedLevel = Math.min(level, 9);

        float damage = (20 - armor) / (10 - cappedLevel);

        if (!victim.getIsInvulnerable()) {
            victim.setEntityInvulnerable(true);
            victim.hurtResistantTime = 0;

            victim.attackEntityFrom(
                    new EntityDamageSource("player", user),
                    damage
            );

            victim.setEntityInvulnerable(false);
        } else {
            victim.attackEntityFrom(
                    new EntityDamageSource("player", user),
                    damage
            );
        }
    }
}
