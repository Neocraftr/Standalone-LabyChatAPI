package de.neocraftr.labychatapi.packets;

import de.neocraftr.labychatapi.handeling.PacketHandler;
import de.neocraftr.labychatapi.PacketBuf;

public class PacketPlayRequestAddFriend extends Packet {
    private String name;

    public PacketPlayRequestAddFriend() {}

    public PacketPlayRequestAddFriend(String name) {
        this.name = name;
    }

    public void read(PacketBuf buf) {
        byte[] a = new byte[buf.readInt()];

        for(int i = 0; i < a.length; ++i) {
            a[i] = buf.readByte();
        }

        this.name = new String(a);
    }

    public void write(PacketBuf buf) {
        buf.writeInt(this.name.getBytes().length);
        buf.writeBytes(this.name.getBytes());
    }

    public void handle(PacketHandler packetHandler) {
        packetHandler.handle(this);
    }

    public String getName() {
        return this.name;
    }
}
