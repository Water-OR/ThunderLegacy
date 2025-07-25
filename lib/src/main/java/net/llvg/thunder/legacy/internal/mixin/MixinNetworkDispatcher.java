package net.llvg.thunder.legacy.internal.mixin;

import io.netty.channel.ChannelOutboundHandler;
import io.netty.channel.SimpleChannelInboundHandler;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.network.handshake.NetworkDispatcher;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.llvg.thunder.legacy.internal.mixin_callback.CallbackMixinNetworkDispatcherKt.*;

@Mixin (
  value = NetworkDispatcher.class,
  remap = false
)
public abstract class MixinNetworkDispatcher
  extends SimpleChannelInboundHandler<Packet<?>>
  implements ChannelOutboundHandler
{
    @Shadow
    @Final
    public NetworkManager manager;
    
    @Inject (
      method = "completeClientSideConnection",
      at = @At ("TAIL")
    )
    private void completeClientSideConnectionInject(
      NetworkDispatcher.ConnectionType type,
      CallbackInfo ci
    ) {
        injectHandler(manager.channel().pipeline());
    }
}
