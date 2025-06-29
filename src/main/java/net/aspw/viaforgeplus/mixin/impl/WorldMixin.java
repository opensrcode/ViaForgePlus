package net.aspw.viaforgeplus.mixin.impl;

import net.aspw.viaforgeplus.vfphook.MotionFixes;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public abstract class WorldMixin implements IBlockAccess {

    @Inject(method = "destroyBlock", at = @At("HEAD"), cancellable = true)
    private void handleDestroyBlockSound(BlockPos p_destroyBlock_1_, boolean p_destroyBlock_2_, CallbackInfoReturnable<Boolean> cir) {
        MotionFixes.handleBlockDestroySound(p_destroyBlock_1_, p_destroyBlock_2_, cir);
    }
}
