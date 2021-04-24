package de.neocraftr.labychatapi.packets;

import de.neocraftr.labychatapi.handeling.PacketHandler;
import de.neocraftr.labychatapi.PacketBuf;
import io.netty.buffer.ByteBuf;

public class PacketLoginStart extends Packet {

    public PacketLoginStart() {}

    public void read(ByteBuf buf) {}

    public void write(ByteBuf buf) {}

    public void handle(PacketHandler packetHandler) {
        packetHandler.handle(this);
    }

    public void read(PacketBuf buf) {
    }

    public void write(PacketBuf buf) {
    }
}