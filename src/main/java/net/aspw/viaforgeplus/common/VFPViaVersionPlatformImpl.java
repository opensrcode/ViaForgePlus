package net.aspw.viaforgeplus.common;

import com.viaversion.vialoader.impl.platform.ViaVersionPlatformImpl;
import java.io.File;

public final class VFPViaVersionPlatformImpl extends ViaVersionPlatformImpl {

    public VFPViaVersionPlatformImpl(final File rootFolder) {
        super(rootFolder);
    }

    @Override
    public String getPlatformName() {
        return "ViaForgePlus";
    }
}
