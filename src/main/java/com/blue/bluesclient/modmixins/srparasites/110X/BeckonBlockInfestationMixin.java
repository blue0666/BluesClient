//package com.blue.bluesclient.modmixins.srparasites;
//
//import com.blue.bluesclient.config.BCConfig;
//import com.dhanantry.scapeandrunparasites.util.convert.BeckonBlockInfestation;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.World;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//
//import java.util.Random;
//
//@Mixin(value=BeckonBlockInfestation.class,remap = false)
//public class BeckonBlockInfestationMixin {
//    @Inject(method = "beckonInfestation", at = @At("HEAD"), cancellable = true, remap = false)
//    private static void bluesclient$noBeckonInfest(World worldIn, BlockPos pos, Random rand, int stage, boolean fromVenkrol, CallbackInfo ci) {
//        if (BCConfig.NoSRPBlockSpread.getBooleanValue()) {
//            ci.cancel();
//        }
//    }
//}
