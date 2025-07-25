package net.llvg.thunder.legacy.internal.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.IChatComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static net.llvg.thunder.legacy.internal.mixin_callback.CallbackMixinNetHandlerPlayClientKt.*;

@Mixin (NetHandlerPlayClient.class)
public abstract class MixinNetHandlerPlayClient
  implements INetHandlerPlayClient
{
    @ModifyExpressionValue (
      method = "handleChat",
      at = @At (
        value = "INVOKE",
        target = "Lnet/minecraftforge/event/ForgeEventFactory;onClientChat(BLnet/minecraft/util/IChatComponent;)Lnet/minecraft/util/IChatComponent;",
        remap = false
      )
    )
    private IChatComponent handleChatModifyExpressionValue(
      IChatComponent original,
      @Local (argsOnly = true)
      S02PacketChat packet
    ) {
        if (packet.getType() != 2) postMessageEvent(original);
        return original;
    }
}
