package de.neocraftr.labychatapi.packets;

import de.neocraftr.labychatapi.handeling.PacketHandler;
import de.neocraftr.labychatapi.user.ChatUser;
import de.neocraftr.labychatapi.PacketBuf;

public class PacketPlayFriendRemove extends Packet {
    private ChatUser toRemove;

    public PacketPlayFriendRemove() {}

    public PacketPlayFriendRemove(ChatUser toRemove) {
        this.toRemove = toRemove;
    }

    public void read(PacketBuf buf) {
        this.toRemove = buf.readChatUser();
    }

    public void write(PacketBuf buf) {
        buf.writeChatUser(this.toRemove);
    }

    public void handle(PacketHandler packetHandler) {
        packetHandler.handle(this);
    }

    public ChatUser getToRemove() {
        return this.toRemove;
    }
}
