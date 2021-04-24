package de.neocraftr.labychatapi.packets;


import de.neocraftr.labychatapi.handeling.PacketHandler;
import de.neocraftr.labychatapi.PacketBuf;

public class PacketHelloPing extends Packet {
    private long a;

    public PacketHelloPing() {}

    public PacketHelloPing(long a) {
        this.a = a;
    }
    
    @Override
    public void read(PacketBuf buffer) {
        this.a = buffer.readLong();
        buffer.readInt();
    }
    
    @Override
    public void write(PacketBuf buffer) {
        buffer.writeLong(this.a);
        buffer.writeInt(24);
    }
    
    @Override
    public void handle(final PacketHandler packetHandler) {
        packetHandler.handle(this);
    }
}
