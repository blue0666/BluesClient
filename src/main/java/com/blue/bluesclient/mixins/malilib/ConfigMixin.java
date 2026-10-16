package com.blue.bluesclient.mixins.malilib;

import com.blue.bluesclient.config.gui.BCConfigScreen;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import fi.dy.masa.malilib.config.options.IConfigBase;
import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.widgets.WidgetListConfigOptions;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

@Mixin(value = WidgetListConfigOptions.class, remap = false)
public class ConfigMixin {
    //因为Malilib1.12版本不兼容多语言，所以做Mixin修改部分配置语言不兼容的结果，包括搜索框不切换语言，GUI界面调整
    @Shadow
    @Final
    protected GuiConfigsBase parent;

    @Inject(
            method = "getMaxNameLengthWrapped",
            at = @At("HEAD"),
            cancellable = true
    )
    private void bluesclient$getNameWidth(
            List<GuiConfigsBase.ConfigOptionWrapper> wrappers, CallbackInfoReturnable<Integer> cir) {

        if (!(this.parent instanceof BCConfigScreen)) {
            return;
        }

        int maxWidth = 0;

        for (GuiConfigsBase.ConfigOptionWrapper entry : wrappers) {
            IConfigBase config = entry.getConfig();

            if (config != null) {
                int width = Minecraft.getMinecraft().fontRenderer
                        .getStringWidth(config.getConfigGuiDisplayName());
                maxWidth = Math.max(maxWidth, width);
            }
        }

        cir.setReturnValue(maxWidth);
    }

    @Inject(
            method = "getEntryStringsForFilter(Lfi/dy/masa/malilib/gui/GuiConfigsBase$ConfigOptionWrapper;)Ljava/util/List;",
            at = @At("HEAD"),
            cancellable = true
    )
    private void bluesclient$getSearchStrings(
            GuiConfigsBase.ConfigOptionWrapper entry,
            CallbackInfoReturnable<List<String>> cir) {

        if (!(this.parent instanceof BCConfigScreen)) {
            return;
        }

        IConfigBase config = entry.getConfig();

        cir.setReturnValue(config == null
                ? Collections.emptyList()
                : Collections.singletonList(
                config.getConfigGuiDisplayName().toLowerCase(Locale.ROOT)
        ));
    }
}
