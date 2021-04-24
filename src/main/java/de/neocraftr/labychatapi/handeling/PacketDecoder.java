package de.neocraftr.labychatapi.handeling;

import de.neocraftr.labychatapi.LabyChatClient;
import de.neocraftr.labychatapi.packets.LabyChatProtocol;
import de.neocraftr.labychatapi.packets.Packet;
import de.neocraftr.labychatapi.PacketBuf;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class PacketDecoder extends ByteToMessageDecoder {

    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> objects) throws Exception {
        PacketBuf buffer = new PacketBuf(byteBuf);
        if (buffer.readableBytes() >= 1) {
            int id = buffer.readVarInt();
            Packet packet = LabyChatProtocol.getProtocol().getPacket(id);
            if (id != 62 && id != 63) {
                LabyChatClient.LOGGER.debug("[IN] " + id + " " + packet.getClass().getSimpleName());
            }

            packet.read(buffer);
            if (buffer.readableBytes() > 0) {
                throw new RuntimeException("Packet  (" + packet.getClass().getSimpleName() + ") was larger than I expected, found " + buffer.readableBytes() + " bytes extra whilst reading packet " + packet);
            } else {
                objects.add(packet);
            }
        }
    }
}

