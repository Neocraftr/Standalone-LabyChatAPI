package de.neocraftr.labychatapi.packets;

import de.neocraftr.labychatapi.handeling.PacketHandler;
import de.neocraftr.labychatapi.user.ChatUser;
import de.neocraftr.labychatapi.PacketBuf;

public class PacketPlayFriendPlayingOn extends Packet {
    private ChatUser player;
    private String gameModeName;

    public PacketPlayFriendPlayingOn() {}

    public PacketPlayFriendPlayingOn(ChatUser player, String gameModeName) {
        this.player = player;
        this.gameModeName = gameModeName;
    }

    public void read(PacketBuf buf) {
        this.player = buf.readChatUser();
        this.gameModeName = buf.readString();
    }

    public void write(PacketBuf buf) {
        buf.writeChatUser(this.player);
        buf.writeString(this.gameModeName);
    }

    public void handle(PacketHandler packetHandler) {
        packetHandler.handle(this);
    }

    public String getGameModeName() {
        return this.gameModeName;
    }

    public ChatUser getPlayer() {
        return this.player;
    }
}