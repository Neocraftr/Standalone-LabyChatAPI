package de.neocraftr.labychatapi.packets;

import de.neocraftr.labychatapi.handeling.PacketHandler;
import de.neocraftr.labychatapi.PacketBuf;

public class PacketLoginVersion extends Packet {
    private int versionId;
    private String versionName;
    private String updateLink;

    public PacketLoginVersion() {}

    public PacketLoginVersion(int internalVersion, String externalVersion) {
        this.versionId = internalVersion;
        this.versionName = externalVersion;
    }

    public void read(PacketBuf buf) {
        this.versionId = buf.readInt();
        this.versionName = buf.readString();
        this.updateLink = buf.readString();
    }

    public void write(PacketBuf buf) {
        buf.writeInt(this.versionId);
        buf.writeString(this.versionName);
        buf.writeString("");
    }

    public void handle(PacketHandler packetHandler) {
        packetHandler.handle(this);
    }

    public String getVersionName() {
        return this.versionName;
    }

    public int getVersionID() {
        return this.versionId;
    }

    public String getUpdateLink() {
        return this.updateLink;
    }
}
