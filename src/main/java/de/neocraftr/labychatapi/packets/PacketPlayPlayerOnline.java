package de.neocraftr.labychatapi.packets;

import de.neocraftr.labychatapi.handeling.PacketHandler;
import de.neocraftr.labychatapi.user.ChatUser;
import de.neocraftr.labychatapi.PacketBuf;

public class PacketPlayPlayerOnline extends Packet {
    private ChatUser newOnlinePlayer;

    public PacketPlayPlayerOnline() {}

    public PacketPlayPlayerOnline(ChatUser newOnlinePlayer) {
        this.newOnlinePlayer = newOnlinePlayer;
    }

    public void read(PacketBuf buf) {
        this.newOnlinePlayer = buf.readChatUser();
    }

    public void write(PacketBuf buf) {
        buf.writeChatUser(this.newOnlinePlayer);
    }

    public void handle(PacketHandler packetHandler) {
        packetHandler.handle(this);
    }

    public ChatUser getPlayer() {
        return this.newOnlinePlayer;
    }
}
