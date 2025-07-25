package net.llvg.thunder.legacy.internal.mixin;

import net.minecraft.client.renderer.RenderGlobal;
import net.minecraftforge.client.ForgeHooksClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.llvg.thunder.legacy.internal.mixin_callback.CallbackMixinForgeHooksClientKt.*;

@Mixin (
  value = ForgeHooksClient.class,
  remap = false
)
public class MixinForgeHooksClient {
    @Inject (
      method = "dispatchRenderLast",
      at = @At ("HEAD")
    )
    private static void dispatchRenderLastInject(RenderGlobal context, float partialTicks, CallbackInfo ci) {
        postRenderWorldEvent(context, partialTicks);
    }
}
