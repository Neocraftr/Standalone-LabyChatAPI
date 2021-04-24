package de.neocraftr.labychatapi.packets;

import de.neocraftr.labychatapi.handeling.PacketHandler;
import de.neocraftr.labychatapi.PacketBuf;

public class PacketKick extends Packet {
    private String cause;

    public PacketKick() {}

    public PacketKick(String cause) {
        this.cause = cause;
    }

    public void read(PacketBuf buf) {
        this.cause = buf.readString();
    }

    public void write(PacketBuf buf) {
        buf.writeString(this.getReason());
    }

    public void handle(PacketHandler packetHandler) {
        packetHandler.handle(this);
    }

    public String getReason() {
        return this.cause;
    }
}
