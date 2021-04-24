package de.neocraftr.labychatapi.packets;

import de.neocraftr.labychatapi.handeling.PacketHandler;
import de.neocraftr.labychatapi.user.ChatUser;
import de.neocraftr.labychatapi.PacketBuf;

import java.util.ArrayList;
import java.util.List;

public class PacketLoginFriend extends Packet {
    private List<ChatUser> friends;

    public PacketLoginFriend(List<ChatUser> friends) {
        this.friends = friends;
    }

    public PacketLoginFriend() {}

    public void read(PacketBuf buf) {
        List<ChatUser> players = new ArrayList();
        int a = buf.readInt();

        for(int i = 0; i < a; ++i) {
            players.add(buf.readChatUser());
        }

        this.friends = new ArrayList();
        this.friends.addAll(players);
    }

    public void write(PacketBuf buf) {
        buf.writeInt(this.getFriends().size());

        for(int i = 0; i < this.getFriends().size(); ++i) {
            ChatUser p = this.getFriends().get(i);
            buf.writeChatUser(p);
        }

    }

    public void handle(PacketHandler packetHandler) {
        packetHandler.handle(this);
    }

    public List<ChatUser> getFriends() {
        return this.friends;
    }
}