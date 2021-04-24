package de.neocraftr.labychatapi.packets;

import de.neocraftr.labychatapi.handeling.PacketHandler;
import de.neocraftr.labychatapi.PacketBuf;

public class PacketNotAllowed extends Packet {
    private String reason;
    private long until;

    public PacketNotAllowed() {}

    public PacketNotAllowed(String reason, long until) {
        this.reason = reason;
        this.until = until;
    }

    public void read(PacketBuf buf) {
        this.reason = buf.readString();
        this.until = buf.readLong();
    }

    public void write(PacketBuf buf) {
        buf.writeString(this.reason);
        buf.writeLong(this.until);
    }

    public void handle(PacketHandler packetHandler) {
        packetHandler.handle(this);
    }

    public String getReason() {
        return this.reason;
    }

    public long getUntil() {
        return this.until;
    }
}

