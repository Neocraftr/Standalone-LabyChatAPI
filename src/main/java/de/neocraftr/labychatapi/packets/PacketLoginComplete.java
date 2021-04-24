package de.neocraftr.labychatapi.packets;

import de.neocraftr.labychatapi.handeling.PacketHandler;
import de.neocraftr.labychatapi.PacketBuf;

public class PacketLoginComplete extends Packet {
    private String showDashboardButton;

    public PacketLoginComplete() {}

    public PacketLoginComplete(String string) {
        this.showDashboardButton = string;
    }

    public void read(PacketBuf buf) {
        this.showDashboardButton = buf.readString();
    }

    public void write(PacketBuf buf) {
        buf.writeString(this.showDashboardButton);
    }

    public void handle(PacketHandler packetHandler) {
        packetHandler.handle(this);
    }

    public String getDashboardPin() {
        return this.showDashboardButton;
    }
}
