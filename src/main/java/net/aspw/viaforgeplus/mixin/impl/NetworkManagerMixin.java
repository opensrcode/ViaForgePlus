package net.aspw.viaforgeplus.mixin.impl;

import net.aspw.viaforgeplus.VfpMain;
import io.netty.channel.Channel;
import net.aspw.viaforgeplus.ViaForgePlus;
import net.aspw.viaforgeplus.vfphook.PacketFixer;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetworkManager.class)
public class NetworkManagerMixin {

    @Shadow private Channel channel;

    @Inject(method = "setCompressionTreshold", at = @At("RETURN"))
    public void reorderPipeline(int p_setCompressionTreshold_1_, CallbackInfo ci) {
        ViaForgePlus.vfpMain.reorderCompression(channel);
    }

    @Inject(method = "sendPacket(Lnet/minecraft/network/Packet;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/NetworkManager;isChannelOpen()Z"), cancellable = true)
    private void handlePacket(final Packet<?> packet, final CallbackInfo ci) {
        PacketFixer.handlePacket(packet, ci);
    }
}
