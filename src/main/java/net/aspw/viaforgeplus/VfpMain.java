package net.aspw.viaforgeplus;

import com.viaversion.vialoader.ViaLoader;
import com.viaversion.vialoader.impl.platform.*;
import com.viaversion.vialoader.impl.viaversion.VLLoader;
import com.viaversion.vialoader.netty.CompressionReorderEvent;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.connection.UserConnectionImpl;
import com.viaversion.viaversion.protocol.ProtocolPipelineImpl;
import net.aspw.viaforgeplus.common.VFPVLInjector;
import net.aspw.viaforgeplus.common.ViaForgeVLLegacyPipeline;
import net.aspw.viaforgeplus.common.VFPViaVersionPlatformImpl;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import net.minecraft.network.NetworkManager;
import net.minecraft.realms.RealmsSharedConstants;

import java.io.File;

public class VfpMain implements IMinecraft {

    public final AttributeKey<UserConnection> LOCAL_VIA_USER = AttributeKey.valueOf("local_via_user");
    public final AttributeKey<NetworkManager> VF_NETWORK_MANAGER = AttributeKey.valueOf("encryption_setup");
    private final int NETWORK_PROTOCOL_VERSION = RealmsSharedConstants.NETWORK_PROTOCOL_VERSION;

    private ProtocolVersion targetVersion;

    public VfpMain() {
        final ProtocolVersion version = ProtocolVersion.getProtocol(NETWORK_PROTOCOL_VERSION);

        final File mainFolder = new File(mc.mcDataDir, "ViaForgePlus");

        ViaLoader.init(new VFPViaVersionPlatformImpl(mainFolder), new VLLoader(), new VFPVLInjector(), null, ViaBackwardsPlatformImpl::new, ViaRewindPlatformImpl::new, null, null  );

        this.setTargetVersion(version);

        ViaForgePlus.updatesChecker.check();
    }

    public void inject(final Channel channel, final NetworkManager networkManager) {
        if (getTargetVersion().equals(getNativeVersion())) return;

        channel.attr(VF_NETWORK_MANAGER).set(networkManager);

        final UserConnection user = new UserConnectionImpl(channel, true);
        new ProtocolPipelineImpl(user);

        channel.attr(LOCAL_VIA_USER).set(user);

        channel.pipeline().addLast(new ViaForgeVLLegacyPipeline(user, targetVersion));
    }

    public void reorderCompression(final Channel channel) {
        channel.pipeline().fireUserEventTriggered(CompressionReorderEvent.INSTANCE);
    }

    public ProtocolVersion getNativeVersion() {
        return ProtocolVersion.getProtocol(NETWORK_PROTOCOL_VERSION);
    }

    public ProtocolVersion getTargetVersion() {
        return targetVersion;
    }

    public void setTargetVersion(final ProtocolVersion targetVersion) {
        if (targetVersion == null) {
            throw new IllegalArgumentException("Target version cannot be null");
        }
        this.targetVersion = targetVersion;
    }
}
