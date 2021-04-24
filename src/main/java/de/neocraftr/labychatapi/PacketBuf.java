package de.neocraftr.labychatapi;

import com.google.common.base.Charsets;
import com.mojang.authlib.GameProfile;
import de.neocraftr.labychatapi.user.ChatRequest;
import de.neocraftr.labychatapi.user.ChatUser;
import de.neocraftr.labychatapi.user.ServerInfo;
import de.neocraftr.labychatapi.user.UserStatus;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.util.ByteProcessor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.nio.charset.Charset;
import java.util.UUID;

public class PacketBuf extends ByteBuf {
    private ByteBuf buf;

    public PacketBuf(ByteBuf buf) {
        this.buf = buf;
    }

    public static int getVarIntSize(int input) {
        for(int i = 1; i < 5; ++i) {
            if ((input & -1 << i * 7) == 0) {
                return i;
            }
        }

        return 5;
    }

    public int readVarInt() {
        int i = 0;
        int j = 0;

        byte b0;
        do {
            b0 = this.readByte();
            i |= (b0 & 127) << j++ * 7;
            if (j > 5) {
                throw new RuntimeException("VarInt too big");
            }
        } while((b0 & 128) == 128);

        return i;
    }

    public void writeVarInt(int input) {
        while((input & -128) != 0) {
            this.writeByte(input & 127 | 128);
            input >>>= 7;
        }

        this.writeByte(input);
    }

    public void writeString(String string) {
        this.writeInt(string.getBytes(Charsets.UTF_8).length);
        this.writeBytes(string.getBytes(Charsets.UTF_8));
    }

    public String readString() {
        byte[] a = new byte[this.readInt()];

        for(int i = 0; i < a.length; ++i) {
            a[i] = this.readByte();
        }

        return new String(a, Charsets.UTF_8);
    }

    public PacketBuf writeUserStatus(UserStatus status) {
        this.writeByte(status.getId());
        return this;
    }

    public UserStatus readUserStatus() {
        return UserStatus.getById(this.readByte());
    }

    public void writeByteArray(byte[] data) {
        this.writeInt(data.length);
        this.writeBytes(data);
    }

    public byte[] readByteArray() {
        byte[] b = new byte[this.readInt()];

        for(int i = 0; i < b.length; ++i) {
            b[i] = this.readByte();
        }

        return b;
    }

    public ChatUser readChatUser() {
        String username = this.readString();
        UUID uuid = this.readUUID();
        String statusMessage = this.readString();
        UserStatus status = this.readUserStatus();
        boolean request = this.readBoolean();
        String timeZone = this.readString();
        int contactsAmount = this.readInt();
        long lastOnline = this.readLong();
        long firstJoined = this.readLong();
        ServerInfo serverInfo = this.readServerInfo();
        return request ? new ChatRequest(new GameProfile(uuid, username)) : new ChatUser(new GameProfile(uuid, username), status, statusMessage, serverInfo, timeZone, lastOnline, firstJoined, contactsAmount);
    }

    public void writeChatUser(ChatUser player) {
        this.writeString(player.getGameProfile().getName());
        this.writeUUID(player.getGameProfile().getId());
        this.writeString(player.getStatusMessage());
        this.writeUserStatus(player.getStatus());
        this.writeBoolean(player.isFriendRequest());
        this.writeString(player.getTimeZone());
        this.writeInt(player.getContactAmount());
        this.writeLong(player.getLastOnline());
        this.writeLong(player.getFirstJoined());
        this.writeServerInfo(player.getCurrentServerInfo());
    }

    public ServerInfo readServerInfo() {
        String serverIp = this.readString();
        int serverPort = this.readInt();
        return this.readBoolean() ? new ServerInfo(serverIp, serverPort, this.readString()) : new ServerInfo(serverIp, serverPort);
    }

    public PacketBuf writeServerInfo(ServerInfo info) {
        if (info == null) {
            info = new ServerInfo("", 0);
        }

        this.writeString(info.getServerIp() == null ? "" : info.getServerIp());
        this.writeInt(info.getServerPort());
        if (info.getSpecifiedServerName() != null) {
            this.writeBoolean(true);
            this.writeString(info.getSpecifiedServerName());
        } else {
            this.writeBoolean(false);
        }

        return this;
    }

    public void writeUUID(UUID uuid) {
        this.writeString(uuid.toString());
    }

    public UUID readUUID() {
        return UUID.fromString(this.readString());
    }

    @Override
    public int capacity() {
        return this.buf.capacity();
    }

    @Override
    public ByteBuf capacity(int i) {
        return this.buf.capacity(i);
    }

    @Override
    public int maxCapacity() {
        return this.buf.maxCapacity();
    }

    @Override
    public ByteBufAllocator alloc() {
        return this.buf.alloc();
    }

    @Override
    public ByteOrder order() {
        return this.buf.order();
    }

    @Override
    public ByteBuf order(ByteOrder byteOrder) {
        return this.buf.order(byteOrder);
    }

    @Override
    public ByteBuf unwrap() {
        return this.buf.unwrap();
    }

    @Override
    public boolean isDirect() {
        return this.buf.isDirect();
    }

    @Override
    public boolean isReadOnly() {
        return this.buf.isReadOnly();
    }

    @Override
    public ByteBuf asReadOnly() {
        return this.buf.asReadOnly();
    }

    @Override
    public int readerIndex() {
        return this.buf.readerIndex();
    }

    @Override
    public ByteBuf readerIndex(int i) {
        return this.buf.readerIndex(i);
    }

    @Override
    public int writerIndex() {
        return this.buf.writerIndex();
    }

    @Override
    public ByteBuf writerIndex(int i) {
        return this.buf.writerIndex(i);
    }

    @Override
    public ByteBuf setIndex(int i, int i1) {
        return this.buf.setIndex(i, i1);
    }

    @Override
    public int readableBytes() {
        return this.buf.readableBytes();
    }

    @Override
    public int writableBytes() {
        return this.buf.writableBytes();
    }

    @Override
    public int maxWritableBytes() {
        return this.buf.maxWritableBytes();
    }

    @Override
    public boolean isReadable() {
        return this.buf.isReadable();
    }

    @Override
    public boolean isReadable(int i) {
        return this.buf.isReadable(i);
    }

    @Override
    public boolean isWritable() {
        return this.buf.isWritable();
    }

    @Override
    public boolean isWritable(int i) {
        return this.buf.isWritable(i);
    }

    @Override
    public ByteBuf clear() {
        return this.buf.clear();
    }

    @Override
    public ByteBuf markReaderIndex() {
        return this.buf.markReaderIndex();
    }

    @Override
    public ByteBuf resetReaderIndex() {
        return this.buf.resetReaderIndex();
    }

    @Override
    public ByteBuf markWriterIndex() {
        return this.buf.markWriterIndex();
    }

    @Override
    public ByteBuf resetWriterIndex() {
        return this.buf.resetWriterIndex();
    }

    @Override
    public ByteBuf discardReadBytes() {
        return this.buf.discardReadBytes();
    }

    @Override
    public ByteBuf discardSomeReadBytes() {
        return this.buf.discardSomeReadBytes();
    }

    @Override
    public ByteBuf ensureWritable(int i) {
        return this.buf.ensureWritable(i);
    }

    @Override
    public int ensureWritable(int i, boolean b) {
        return this.buf.ensureWritable(i, b);
    }

    @Override
    public boolean getBoolean(int i) {
        return this.buf.getBoolean(i);
    }

    @Override
    public byte getByte(int i) {
        return this.buf.getByte(i);
    }

    @Override
    public short getUnsignedByte(int i) {
        return this.buf.getUnsignedByte(i);
    }

    @Override
    public short getShort(int i) {
        return this.buf.getShort(i);
    }

    @Override
    public short getShortLE(int i) {
        return this.buf.getShortLE(i);
    }

    @Override
    public int getUnsignedShort(int i) {
        return this.buf.getUnsignedShort(i);
    }

    @Override
    public int getUnsignedShortLE(int i) {
        return this.buf.getUnsignedShortLE(i);
    }

    @Override
    public int getMedium(int i) {
        return this.buf.getMedium(i);
    }

    @Override
    public int getMediumLE(int i) {
        return this.buf.getMediumLE(i);
    }

    @Override
    public int getUnsignedMedium(int i) {
        return this.buf.getUnsignedMedium(i);
    }

    @Override
    public int getUnsignedMediumLE(int i) {
        return this.buf.getUnsignedMediumLE(i);
    }

    @Override
    public int getInt(int i) {
        return this.buf.getInt(i);
    }

    @Override
    public int getIntLE(int i) {
        return this.buf.getIntLE(i);
    }

    @Override
    public long getUnsignedInt(int i) {
        return this.buf.getUnsignedInt(i);
    }

    @Override
    public long getUnsignedIntLE(int i) {
        return this.buf.getUnsignedIntLE(i);
    }

    @Override
    public long getLong(int i) {
        return this.buf.getLong(i);
    }

    @Override
    public long getLongLE(int i) {
        return this.buf.getLongLE(i);
    }

    @Override
    public char getChar(int i) {
        return this.buf.getChar(i);
    }

    @Override
    public float getFloat(int i) {
        return this.buf.getFloat(i);
    }

    @Override
    public double getDouble(int i) {
        return this.buf.getDouble(i);
    }

    @Override
    public ByteBuf getBytes(int i, ByteBuf byteBuf) {
        return this.buf.getBytes(i, byteBuf);
    }

    @Override
    public ByteBuf getBytes(int i, ByteBuf byteBuf, int i1) {
        return this.buf.getBytes(i, byteBuf, i1);
    }

    @Override
    public ByteBuf getBytes(int i, ByteBuf byteBuf, int i1, int i2) {
        return this.buf.getBytes(i, byteBuf, i1, i2);
    }

    @Override
    public ByteBuf getBytes(int i, byte[] bytes) {
        return this.buf.getBytes(i, bytes);
    }

    @Override
    public ByteBuf getBytes(int i, byte[] bytes, int i1, int i2) {
        return this.buf.getBytes(i, bytes, i1, i2);
    }

    @Override
    public ByteBuf getBytes(int i, ByteBuffer byteBuffer) {
        return this.buf.getBytes(i, byteBuffer);
    }

    @Override
    public ByteBuf getBytes(int i, OutputStream outputStream, int i1) throws IOException {
        return this.buf.getBytes(i, outputStream, i1);
    }

    @Override
    public int getBytes(int i, GatheringByteChannel gatheringByteChannel, int i1) throws IOException {
        return this.buf.getBytes(i, gatheringByteChannel, i1);
    }

    @Override
    public int getBytes(int i, FileChannel fileChannel, long l, int i1) throws IOException {
        return this.buf.getBytes(i, fileChannel, l, i1);
    }

    @Override
    public CharSequence getCharSequence(int i, int i1, Charset charset) {
        return this.buf.getCharSequence(i, i1, charset);
    }

    @Override
    public ByteBuf setBoolean(int i, boolean b) {
        return this.buf.setBoolean(i, b);
    }

    @Override
    public ByteBuf setByte(int i, int i1) {
        return this.buf.setByte(1, i1);
    }

    @Override
    public ByteBuf setShort(int i, int i1) {
        return this.buf.setShort(i, i1);
    }

    @Override
    public ByteBuf setShortLE(int i, int i1) {
        return this.buf.setShortLE(i, i1);
    }

    @Override
    public ByteBuf setMedium(int i, int i1) {
        return this.buf.setMedium(i, i1);
    }

    @Override
    public ByteBuf setMediumLE(int i, int i1) {
        return this.buf.setMediumLE(i, i1);
    }

    @Override
    public ByteBuf setInt(int i, int i1) {
        return this.buf.setInt(i, i1);
    }

    @Override
    public ByteBuf setIntLE(int i, int i1) {
        return this.buf.setIntLE(i, i1);
    }

    @Override
    public ByteBuf setLong(int i, long l) {
        return this.buf.setLong(i, l);
    }

    @Override
    public ByteBuf setLongLE(int i, long l) {
        return this.buf.setLongLE(i, l);
    }

    @Override
    public ByteBuf setChar(int i, int i1) {
        return this.buf.setChar(i, i1);
    }

    @Override
    public ByteBuf setFloat(int i, float v) {
        return this.buf.setFloat(i, v);
    }

    @Override
    public ByteBuf setDouble(int i, double v) {
        return this.buf.setDouble(i, v);
    }

    @Override
    public ByteBuf setBytes(int i, ByteBuf byteBuf) {
        return this.buf.setBytes(i, byteBuf);
    }

    @Override
    public ByteBuf setBytes(int i, ByteBuf byteBuf, int i1) {
        return this.buf.setBytes(i, byteBuf, i1);
    }

    @Override
    public ByteBuf setBytes(int i, ByteBuf byteBuf, int i1, int i2) {
        return this.buf.setBytes(i, byteBuf, i1, i2);
    }

    @Override
    public ByteBuf setBytes(int i, byte[] bytes) {
        return this.buf.setBytes(i, bytes);
    }

    @Override
    public ByteBuf setBytes(int i, byte[] bytes, int i1, int i2) {
        return this.buf.setBytes(i, bytes, i1, i2);
    }

    @Override
    public ByteBuf setBytes(int i, ByteBuffer byteBuffer) {
        return this.buf.setBytes(i, byteBuffer);
    }

    @Override
    public int setBytes(int i, InputStream inputStream, int i1) throws IOException {
        return this.buf.setBytes(i, inputStream, i1);
    }

    @Override
    public int setBytes(int i, ScatteringByteChannel scatteringByteChannel, int i1) throws IOException {
        return this.buf.setBytes(i, scatteringByteChannel, i1);
    }

    @Override
    public int setBytes(int i, FileChannel fileChannel, long l, int i1) throws IOException {
        return this.buf.setBytes(i, fileChannel, l, i1);
    }

    @Override
    public ByteBuf setZero(int i, int i1) {
        return this.buf.setZero(i, i1);
    }

    @Override
    public int setCharSequence(int i, CharSequence charSequence, Charset charset) {
        return this.buf.setCharSequence(i, charSequence, charset);
    }

    @Override
    public boolean readBoolean() {
        return this.buf.readBoolean();
    }

    @Override
    public byte readByte() {
        return this.buf.readByte();
    }

    @Override
    public short readUnsignedByte() {
        return this.buf.readUnsignedByte();
    }

    @Override
    public short readShort() {
        return this.buf.readShort();
    }

    @Override
    public short readShortLE() {
        return this.buf.readShortLE();
    }

    @Override
    public int readUnsignedShort() {
        return this.buf.readUnsignedShort();
    }

    @Override
    public int readUnsignedShortLE() {
        return this.buf.readUnsignedShortLE();
    }

    @Override
    public int readMedium() {
        return this.buf.readMedium();
    }

    @Override
    public int readMediumLE() {
        return this.buf.readMediumLE();
    }

    @Override
    public int readUnsignedMedium() {
        return this.buf.readUnsignedMedium();
    }

    @Override
    public int readUnsignedMediumLE() {
        return this.buf.readUnsignedMediumLE();
    }

    @Override
    public int readInt() {
        return this.buf.readInt();
    }

    @Override
    public int readIntLE() {
        return this.buf.readIntLE();
    }

    @Override
    public long readUnsignedInt() {
        return this.buf.readUnsignedInt();
    }

    @Override
    public long readUnsignedIntLE() {
        return this.buf.readUnsignedIntLE();
    }

    @Override
    public long readLong() {
        return this.buf.readLong();
    }

    @Override
    public long readLongLE() {
        return this.buf.readLongLE();
    }

    @Override
    public char readChar() {
        return this.buf.readChar();
    }

    @Override
    public float readFloat() {
        return this.buf.readFloat();
    }

    @Override
    public double readDouble() {
        return this.buf.readDouble();
    }

    @Override
    public ByteBuf readBytes(int i) {
        return this.buf.readBytes(i);
    }

    @Override
    public ByteBuf readSlice(int i) {
        return this.buf.readSlice(i);
    }

    @Override
    public ByteBuf readRetainedSlice(int i) {
        return this.buf.readRetainedSlice(i);
    }

    @Override
    public ByteBuf readBytes(ByteBuf byteBuf) {
        return this.buf.readBytes(byteBuf);
    }

    @Override
    public ByteBuf readBytes(ByteBuf byteBuf, int i) {
        return this.buf.readBytes(byteBuf, i);
    }

    @Override
    public ByteBuf readBytes(ByteBuf byteBuf, int i, int i1) {
        return this.buf.readBytes(byteBuf, i, i1);
    }

    @Override
    public ByteBuf readBytes(byte[] bytes) {
        return this.buf.readBytes(bytes);
    }

    @Override
    public ByteBuf readBytes(byte[] bytes, int i, int i1) {
        return this.buf.readBytes(bytes, i, i1);
    }

    @Override
    public ByteBuf readBytes(ByteBuffer byteBuffer) {
        return this.buf.readBytes(byteBuffer);
    }

    @Override
    public ByteBuf readBytes(OutputStream outputStream, int i) throws IOException {
        return this.buf.readBytes(outputStream, i);
    }

    @Override
    public int readBytes(GatheringByteChannel gatheringByteChannel, int i) throws IOException {
        return this.buf.readBytes(gatheringByteChannel, i);
    }

    @Override
    public CharSequence readCharSequence(int i, Charset charset) {
        return this.buf.readCharSequence(i, charset);
    }

    @Override
    public int readBytes(FileChannel fileChannel, long l, int i) throws IOException {
        return this.buf.readBytes(fileChannel, l, i);
    }

    @Override
    public ByteBuf skipBytes(int i) {
        return this.buf.skipBytes(i);
    }

    @Override
    public ByteBuf writeBoolean(boolean b) {
        return this.buf.writeBoolean(b);
    }

    @Override
    public ByteBuf writeByte(int i) {
        return this.buf.writeByte(i);
    }

    @Override
    public ByteBuf writeShort(int i) {
        return this.buf.writeShort(i);
    }

    @Override
    public ByteBuf writeShortLE(int i) {
        return this.buf.writeShortLE(i);
    }

    @Override
    public ByteBuf writeMedium(int i) {
        return this.buf.writeMedium(i);
    }

    @Override
    public ByteBuf writeMediumLE(int i) {
        return this.buf.writeMediumLE(i);
    }

    @Override
    public ByteBuf writeInt(int i) {
        return this.buf.writeInt(i);
    }

    @Override
    public ByteBuf writeIntLE(int i) {
        return this.buf.writeIntLE(i);
    }

    @Override
    public ByteBuf writeLong(long l) {
        return this.buf.writeLong(l);
    }

    @Override
    public ByteBuf writeLongLE(long l) {
        return this.buf.writeLongLE(l);
    }

    @Override
    public ByteBuf writeChar(int i) {
        return this.buf.writeChar(i);
    }

    @Override
    public ByteBuf writeFloat(float v) {
        return this.buf.writeFloat(v);
    }

    @Override
    public ByteBuf writeDouble(double v) {
        return this.buf.writeDouble(v);
    }

    @Override
    public ByteBuf writeBytes(ByteBuf byteBuf) {
        return this.buf.writeBytes(byteBuf);
    }

    @Override
    public ByteBuf writeBytes(ByteBuf byteBuf, int i) {
        return this.buf.writeBytes(byteBuf, i);
    }

    @Override
    public ByteBuf writeBytes(ByteBuf byteBuf, int i, int i1) {
        return this.buf.writeBytes(byteBuf, i, i1);
    }

    @Override
    public ByteBuf writeBytes(byte[] bytes) {
        return this.buf.writeBytes(bytes);
    }

    @Override
    public ByteBuf writeBytes(byte[] bytes, int i, int i1) {
        return this.buf.writeBytes(bytes, i, i1);
    }

    @Override
    public ByteBuf writeBytes(ByteBuffer byteBuffer) {
        return this.buf.writeBytes(byteBuffer);
    }

    @Override
    public int writeBytes(InputStream inputStream, int i) throws IOException {
        return this.buf.writeBytes(inputStream, i);
    }

    @Override
    public int writeBytes(ScatteringByteChannel scatteringByteChannel, int i) throws IOException {
        return this.buf.writeBytes(scatteringByteChannel, i);
    }

    @Override
    public int writeBytes(FileChannel fileChannel, long l, int i) throws IOException {
        return this.buf.writeBytes(fileChannel, l, i);
    }

    @Override
    public ByteBuf writeZero(int i) {
        return this.buf.writeZero(i);
    }

    @Override
    public int writeCharSequence(CharSequence charSequence, Charset charset) {
        return this.buf.writeCharSequence(charSequence, charset);
    }

    @Override
    public int indexOf(int i, int i1, byte b) {
        return this.buf.indexOf(i, i1, b);
    }

    @Override
    public int bytesBefore(byte b) {
        return this.buf.bytesBefore(b);
    }

    @Override
    public int bytesBefore(int i, byte b) {
        return this.buf.bytesBefore(i, b);
    }

    @Override
    public int bytesBefore(int i, int i1, byte b) {
        return this.buf.bytesBefore(i, i1, b);
    }

    @Override
    public int forEachByte(ByteProcessor byteProcessor) {
        return this.buf.forEachByte(byteProcessor);
    }

    @Override
    public int forEachByte(int i, int i1, ByteProcessor byteProcessor) {
        return this.buf.forEachByte(i, i1, byteProcessor);
    }

    @Override
    public int forEachByteDesc(ByteProcessor byteProcessor) {
        return this.buf.forEachByteDesc(byteProcessor);
    }

    @Override
    public int forEachByteDesc(int i, int i1, ByteProcessor byteProcessor) {
        return this.buf.forEachByteDesc(i, i1, byteProcessor);
    }

    @Override
    public ByteBuf copy() {
        return this.buf.copy();
    }

    @Override
    public ByteBuf copy(int i, int i1) {
        return this.buf.copy(i, i1);
    }

    @Override
    public ByteBuf slice() {
        return this.buf.slice();
    }

    @Override
    public ByteBuf retainedSlice() {
        return this.buf.retainedSlice();
    }

    @Override
    public ByteBuf slice(int i, int i1) {
        return this.buf.slice(i, i1);
    }

    @Override
    public ByteBuf retainedSlice(int i, int i1) {
        return this.buf.retainedSlice(i, i1);
    }

    @Override
    public ByteBuf duplicate() {
        return this.buf.duplicate();
    }

    @Override
    public ByteBuf retainedDuplicate() {
        return this.buf.retainedSlice();
    }

    @Override
    public int nioBufferCount() {
        return this.buf.nioBufferCount();
    }

    @Override
    public ByteBuffer nioBuffer() {
        return this.buf.nioBuffer();
    }

    @Override
    public ByteBuffer nioBuffer(int i, int i1) {
        return this.buf.nioBuffer(i, i1);
    }

    @Override
    public ByteBuffer internalNioBuffer(int i, int i1) {
        return this.buf.internalNioBuffer(i, i1);
    }

    @Override
    public ByteBuffer[] nioBuffers() {
        return this.buf.nioBuffers();
    }

    @Override
    public ByteBuffer[] nioBuffers(int i, int i1) {
        return this.buf.nioBuffers(i, i1);
    }

    @Override
    public boolean hasArray() {
        return this.buf.hasArray();
    }

    @Override
    public byte[] array() {
        return this.buf.array();
    }

    @Override
    public int arrayOffset() {
        return this.buf.arrayOffset();
    }

    @Override
    public boolean hasMemoryAddress() {
        return this.buf.hasMemoryAddress();
    }

    @Override
    public long memoryAddress() {
        return this.buf.memoryAddress();
    }

    @Override
    public String toString(Charset charset) {
        return this.buf.toString();
    }

    @Override
    public String toString(int i, int i1, Charset charset) {
        return this.buf.toString(i, i1, charset);
    }

    @Override
    public int hashCode() {
        return this.buf.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return this.buf.equals(o);
    }

    @Override
    public int compareTo(ByteBuf byteBuf) {
        return this.buf.compareTo(byteBuf);
    }

    @Override
    public String toString() {
        return this.buf.toString();
    }

    @Override
    public ByteBuf retain(int i) {
        return this.buf.retain(i);
    }

    @Override
    public int refCnt() {
        return this.buf.refCnt();
    }

    @Override
    public ByteBuf retain() {
        return this.buf.retain();
    }

    @Override
    public ByteBuf touch() {
        return this.buf.touch();
    }

    @Override
    public ByteBuf touch(Object o) {
        return this.buf.touch(o);
    }

    @Override
    public boolean release() {
        return this.buf.release();
    }

    @Override
    public boolean release(int i) {
        return this.buf.release(i);
    }
}
