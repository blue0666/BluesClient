package com.blue.bluesclient.mixins.updatesuppressionblock;

import com.blue.bluesclient.config.BCConfig;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Chunk.class)
public class ChunkMixin {
    @WrapOperation(
            method = "setBlockState",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/tileentity/TileEntity;shouldRefresh(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/block/state/IBlockState;)Z",
                    remap = false
            ),
            require = 2,
            allow = 2
    )
    private boolean bluesclient$preserveResidualTileEntity(TileEntity tile, World world, BlockPos pos,
                                                          IBlockState oldState, IBlockState newState,
                                                          Operation<Boolean> original) {
        if (!world.isRemote && BCConfig.UpdateSuppressionBlock.getBooleanValue()) {
            return false;
        }
        return original.call(tile, world, pos, oldState, newState);
    }
}
