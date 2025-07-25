package net.llvg.thunder.legacy.internal.mixin;

import net.llvg.thunder.legacy.internal.Internal;
import net.minecraft.client.Minecraft;
import net.minecraft.profiler.IPlayerUsage;
import net.minecraft.util.IThreadListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.llvg.thunder.legacy.internal.mixin_callback.CallbackMixinMinecraftKt.*;

@Mixin (Minecraft.class)
public abstract class MixinMinecraft
  implements IThreadListener, IPlayerUsage
{
    @Inject (
      method = "startGame",
      at = @At ("TAIL")
    )
    private void startGameInject(CallbackInfo ci) {
        postGameStartedEvent((Minecraft) (Object) this);
    }
    
    @Inject (
      method = "shutdownMinecraftApplet",
      at = @At ("HEAD")
    )
    private void shutdownMinecraftAppletInject(CallbackInfo ci) {
        try {
            postGameShutdownEvent();
        } catch (Throwable e) {
            try {
                Internal.logger.warn("Failure occur while shutting down", e);
            } catch (Throwable ignored) {
                // Do nothing
            }
        }
    }
}
