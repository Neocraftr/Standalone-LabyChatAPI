package de.neocraftr.labychatapi.handeling;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import javax.crypto.Cipher;
import java.util.List;

public class PacketEncryptingDecoder extends MessageToMessageDecoder<ByteBuf> {
    private final EncryptionTranslator decryptionCodec;

    public PacketEncryptingDecoder(Cipher cipher) {
        this.decryptionCodec = new EncryptionTranslator(cipher);
    }

    protected void decode(ChannelHandlerContext context, ByteBuf byteBuf, List<Object> list) throws Exception {
        list.add(this.decryptionCodec.decipher(context, byteBuf));
    }
}
