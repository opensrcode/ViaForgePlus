package net.aspw.viaforgeplus.vfphook;

import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import net.aspw.viaforgeplus.IMinecraft;
import net.aspw.viaforgeplus.VfpMain;
import net.aspw.viaforgeplus.ViaForgePlus;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.Objects;

public class VersionDiffPatches {

    public static void blockBreakUnderThan1_7Hook(boolean leftClick, CallbackInfo ci) {
        ci.cancel();

        if (!leftClick)
            IMinecraft.mc.leftClickCounter = 0;

        if (IMinecraft.mc.leftClickCounter <= 0) {
            if (leftClick && IMinecraft.mc.objectMouseOver != null && IMinecraft.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                BlockPos blockPos = IMinecraft.mc.objectMouseOver.getBlockPos();

                if (IMinecraft.mc.thePlayer.isUsingItem() && (ViaForgePlus.vfpMain.getTargetVersion().newerThanOrEqualTo(ProtocolVersion.v1_8) || IMinecraft.mc.isSingleplayer())) return;

                if (IMinecraft.mc.theWorld.getBlockState(blockPos).getBlock().getMaterial() != Material.air && IMinecraft.mc.playerController.onPlayerDamageBlock(blockPos, IMinecraft.mc.objectMouseOver.sideHit)) {
                    IMinecraft.mc.effectRenderer.addBlockHitEffects(blockPos, IMinecraft.mc.objectMouseOver.sideHit);
                    IMinecraft.mc.thePlayer.swingItem();
                }
            } else {
                IMinecraft.mc.playerController.resetBlockRemoving();
            }
        }
    }

    public static void fixedAttackOrder(CallbackInfo ci) {
        ci.cancel();

        if (IMinecraft.mc.leftClickCounter <= 0) {
            if (IMinecraft.mc.objectMouseOver != null && Objects.requireNonNull(IMinecraft.mc.objectMouseOver.typeOfHit) != MovingObjectPosition.MovingObjectType.ENTITY) {
                IMinecraft.mc.thePlayer.swingItem();
            }

            if (IMinecraft.mc.objectMouseOver != null) {
                switch (IMinecraft.mc.objectMouseOver.typeOfHit) {
                    case ENTITY:
                        sendFixedAttack(IMinecraft.mc.thePlayer, IMinecraft.mc.objectMouseOver.entityHit);
                        break;

                    case BLOCK:
                        BlockPos blockpos = IMinecraft.mc.objectMouseOver.getBlockPos();

                        if (IMinecraft.mc.theWorld.getBlockState(blockpos).getBlock().getMaterial() != Material.air) {
                            IMinecraft.mc.playerController.clickBlock(blockpos, IMinecraft.mc.objectMouseOver.sideHit);
                            break;
                        }

                    case MISS:
                    default:
                        if (IMinecraft.mc.playerController.isNotCreative()) {
                            IMinecraft.mc.leftClickCounter = 10;
                        }
                }
            }
        }
    }

    private static void sendFixedAttack(final EntityPlayer entityIn, final Entity target) {
        if (!IMinecraft.mc.isSingleplayer() && ViaForgePlus.vfpMain.getTargetVersion().newerThan(ProtocolVersion.v1_8)) {
            IMinecraft.mc.playerController.attackEntity(entityIn, target);
            IMinecraft.mc.thePlayer.swingItem();
        } else {
            IMinecraft.mc.thePlayer.swingItem();
            IMinecraft.mc.playerController.attackEntity(entityIn, target);
        }
    }

    public static void pushOutHook(CallbackInfoReturnable<Boolean> cir) {
        if (shouldSwimOrCrawl() || !IMinecraft.mc.isSingleplayer() && ViaForgePlus.vfpMain.getTargetVersion().newerThanOrEqualTo(ProtocolVersion.v1_13) && IMinecraft.mc.thePlayer.isSneaking())
            cir.setReturnValue(false);
    }

    private static boolean isUnderWater() {
        final World world = IMinecraft.mc.thePlayer.getEntityWorld();
        double eyeBlock = IMinecraft.mc.thePlayer.posY + (double) IMinecraft.mc.thePlayer.getEyeHeight() - 0.25;
        BlockPos blockPos = new BlockPos(IMinecraft.mc.thePlayer.posX, eyeBlock, IMinecraft.mc.thePlayer.posZ);

        return world.getBlockState(blockPos).getBlock().getMaterial() == Material.water && !(IMinecraft.mc.thePlayer.ridingEntity instanceof EntityBoat);
    }

    private static boolean canSwim() {
        return !IMinecraft.mc.thePlayer.isSneaking() && IMinecraft.mc.thePlayer.isInWater() && IMinecraft.mc.thePlayer.isSprinting() && isUnderWater();
    }

    public static boolean shouldSwimOrCrawl() {
        AxisAlignedBB box = IMinecraft.mc.thePlayer.getEntityBoundingBox();
        AxisAlignedBB crawlBB = new AxisAlignedBB(box.minX, box.minY + 0.9, box.minZ, box.minX + 0.6, box.minY + 1.5, box.minZ + 0.6);

        return !IMinecraft.mc.isSingleplayer() && ViaForgePlus.vfpMain.getTargetVersion().newerThanOrEqualTo(ProtocolVersion.v1_13) && (canSwim() || !IMinecraft.mc.theWorld.getCollisionBoxes(crawlBB).isEmpty());
    }
}
