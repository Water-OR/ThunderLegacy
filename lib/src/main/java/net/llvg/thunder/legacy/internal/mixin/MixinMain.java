package net.llvg.thunder.legacy.internal.mixin;

import com.llamalad7.mixinextras.injector.ModifyReceiver;
import net.llvg.thunder.legacy.internal.Internal;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.llvg.thunder.legacy.internal.mixin_callback.CallbackMixinMainKt.*;

@Mixin (
  value = Main.class,
  remap = false
)
public abstract class MixinMain {
    @Inject (
      method = "main",
      at = @At ("HEAD")
    )
    private static void mainInject(
      String[] strings,
      CallbackInfo ci
    ) {
        Internal.init();
    }
    
    @ModifyReceiver (
      method = "main",
      at = @At (
        value = "INVOKE",
        target = "Lnet/minecraft/client/Minecraft;run()V",
        remap = true
      )
    )
    private static Minecraft mainModifyReceiver(Minecraft instance) {
        postMinecraftInstantiationEvent(instance);
        return instance;
    }
}
