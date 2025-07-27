package net.llvg.thunder.legacy.internal.mixin_callback

import net.llvg.thunder.legacy.event.post
import net.llvg.thunder.legacy.vanilla.BlockChangeEvent
import net.minecraft.block.state.IBlockState

internal fun postBlockChangeEvent(
    oldState: IBlockState,
    newState: IBlockState,
) = BlockChangeEvent.of(oldState, newState).apply { post() }.cancelled