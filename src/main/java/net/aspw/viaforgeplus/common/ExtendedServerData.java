package net.aspw.viaforgeplus.common;

import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;

public interface ExtendedServerData {

    ProtocolVersion viaForgePlus$getVersion();

    void viaForgePlus$setVersion(final ProtocolVersion version);

}
