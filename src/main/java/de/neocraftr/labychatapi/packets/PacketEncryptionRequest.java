package de.neocraftr.labychatapi.packets;

import de.neocraftr.labychatapi.handeling.PacketHandler;
import de.neocraftr.labychatapi.PacketBuf;

public class PacketEncryptionRequest extends Packet {
    private String serverId;
    private byte[] publicKey;
    private byte[] verifyToken;

    public PacketEncryptionRequest() {}

    public PacketEncryptionRequest(String serverId, byte[] publicKey, byte[] verifyToken) {
        this.serverId = serverId;
        this.publicKey = publicKey;
        this.verifyToken = verifyToken;
    }

    public byte[] getPublicKey() {
        return this.publicKey;
    }

    public String getServerId() {
        return this.serverId;
    }

    public byte[] getVerifyToken() {
        return this.verifyToken;
    }

    public void read(PacketBuf buf) {
        this.serverId = buf.readString();
        this.publicKey = buf.readByteArray();
        this.verifyToken = buf.readByteArray();
    }

    public void write(PacketBuf buf) {
        buf.writeString(this.serverId);
        buf.writeByteArray(this.publicKey);
        buf.writeByteArray(this.verifyToken);
    }

    public void handle(PacketHandler packetHandler) {
        packetHandler.handle(this);
    }
}
