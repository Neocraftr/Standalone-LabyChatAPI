package de.neocraftr.labychatapi.packets;

import de.neocraftr.labychatapi.handeling.PacketHandler;
import de.neocraftr.labychatapi.user.ChatUser;
import de.neocraftr.labychatapi.PacketBuf;

public class PacketMessage extends Packet {
    private ChatUser sender;
    private ChatUser to;
    private String message;
    private long sentTime;
    private long fileSize;
    private double audioTime;

    public PacketMessage() {}

    public PacketMessage(ChatUser sender, ChatUser to, String message, long fileSize, double time, long sentTime) {
        this.sender = sender;
        this.to = to;
        this.message = message;
        this.fileSize = fileSize;
        this.audioTime = time;
        this.sentTime = sentTime;
    }

    public void read(PacketBuf buf) {
        this.sender = buf.readChatUser();
        this.to = buf.readChatUser();
        this.message = buf.readString();
        this.fileSize = buf.readLong();
        this.audioTime = buf.readDouble();
        this.sentTime = buf.readLong();
    }

    public void write(PacketBuf buf) {
        buf.writeChatUser(this.sender);
        buf.writeChatUser(this.to);
        buf.writeString(this.message);
        buf.writeLong(this.fileSize);
        buf.writeDouble(this.audioTime);
        buf.writeLong(this.sentTime);
    }

    public void handle(PacketHandler packetHandler) {
        packetHandler.handle(this);
    }

    public double getAudioTime() {
        return this.audioTime;
    }

    public long getFileSize() {
        return this.fileSize;
    }

    public String getMessage() {
        return this.message;
    }

    public ChatUser getSender() {
        return this.sender;
    }

    public ChatUser getTo() {
        return this.to;
    }

    public long getSentTime() {
        return this.sentTime;
    }
}
