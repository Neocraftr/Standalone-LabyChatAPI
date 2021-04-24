package de.neocraftr.labychatapi.packets;

import de.neocraftr.labychatapi.handeling.PacketHandler;
import de.neocraftr.labychatapi.PacketBuf;

import java.util.UUID;

public class PacketActionRequestResponse extends Packet {
    private UUID uuid;
    private short actionId;
    private byte[] data;

    public PacketActionRequestResponse() {}

    public PacketActionRequestResponse(UUID uuid, short actionId, byte[] data) {
        this.uuid = uuid;
        this.actionId = actionId;
        this.data = data;
    }

    public void read(PacketBuf buf) {
        this.uuid = UUID.fromString(buf.readString());
        this.actionId = buf.readShort();
        int length = buf.readVarInt();
        if (length > 1024) {
            throw new RuntimeException("data array too big");
        } else {
            this.data = new byte[length];
            buf.readBytes(this.data);
        }
    }

    public void write(PacketBuf buf) {
        buf.writeString(this.uuid.toString());
        buf.writeShort(this.actionId);
        if (this.data == null) {
            buf.writeVarInt(0);
        } else {
            buf.writeVarInt(this.data.length);
            buf.writeBytes(this.data);
        }

    }

    public void handle(PacketHandler packetHandler) {
        packetHandler.handle(this);
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public short getActionId() {
        return this.actionId;
    }

    public byte[] getData() {
        return this.data;
    }
}
