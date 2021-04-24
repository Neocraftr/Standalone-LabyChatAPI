package de.neocraftr.labychatapi.packets;

import de.neocraftr.labychatapi.handeling.PacketHandler;
import de.neocraftr.labychatapi.PacketBuf;

import java.util.UUID;

public class PacketUserBadge extends Packet {
    private UUID[] uuids;
    private byte[] ranks;

    public PacketUserBadge() {}

    public PacketUserBadge(UUID[] uuids) {
        this.uuids = uuids;
    }

    public void read(PacketBuf buf) {
        int size = buf.readVarInt();
        this.uuids = new UUID[size];

        for(int i = 0; i < size; ++i) {
            this.uuids[i] = new UUID(buf.readLong(), buf.readLong());
        }

        byte[] bytes = new byte[size];
        buf.readBytes(bytes);
        this.ranks = bytes;
    }

    public void write(PacketBuf buf) {
        buf.writeVarInt(this.uuids.length);

        for (UUID uuid : this.uuids) {
            buf.writeLong(uuid.getMostSignificantBits());
            buf.writeLong(uuid.getLeastSignificantBits());
        }

    }

    public void handle(PacketHandler packetHandler) {
        packetHandler.handle(this);
    }

    public UUID[] getUuids() {
        return this.uuids;
    }

    public byte[] getRanks() {
        return this.ranks;
    }
}

