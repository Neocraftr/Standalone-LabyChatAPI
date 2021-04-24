package de.neocraftr.labychatapi.packets;

import de.neocraftr.labychatapi.handeling.PacketHandler;
import de.neocraftr.labychatapi.PacketBuf;

public class PacketServerMessage extends Packet {
    private String message;

    public PacketServerMessage() {}

    public PacketServerMessage(String message) {
        this.message = message;
    }

    public void read(PacketBuf buf) {
        this.message = buf.readString();
    }

    public void write(PacketBuf buf) {
        buf.writeString(this.message);
    }

    public void handle(PacketHandler packetHandler) {
        packetHandler.handle(this);
    }

    public String getMessage() {
        return this.message;
    }
}
