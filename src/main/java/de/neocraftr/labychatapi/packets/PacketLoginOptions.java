package de.neocraftr.labychatapi.packets;

import de.neocraftr.labychatapi.handeling.PacketHandler;
import de.neocraftr.labychatapi.user.UserStatus;
import de.neocraftr.labychatapi.PacketBuf;

import java.util.TimeZone;

public class PacketLoginOptions extends Packet {
    private boolean showServer;
    private UserStatus status;
    private TimeZone timeZone;

    public PacketLoginOptions() {}

    public PacketLoginOptions(boolean showServer, UserStatus status, TimeZone timeZone) {
        this.showServer = showServer;
        this.status = status;
        this.timeZone = timeZone;
    }

    public void read(PacketBuf buf) {
        this.showServer = buf.readBoolean();
        this.status = buf.readUserStatus();
        this.timeZone = TimeZone.getTimeZone(buf.readString());
    }

    public void write(PacketBuf buf) {
        buf.writeBoolean(this.showServer);
        buf.writeUserStatus(this.status);
        buf.writeString(this.timeZone.getID());
    }

    public void handle(PacketHandler packetHandler) {
        packetHandler.handle(this);
    }

    public PacketLoginOptions.Options getOptions() {
        return new PacketLoginOptions.Options(this.showServer, this.status, this.timeZone);
    }

    public static class Options {
        private final boolean showServer;
        private final UserStatus onlineStatus;
        private final TimeZone timeZone;

        public Options(boolean showServer, UserStatus onlineStatus, TimeZone timeZone) {
            this.showServer = showServer;
            this.timeZone = timeZone;
            this.onlineStatus = onlineStatus;
        }

        public boolean isShowServer() {
            return this.showServer;
        }

        public UserStatus getOnlineStatus() {
            return this.onlineStatus;
        }

        public TimeZone getTimeZone() {
            return this.timeZone;
        }
    }
}

