package de.neocraftr.labychatapi.packets;

import de.neocraftr.labychatapi.handeling.PacketHandler;
import de.neocraftr.labychatapi.PacketBuf;

import java.beans.ConstructorProperties;

public class PacketActionPlay extends Packet {
    private short requestId;
    private short actionType;
    private byte[] data;

    public PacketActionPlay() {}

    public void read(PacketBuf buf) {
        this.requestId = buf.readShort();
        this.actionType = buf.readShort();
        int length = buf.readVarInt();
        if (length > 1024) {
            throw new RuntimeException("data array too big");
        } else {
            this.data = new byte[length];
            buf.readBytes(this.data);
        }
    }

    public void write(PacketBuf buf) {
        buf.writeShort(this.requestId);
        buf.writeShort(this.actionType);
        if (this.data == null) {
            buf.writeVarInt(0);
        } else {
            buf.writeVarInt(this.data.length);
            buf.writeBytes(this.data);
        }
    }

    public void handle(PacketHandler packetHandler) {}

    public short getRequestId() {
        return this.requestId;
    }

    public short getActionType() {
        return this.actionType;
    }

    public byte[] getData() {
        return this.data;
    }

    @ConstructorProperties({"requestId", "actionType", "data"})
    public PacketActionPlay(short requestId, short actionType, byte[] data) {
        this.requestId = requestId;
        this.actionType = actionType;
        this.data = data;
    }
}
