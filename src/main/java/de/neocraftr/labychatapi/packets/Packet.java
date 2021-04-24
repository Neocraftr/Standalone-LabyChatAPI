package de.neocraftr.labychatapi.packets;

import de.neocraftr.labychatapi.handeling.PacketHandler;
import de.neocraftr.labychatapi.PacketBuf;

public abstract class Packet {
    public abstract void read(PacketBuf buffer);
    
    public abstract void write(PacketBuf buffer);
    
    public abstract void handle(PacketHandler packetHandler);
}
