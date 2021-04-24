package de.neocraftr.labychatapi.packets;

import de.neocraftr.labychatapi.handeling.PacketHandler;
import de.neocraftr.labychatapi.user.ChatUser;
import de.neocraftr.labychatapi.PacketBuf;

public class PacketPlayTyping extends Packet {
    private ChatUser player;
    private ChatUser inChatWith;
    private boolean typing;

    public PacketPlayTyping() {}

    public PacketPlayTyping(ChatUser player, ChatUser inChatWith, boolean typing) {
        this.player = player;
        this.inChatWith = inChatWith;
        this.typing = typing;
    }

    public void read(PacketBuf buf) {
        this.player = buf.readChatUser();
        this.inChatWith = buf.readChatUser();
        this.typing = buf.readBoolean();
    }

    public void write(PacketBuf buf) {
        buf.writeChatUser(this.player);
        buf.writeChatUser(this.inChatWith);
        buf.writeBoolean(this.typing);
    }

    public void handle(PacketHandler packetHandler) {
        packetHandler.handle(this);
    }

    public ChatUser getInChatWith() {
        return this.inChatWith;
    }

    public ChatUser getPlayer() {
        return this.player;
    }

    public boolean isTyping() {
        return this.typing;
    }
}
