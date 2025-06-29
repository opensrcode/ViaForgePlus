package net.aspw.viaforgeplus;

import net.minecraftforge.fml.common.Mod;

@Mod(modid = "viaforgeplus", name = "ViaForgePlus", acceptableRemoteVersions = "*", clientSideOnly=true, useMetadata=true)
public class ViaForgePlus {

    public static VfpMain vfpMain;
    public static UpdatesChecker updatesChecker;

    public static void init() {
        updatesChecker = new UpdatesChecker();
        vfpMain = new VfpMain();
    }
}
