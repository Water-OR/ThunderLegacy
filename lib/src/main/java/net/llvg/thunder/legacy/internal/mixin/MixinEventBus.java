package net.llvg.thunder.legacy.internal.mixin;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.llvg.thunder.legacy.internal.mixin_callback.CallbackMixinEventBusKt.*;

@Mixin (
  value = EventBus.class,
  remap = false
)
public class MixinEventBus {
    @Inject (
      method = "post",
      at = @At ("HEAD")
    )
    private void postInject(Event event, CallbackInfoReturnable<Boolean> cir) {
        if (this == (Object) MinecraftForge.EVENT_BUS) postForgeEventEvent(event);
    }
}
