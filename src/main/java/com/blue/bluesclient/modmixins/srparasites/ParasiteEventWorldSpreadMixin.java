package com.blue.bluesclient.modmixins.srparasites;

import com.blue.bluesclient.config.BCConfig;
import com.dhanantry.scapeandrunparasites.util.ParasiteEventWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(value= ParasiteEventWorld.class, remap = false)
public class ParasiteEventWorldSpreadMixin {
    @Inject(method = "spreadBiomeBlockStain", at = @At("HEAD"), cancellable = true, remap = false)
    private static void bluesclient$noBlockStain(World worldIn, BlockPos pos, Random rand, CallbackInfo ci) {
        if (BCConfig.NoSRPBlockSpread.getBooleanValue()) {
            ci.cancel();
        }
    }

    @Inject(method = "SpreadBiome", at = @At("HEAD"), cancellable = true, remap = false)
    private static void bluesclient$noBiomeSpread(World worldIn, BlockPos pos, int age, CallbackInfo ci) {
        if (BCConfig.NoSRPBlockSpread.getBooleanValue()) {
            ci.cancel();
        }
    }
    @Inject(method = "canInfestBlock", at = @At("HEAD"), cancellable = true, remap = false)
    private static void bluesclient$noCanInfest(
            World worldIn, BlockPos pos, Random rand, int stage, boolean fromVenkrol, CallbackInfo ci) {
        if (BCConfig.NoSRPBlockSpread.getBooleanValue()) {
            ci.cancel();
        }
    }
}