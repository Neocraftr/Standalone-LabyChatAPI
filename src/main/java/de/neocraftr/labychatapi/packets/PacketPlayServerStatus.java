package de.neocraftr.labychatapi.packets;

import com.google.common.base.Objects;
import de.neocraftr.labychatapi.handeling.PacketHandler;
import de.neocraftr.labychatapi.user.ServerInfo;
import de.neocraftr.labychatapi.PacketBuf;

public class PacketPlayServerStatus extends Packet {
    private String serverIp = "";
    private int port = 25565;
    private String gamemode = null;

    public PacketPlayServerStatus() {}

    public PacketPlayServerStatus(String serverIp, int port) {
        this.serverIp = serverIp;
        this.port = port;
        this.gamemode = null;
    }

    public PacketPlayServerStatus(String serverIp, int port, String gamemode) {
        this.serverIp = serverIp;
        this.port = port;
        this.gamemode = gamemode;
    }

    public void read(PacketBuf buf) {
        this.serverIp = buf.readString();
        this.port = buf.readInt();
        if (buf.readBoolean()) {
            this.gamemode = buf.readString();
        }

    }

    public void write(PacketBuf buf) {
        buf.writeString(this.serverIp);
        buf.writeInt(this.port);
        if (this.gamemode != null && !this.gamemode.isEmpty()) {
            buf.writeBoolean(true);
            buf.writeString(this.gamemode);
        } else {
            buf.writeBoolean(false);
        }

    }

    public void handle(PacketHandler packetHandler) {
        packetHandler.handle(this);
    }

    public String getServerIp() {
        return this.serverIp;
    }

    public int getPort() {
        return this.port;
    }

    public String getGamemode() {
        return this.gamemode;
    }

    public ServerInfo build() {
        return this.gamemode == null ? new ServerInfo(this.serverIp, this.port) : new ServerInfo(this.serverIp, this.port, this.gamemode);
    }

    public boolean equals(PacketPlayServerStatus packet) {
        return this.serverIp.equals(packet.getServerIp()) && this.port == packet.getPort() && Objects.equal(this.gamemode, packet.getGamemode());
    }
}
