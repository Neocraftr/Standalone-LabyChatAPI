package de.neocraftr.labychatapi.packets;

import de.neocraftr.labychatapi.handeling.PacketHandler;
import de.neocraftr.labychatapi.user.ChatUser;
import de.neocraftr.labychatapi.PacketBuf;

public class PacketLoginTime extends Packet {
    private ChatUser player;
    private long dateJoined;
    private long lastOnline;

    public PacketLoginTime() {}

    public PacketLoginTime(ChatUser player, long dateJoined, long lastOnline) {
        this.player = player;
        this.dateJoined = dateJoined;
        this.lastOnline = lastOnline;
    }

    public void read(PacketBuf buf) {
        this.player = buf.readChatUser();
        this.dateJoined = buf.readLong();
        this.lastOnline = buf.readLong();
    }

    public void write(PacketBuf buf) {
        buf.writeChatUser(this.player);
        buf.writeLong(this.dateJoined);
        buf.writeLong(this.lastOnline);
    }

    public void handle(PacketHandler packetHandler) {
        packetHandler.handle(this);
    }

    public long getDateJoined() {
        return this.dateJoined;
    }

    public long getLastOnline() {
        return this.lastOnline;
    }

    public ChatUser getPlayer() {
        return this.player;
    }
}