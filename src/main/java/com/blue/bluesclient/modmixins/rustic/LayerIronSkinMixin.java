package com.blue.bluesclient.modmixins.rustic;

import com.blue.bluesclient.config.BCConfig;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import rustic.client.renderer.LayerIronSkin;

@Mixin(LayerIronSkin.class)
public class LayerIronSkinMixin {
    @Inject(
            method="doRenderLayer",
            at=@At("HEAD"),
            cancellable = true
    )
    public void DisableIronSkinMixin(EntityLivingBase entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, CallbackInfo ci){
        if (BCConfig.DisableIronSkinRenderer.getBooleanValue() && entityLivingBaseIn instanceof EntityPlayer) {
            ci.cancel();
        }
    }
}
