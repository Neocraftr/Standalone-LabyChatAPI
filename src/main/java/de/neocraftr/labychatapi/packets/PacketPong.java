package de.neocraftr.labychatapi.packets;

import de.neocraftr.labychatapi.handeling.PacketHandler;
import de.neocraftr.labychatapi.PacketBuf;

public class PacketPong extends Packet {

    public PacketPong() {}

    public void read(PacketBuf buf) {}

    public void write(PacketBuf buf) {}

    public void handle(PacketHandler packetHandler) {
        packetHandler.handle(this);
    }
}