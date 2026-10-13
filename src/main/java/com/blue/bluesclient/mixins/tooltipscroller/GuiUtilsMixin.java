package com.blue.bluesclient.mixins.tooltipscroller;

import com.blue.bluesclient.config.BCConfig;
import com.blue.bluesclient.feat.tooltipscroller.TooltipScrollHandler;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.config.GuiUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = GuiUtils.class, remap = false)
public class GuiUtilsMixin {

    @Inject(
            method = "drawHoveringText(Lnet/minecraft/item/ItemStack;Ljava/util/List;IIIIILnet/minecraft/client/gui/FontRenderer;)V",
            at = @At(
                    value = "NEW",
                    target = "net/minecraftforge/client/event/RenderTooltipEvent$Color"
            ),
            remap = false
    )
    private static void bluesclient$afterClampBeforeColor(
            ItemStack stack, List<String> textLines, int mouseX, int mouseY, int screenWidth, int screenHeight, int maxTextWidth, FontRenderer font, CallbackInfo ci, @Local(name = "tooltipY") LocalIntRef tooltipY
    ) {
        if (!BCConfig.ToolTipScroller.getBooleanValue()) return;
        tooltipY.set(tooltipY.get() + TooltipScrollHandler.getOffsetY());
    }
}