package net.aspw.viaforgeplus.mixin.impl;

import net.aspw.viaforgeplus.vfphook.PacketFixer;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetHandlerPlayClient.class)
public abstract class NetHandlerPlayClientMixin {

    @Inject(method = "handleConfirmTransaction", at = @At("HEAD"), cancellable = true)
    public void fix1_17ConfirmTransaction$playClient(S32PacketConfirmTransaction p_handleConfirmTransaction_1_, CallbackInfo ci) {
        PacketFixer.handle1_17ConfirmTransaction$playClient(p_handleConfirmTransaction_1_, ci);
    }
}
