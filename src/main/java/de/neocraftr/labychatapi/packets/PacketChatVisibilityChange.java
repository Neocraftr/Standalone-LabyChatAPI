package de.neocraftr.labychatapi.packets;

import de.neocraftr.labychatapi.handeling.PacketHandler;
import de.neocraftr.labychatapi.PacketBuf;

public class PacketChatVisibilityChange extends Packet {
    private boolean visible;

    public PacketChatVisibilityChange() {}

    public void read(PacketBuf buf) {
        this.visible = buf.readBoolean();
    }

    public void write(PacketBuf buf) {
        buf.writeBoolean(this.visible);
    }

    public void handle(PacketHandler packetHandler) {
        packetHandler.handle(this);
    }

    public boolean isVisible() {
        return this.visible;
    }
}
