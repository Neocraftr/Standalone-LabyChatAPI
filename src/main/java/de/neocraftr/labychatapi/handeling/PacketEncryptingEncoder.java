package de.neocraftr.labychatapi.handeling;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import javax.crypto.Cipher;

public class PacketEncryptingEncoder extends MessageToByteEncoder<ByteBuf> {
    private final EncryptionTranslator encryptionCodec;

    public PacketEncryptingEncoder(Cipher cipher) {
        this.encryptionCodec = new EncryptionTranslator(cipher);
    }

    protected void encode(ChannelHandlerContext context, ByteBuf byteBuf, ByteBuf secondByteBuf) throws Exception {
        this.encryptionCodec.cipher(byteBuf, secondByteBuf);
    }
}
