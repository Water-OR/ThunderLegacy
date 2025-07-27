package net.llvg.thunder.legacy.internal.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.llvg.thunder.legacy.internal.mixin_callback.CallbackMixinChunkKt.*;

@Mixin (Chunk.class)
public class MixinChunk {
    @Inject (
      method = "setBlockState",
      at = @At (
        value = "INVOKE",
        target = "Lnet/minecraft/block/state/IBlockState;getBlock()Lnet/minecraft/block/Block;",
        ordinal = 0
      ),
      cancellable = true
    )
    private void setBlockStateInject(
      BlockPos pos,
      IBlockState newState,
      CallbackInfoReturnable<IBlockState> cir,
      @Local (ordinal = 1)
      IBlockState oldState
    ) {
        if (postBlockChangeEvent(oldState, newState)) cir.setReturnValue(null);
    }
}
