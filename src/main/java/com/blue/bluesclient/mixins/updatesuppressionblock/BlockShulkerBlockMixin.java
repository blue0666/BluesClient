package com.blue.bluesclient.mixins.updatesuppressionblock;

import com.blue.bluesclient.config.BCConfig;
import com.blue.bluesclient.feat.updatesuppressionblock.BluesClientCCEException;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockShulkerBox.class)
public class BlockShulkerBlockMixin {
    @Inject(
            method = "getComparatorInputOverride",
            at = @At("HEAD"),
            remap = true
    )
    public void ManuallyThrowUpdateSuppression(IBlockState blockState, World world, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        if (BCConfig.UpdateSuppressionBlock.getBooleanValue()) {
            if (world.isRemote) return;
            TileEntity te = world.getTileEntity(pos);
            if (!(te instanceof TileEntityShulkerBox)) return;

            TileEntityShulkerBox shulkerBoxBlockEntity = (TileEntityShulkerBox) te;
            if ((shulkerBoxBlockEntity).hasCustomName()) {
                String customName = shulkerBoxBlockEntity.getName();
                if (customName.equals("更新抑制器")  || customName.equalsIgnoreCase("updatesuppressor") ) {
                    throw BluesClientCCEException.INSTANCE;
                }
            }
        }
    }
}
