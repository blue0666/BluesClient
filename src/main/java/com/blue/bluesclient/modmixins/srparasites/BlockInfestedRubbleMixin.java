package com.blue.bluesclient.modmixins.srparasites;
import com.blue.bluesclient.config.BCConfig;
import com.dhanantry.scapeandrunparasites.block.BlockInfestedRubble;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.Random;
@Mixin(value = BlockInfestedRubble.class, remap = false)
public class BlockInfestedRubbleMixin {
    @Inject(method = "randomTick", at = @At("HEAD"), cancellable = true, remap = true)
    private void bluesclient$noMature(World worldIn, BlockPos pos, IBlockState state, Random random, CallbackInfo ci) {
        if (BCConfig.NoSRPBlockSpread.getBooleanValue()) {
            ci.cancel();
        }
    }
}