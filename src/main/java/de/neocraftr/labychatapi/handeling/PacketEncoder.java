package de.neocraftr.labychatapi.handeling;

import de.neocraftr.labychatapi.LabyChatClient;
import de.neocraftr.labychatapi.packets.LabyChatProtocol;
import de.neocraftr.labychatapi.packets.Packet;
import de.neocraftr.labychatapi.PacketBuf;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class PacketEncoder extends MessageToByteEncoder<Packet> {

    protected void encode(ChannelHandlerContext channelHandlerContext, Packet packet, ByteBuf byteBuf) {
        int id = LabyChatProtocol.getProtocol().getPacketId(packet);
        if (id != 62 && id != 63) {
            LabyChatClient.LOGGER.debug("[OUT] " + id + " " + packet.getClass().getSimpleName());
        }

        PacketBuf buffer = new PacketBuf(byteBuf);
        buffer.writeVarInt(id);
        packet.write(buffer);
    }
}
