package de.neocraftr.labychatapi.packets;

import de.neocraftr.labychatapi.handeling.PacketHandler;
import de.neocraftr.labychatapi.PacketBuf;

import java.beans.ConstructorProperties;

public class PacketUpdateCosmetics extends Packet {
    private String json = null;

    public PacketUpdateCosmetics() {}

    public PacketUpdateCosmetics(String json) {
        this.json = json;
    }

    public void read(PacketBuf buf) {
        boolean hasJsonString = buf.readBoolean();
        if (hasJsonString) {
            this.json = buf.readString();
        }

    }

    public void write(PacketBuf buf) {
        buf.writeBoolean(true);
        buf.writeString(this.json);
    }

    public void handle(PacketHandler packetHandler) {
        packetHandler.handle(this);
    }

    public String getJson() {
        return this.json;
    }
}
