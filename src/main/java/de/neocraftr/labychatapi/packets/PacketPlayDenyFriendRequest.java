package de.neocraftr.labychatapi.packets;

import de.neocraftr.labychatapi.handeling.PacketHandler;
import de.neocraftr.labychatapi.user.ChatRequest;
import de.neocraftr.labychatapi.PacketBuf;

public class PacketPlayDenyFriendRequest extends Packet {
    private ChatRequest denied;

    public PacketPlayDenyFriendRequest() {}

    public PacketPlayDenyFriendRequest(ChatRequest denied) {
        this.denied = denied;
    }

    public void read(PacketBuf buf) {
        this.denied = (ChatRequest)buf.readChatUser();
    }

    public void write(PacketBuf buf) {
        buf.writeChatUser(this.denied);
    }

    public void handle(PacketHandler packetHandler) {
        packetHandler.handle(this);
    }

    public ChatRequest getDenied() {
        return this.denied;
    }
}
