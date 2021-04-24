package de.neocraftr.labychatapi.packets;

import de.neocraftr.labychatapi.handeling.PacketHandler;
import de.neocraftr.labychatapi.user.ChatRequest;
import de.neocraftr.labychatapi.PacketBuf;

import java.util.ArrayList;
import java.util.List;

public class PacketLoginRequest extends Packet {
    private List<ChatRequest> requesters;

    public PacketLoginRequest() {}

    public PacketLoginRequest(List<ChatRequest> requesters) {
        this.requesters = requesters;
    }

    public List<ChatRequest> getRequests() {
        return this.requesters;
    }

    public void read(PacketBuf buf) {
        this.requesters = new ArrayList();
        int a = buf.readInt();

        for(int i = 0; i < a; ++i) {
            this.requesters.add((ChatRequest)buf.readChatUser());
        }

    }

    public void write(PacketBuf buf) {
        buf.writeInt(this.getRequests().size());

        for(int i = 0; i < this.getRequests().size(); ++i) {
            buf.writeChatUser(this.getRequests().get(i));
        }

    }

    public void handle(PacketHandler packetHandler) {
        packetHandler.handle(this);
    }
}
