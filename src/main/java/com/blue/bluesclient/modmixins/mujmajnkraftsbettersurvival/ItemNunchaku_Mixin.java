package com.blue.bluesclient.modmixins.mujmajnkraftsbettersurvival;

import com.blue.bluesclient.config.BCConfig;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mujmajnkraft.bettersurvival.capabilities.nunchakucombo.INunchakuCombo;
import com.mujmajnkraft.bettersurvival.capabilities.nunchakucombo.NunchakuComboProvider;
import com.mujmajnkraft.bettersurvival.config.ForgeConfigHandler;
import com.mujmajnkraft.bettersurvival.items.ItemNunchaku;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(targets = "com.mujmajnkraft.bettersurvival.items.ItemNunchaku$1")
public class ItemNunchaku_Mixin {
    @Inject(method = "apply", at = @At("HEAD"), cancellable = true, remap = false)
    private void bluesclient$applySpin(
            ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn,
            CallbackInfoReturnable<Float> cir) {
        if (entityIn == null) {
            cir.setReturnValue(0.0F);
            return;
        }
        boolean main = entityIn.getHeldItemMainhand() == stack;
        boolean treatAsHeld = main;
        if (BCConfig.RLCombatOffhandNunchaku.getBooleanValue() && entityIn == Minecraft.getMinecraft().player) {
            if (main) {
                treatAsHeld = Minecraft.getMinecraft().gameSettings.keyBindAttack.isKeyDown();
            } else if (entityIn.getHeldItemOffhand() == stack) {
                treatAsHeld = Minecraft.getMinecraft().gameSettings.keyBindUseItem.isKeyDown();
            }
        }
        if (treatAsHeld) {
            INunchakuCombo cap = entityIn.getCapability(NunchakuComboProvider.NUNCHAKUCOMBO_CAP, null);
            if (cap != null && cap.isSpinning()) {
                cir.setReturnValue(1.0F);
                return;
            }
        }
        cir.setReturnValue(0.0F);
    }
}
