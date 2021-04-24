package de.neocraftr.labychatapi.handeling;

import de.neocraftr.labychatapi.PacketBuf;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class PacketSplitter extends MessageToByteEncoder<ByteBuf> {

    protected void encode(ChannelHandlerContext ctx, ByteBuf buffer, ByteBuf byteBuf) {
        int bytes = buffer.readableBytes();
        int varIntSize = PacketBuf.getVarIntSize(bytes);
        if (varIntSize > 3) {
            throw new IllegalArgumentException("Unable to fit " + bytes + " into " + 3);
        } else {
            PacketBuf packetBuffer = new PacketBuf(byteBuf);
            packetBuffer.ensureWritable(varIntSize + bytes);
            packetBuffer.writeVarInt(bytes);
            packetBuffer.writeBytes(buffer, buffer.readerIndex(), bytes);
        }
    }
}
