//package com.blue.bluesclient.modmixins.srparasites;
//
//import com.blue.bluesclient.config.BCConfig;
//import com.dhanantry.scapeandrunparasites.world.biome.BiomeParasiteBase;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.World;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//
//import java.util.Random;
//
//@Mixin(value = BiomeParasiteBase.class,remap=false)
//public class BiomeParasiteBaseMixin {
//    @Inject(method = "convertBlock", at = @At("HEAD"), cancellable = true, remap = false)
//    private void bluesclient$noConvertBlock(BlockPos helper, World worldIn, Random rand, CallbackInfoReturnable<Integer> cir) {
//        if (BCConfig.NoSRPBlockSpread.getBooleanValue()) {
//            cir.setReturnValue(0);
//        }
//    }
//}
