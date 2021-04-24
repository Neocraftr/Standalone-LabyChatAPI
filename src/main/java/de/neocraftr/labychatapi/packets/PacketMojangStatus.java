package de.neocraftr.labychatapi.packets;

import de.neocraftr.labychatapi.handeling.PacketHandler;
import de.neocraftr.labychatapi.PacketBuf;

public class PacketMojangStatus extends Packet {

    public PacketMojangStatus() {}

    public void read(PacketBuf buf) {
        buf.readInt();
        buf.readString();
    }

    public void write(PacketBuf buf) {
    }

    public void handle(PacketHandler packetHandler) {
        packetHandler.handle(this);
    }
}
