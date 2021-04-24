package de.neocraftr.labychatapi.packets;

import java.util.TimeZone;

import de.neocraftr.labychatapi.handeling.PacketHandler;
import de.neocraftr.labychatapi.packets.PacketLoginOptions.Options;
import de.neocraftr.labychatapi.user.UserStatus;
import de.neocraftr.labychatapi.PacketBuf;

public class PacketPlayChangeOptions extends Packet {
    private Options options;

    public PacketPlayChangeOptions() {}

    public PacketPlayChangeOptions(Options options) {
        this.options = options;
    }

    public PacketPlayChangeOptions(boolean showServer, UserStatus status, TimeZone timeZone) {
        this.options = new Options(showServer, status, timeZone);
    }

    public void read(PacketBuf buf) {
        this.options = new Options(buf.readBoolean(), buf.readUserStatus(), TimeZone.getTimeZone(buf.readString()));
    }

    public void write(PacketBuf buf) {
        buf.writeBoolean(this.getOptions().isShowServer());
        buf.writeUserStatus(this.getOptions().getOnlineStatus());
        buf.writeString(this.getOptions().getTimeZone().getID());
    }

    public void handle(PacketHandler packetHandler) {
        packetHandler.handle(this);
    }

    public Options getOptions() {
        return this.options;
    }
}
