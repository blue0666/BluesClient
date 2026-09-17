package com.blue.bluesclient.modmixins.somanyenchantments;

import com.blue.bluesclient.config.BCConfig;
import com.shultrea.rin.enchantments.base.EnchantmentCurse;
import com.shultrea.rin.enchantments.curses.EnchantmentPandorasCurse;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentPandorasCurse.class)
public abstract class EnchantmentPandorasCurseMixin extends EnchantmentCurse {
    public EnchantmentPandorasCurseMixin(String name, Rarity rarity, EntityEquipmentSlot... slots) {
        super(name, rarity, slots);
    }

    @Inject(
            method = "getTranslatedName",
            at = @At("RETURN"),
            cancellable = true,
            remap = true)
    public void getTranslatedName(int level, CallbackInfoReturnable<String> cir) {
        if (BCConfig.NoHiddenFlag.getBooleanValue()) {
            String name = I18n.translateToLocal(this.getName());
            name = TextFormatting.getTextWithoutFormattingCodes(name);
            cir.setReturnValue(TextFormatting.DARK_RED.toString() + TextFormatting.BOLD + name + TextFormatting.RESET);
        }
    }
}
