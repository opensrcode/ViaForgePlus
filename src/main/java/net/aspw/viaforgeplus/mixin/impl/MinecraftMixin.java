package net.aspw.viaforgeplus.mixin.impl;

import net.aspw.viaforgeplus.ViaForgePlus;
import net.aspw.viaforgeplus.vfphook.VersionDiffPatches;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Minecraft.class, priority = 1019)
public abstract class MinecraftMixin {

    @Inject(method = "startGame", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;checkGLError(Ljava/lang/String;)V", ordinal = 2, shift = At.Shift.AFTER))
    private void injectMod(CallbackInfo callbackInfo) {
        ViaForgePlus.init();
    }

    @Inject(method = "clickMouse", at = @At("HEAD"), cancellable = true)
    private void fixAttackOrder(CallbackInfo ci) {
        VersionDiffPatches.fixedAttackOrder(ci);
    }

    @Inject(method = "sendClickBlockToController", at = @At("HEAD"), cancellable = true)
    private void blockBreakUnderThan1_7Hook(boolean leftClick, CallbackInfo ci) {
        VersionDiffPatches.blockBreakUnderThan1_7Hook(leftClick, ci);
    }
}
