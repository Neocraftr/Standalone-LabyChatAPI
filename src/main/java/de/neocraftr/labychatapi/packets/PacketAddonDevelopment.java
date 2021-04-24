package de.neocraftr.labychatapi.packets;

import de.neocraftr.labychatapi.handeling.PacketHandler;
import de.neocraftr.labychatapi.utils.GZIPCompression;
import de.neocraftr.labychatapi.PacketBuf;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class PacketAddonDevelopment extends Packet {
    private UUID sender;
    private UUID[] receivers;
    private String key;
    private byte[] data;

    public PacketAddonDevelopment() {}

    public PacketAddonDevelopment(UUID sender, String key, byte[] data) {
        this.sender = sender;
        this.key = key;
        this.data = GZIPCompression.compress(data);
        this.receivers = new UUID[0];
    }

    public PacketAddonDevelopment(UUID sender, String key, String json) {
        this.sender = sender;
        this.key = key;
        this.data = this.toBytes(json);
        this.receivers = new UUID[0];
    }

    public PacketAddonDevelopment(UUID sender, UUID[] receivers, String key, byte[] data) {
        this.sender = sender;
        this.receivers = receivers;
        this.key = key;
        this.data = GZIPCompression.compress(data);
    }

    public PacketAddonDevelopment(UUID sender, UUID[] receivers, String key, String json) {
        this.sender = sender;
        this.receivers = receivers;
        this.key = key;
        this.data = this.toBytes(json);
    }

    public void read(PacketBuf buf) {
        this.sender = new UUID(buf.readLong(), buf.readLong());
        int receiverCnt = buf.readShort();
        this.receivers = new UUID[receiverCnt];

        for(int i = 0; i < this.receivers.length; ++i) {
            this.receivers[i] = new UUID(buf.readLong(), buf.readLong());
        }

        this.key = buf.readString();
        byte[] data = new byte[buf.readInt()];
        buf.readBytes(data);
        this.data = data;
    }

    public void write(PacketBuf buf) {
        buf.writeLong(this.sender.getMostSignificantBits());
        buf.writeLong(this.sender.getLeastSignificantBits());
        buf.writeShort(this.receivers.length);
        UUID[] uuids = this.receivers;

        for (UUID receiver : uuids) {
            buf.writeLong(receiver.getMostSignificantBits());
            buf.writeLong(receiver.getLeastSignificantBits());
        }

        buf.writeString(this.key);
        buf.writeInt(this.data.length);
        buf.writeBytes(this.data);
    }

    public void handle(PacketHandler packetHandler) {
        packetHandler.handle(this);
    }

    public String getJson() {
        try {
            StringBuilder outStr = new StringBuilder();
            if (this.data != null && this.data.length != 0) {
                if (this.isCompressed(this.data)) {
                    GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(this.data));
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(gis, StandardCharsets.UTF_8));

                    String line;
                    while((line = bufferedReader.readLine()) != null) {
                        outStr.append(line);
                    }
                } else {
                    outStr.append(Arrays.toString(this.data));
                }

                return outStr.toString();
            } else {
                return "";
            }
        } catch (IOException var5) {
            throw new RuntimeException(var5);
        }
    }

    private byte[] toBytes(String in) {
        byte[] str = in.getBytes(StandardCharsets.UTF_8);

        try {
            if (str.length != 0) {
                ByteArrayOutputStream obj = new ByteArrayOutputStream();
                GZIPOutputStream gzip = new GZIPOutputStream(obj);
                gzip.write(str);
                gzip.flush();
                gzip.close();
                return obj.toByteArray();
            } else {
                return new byte[0];
            }
        } catch (IOException err) {
            throw new RuntimeException(err);
        }
    }

    private boolean isCompressed(byte[] compressed) {
        return compressed[0] == 31 && compressed[1] == -117;
    }

    public byte[] getData() {
        return GZIPCompression.decompress(this.data);
    }

    public UUID getSender() {
        return this.sender;
    }

    public UUID[] getReceivers() {
        return this.receivers;
    }

    public String getKey() {
        return this.key;
    }
}