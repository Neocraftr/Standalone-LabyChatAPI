package de.neocraftr.labychatapi.packets;

import de.neocraftr.labychatapi.handeling.PacketHandler;
import de.neocraftr.labychatapi.PacketBuf;

public class PacketActionPlayResponse extends Packet {
    private short requestId;
    private boolean allowed;
    private String reason;

    public PacketActionPlayResponse() {}

    public PacketActionPlayResponse(boolean allowed) {
        this.allowed = allowed;
    }

    public void read(PacketBuf buf) {
        this.requestId = buf.readShort();
        this.allowed = buf.readBoolean();
        if (!this.allowed) {
            this.reason = buf.readString();
        }

    }

    public void write(PacketBuf buf) {
        buf.writeShort(this.requestId);
        buf.writeBoolean(this.allowed);
        if (!this.allowed) {
            buf.writeString(this.reason);
        }

    }

    public void handle(PacketHandler packetHandler) {
        packetHandler.handle(this);
    }

    public boolean isAllowed() {
        return this.allowed;
    }

    public short getRequestId() {
        return this.requestId;
    }

    public String getReason() {
        return this.reason;
    }
}
