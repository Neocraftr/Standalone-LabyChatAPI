package de.neocraftr.labychatapi.packets;

import de.neocraftr.labychatapi.handeling.PacketHandler;
import de.neocraftr.labychatapi.user.ChatUser;
import de.neocraftr.labychatapi.user.ServerInfo;
import de.neocraftr.labychatapi.PacketBuf;

public class PacketPlayFriendStatus extends Packet {
    private ChatUser player;
    private ServerInfo playerInfo;

    public PacketPlayFriendStatus() {}

    public PacketPlayFriendStatus(ChatUser player, ServerInfo playerInfo) {
        this.player = player;
        this.playerInfo = playerInfo;
    }

    public void read(PacketBuf buf) {
        this.player = buf.readChatUser();
        this.playerInfo = buf.readServerInfo();
    }

    public void write(PacketBuf buf) {
        buf.writeChatUser(this.player);
        buf.writeServerInfo(this.playerInfo);
    }

    public void handle(PacketHandler packetHandler) {
        packetHandler.handle(this);
    }

    public ChatUser getPlayer() {
        return this.player;
    }

    public ServerInfo getPlayerInfo() {
        return this.playerInfo;
    }
}
