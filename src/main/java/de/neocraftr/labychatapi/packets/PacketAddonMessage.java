package de.neocraftr.labychatapi.packets;

import de.neocraftr.labychatapi.handeling.PacketHandler;
import de.neocraftr.labychatapi.PacketBuf;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class PacketAddonMessage extends Packet {
    private String key;
    private byte[] data;

    public PacketAddonMessage() {}

    public PacketAddonMessage(String key, byte[] data) {
        this.key = key;
        this.data = data;
    }

    public PacketAddonMessage(String key, String json) {
        this.key = key;
        this.data = this.toBytes(json);
    }

    public void read(PacketBuf buf) {
        this.key = buf.readString();
        byte[] data = new byte[buf.readInt()];
        buf.readBytes(data);
        this.data = data;
    }

    public void write(PacketBuf buf) {
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

    public String getKey() {
        return this.key;
    }

    public byte[] getData() {
        return this.data;
    }
}
