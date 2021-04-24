package de.neocraftr.labychatapi.packets;


import de.neocraftr.labychatapi.handeling.PacketHandler;
import de.neocraftr.labychatapi.PacketBuf;

public class PacketHelloPong extends Packet {
    private long a;

    public PacketHelloPong() {}

    public PacketHelloPong(long a) {
        this.a = a;
    }
    
    @Override
    public void read(PacketBuf buffer) {
        this.a = buffer.readLong();
    }
    
    @Override
    public void write(PacketBuf buffer) {
        buffer.writeLong(this.a);
    }

    @Override
    public void handle(final PacketHandler packetHandler) {
        packetHandler.handle(this);
    }
}
