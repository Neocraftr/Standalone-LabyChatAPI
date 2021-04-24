package de.neocraftr.labychatapi.packets;

import de.neocraftr.labychatapi.handeling.PacketHandler;
import de.neocraftr.labychatapi.PacketBuf;

import java.util.UUID;

public class PacketActionRequest extends Packet {
    private UUID uuid;

    public PacketActionRequest() {}

    public PacketActionRequest(UUID uuid) {
        this.uuid = uuid;
    }

    public void read(PacketBuf buf) {
        this.uuid = UUID.fromString(buf.readString());
    }

    public void write(PacketBuf buf) {
        buf.writeString(this.uuid.toString());
    }

    public void handle(PacketHandler packetHandler) {
        packetHandler.handle(this);
    }

    public UUID getUuid() {
        return this.uuid;
    }
}
