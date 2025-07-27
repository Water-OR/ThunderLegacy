package net.llvg.thunder.legacy.vanilla

import net.llvg.thunder.legacy.event.Event
import net.minecraft.block.state.IBlockState

sealed interface BlockChangeEvent : Event.Cancellable {
    val oldState: IBlockState
    
    val newState: IBlockState
    
    companion object {
        private class Impl(
            override val oldState: IBlockState,
            override val newState: IBlockState,
        ) : BlockChangeEvent, Event.Cancellable.Default()
        
        internal fun of(
            oldState: IBlockState,
            newState: IBlockState,
        ): BlockChangeEvent = Impl(oldState, newState)
    }
}