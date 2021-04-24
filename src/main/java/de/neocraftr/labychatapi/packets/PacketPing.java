package de.neocraftr.labychatapi.packets;

import de.neocraftr.labychatapi.handeling.PacketHandler;
import de.neocraftr.labychatapi.PacketBuf;

public class PacketPing extends Packet {

    public PacketPing() {}

    @Override
    public void read(PacketBuf buffer) {}

    @Override
    public void write(PacketBuf buffer) {}

    @Override
    public void handle(PacketHandler packetHandler) { packetHandler.handle(this); }
}
