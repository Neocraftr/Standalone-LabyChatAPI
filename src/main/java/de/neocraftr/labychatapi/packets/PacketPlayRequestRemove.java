package de.neocraftr.labychatapi.packets;

import de.neocraftr.labychatapi.handeling.PacketHandler;
import de.neocraftr.labychatapi.PacketBuf;

public class PacketPlayRequestRemove extends Packet {
    private String playerName;

    public PacketPlayRequestRemove() {}

    public PacketPlayRequestRemove(String playerName) {
        this.playerName = playerName;
    }

    public void read(PacketBuf buf) {
        this.playerName = buf.readString();
    }

    public void write(PacketBuf buf) {
        buf.writeString(this.playerName);
    }

    public void handle(PacketHandler packetHandler) {
        packetHandler.handle(this);
    }

    public String getPlayerName() {
        return this.playerName;
    }
}

