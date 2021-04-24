package de.neocraftr.labychatapi;

import de.neocraftr.labychatapi.handeling.PacketDecoder;
import de.neocraftr.labychatapi.handeling.PacketEncoder;
import de.neocraftr.labychatapi.handeling.PacketPrepender;
import de.neocraftr.labychatapi.handeling.PacketSplitter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.util.concurrent.TimeUnit;

public class ClientChannelInitializer extends ChannelInitializer<NioSocketChannel> {
    private LabyChatClient labyChatClient;

    public ClientChannelInitializer(LabyChatClient labyChatClient) {
        this.labyChatClient = labyChatClient;
    }

    protected void initChannel(NioSocketChannel channel) {
        this.labyChatClient.setNioSocketChannel(channel);
        channel.pipeline()
                .addLast("timeout", new ReadTimeoutHandler(120L, TimeUnit.SECONDS))
                .addLast("splitter", new PacketPrepender())
                .addLast("decoder", new PacketDecoder())
                .addLast("prepender", new PacketSplitter())
                .addLast("encoder", new PacketEncoder())
                .addLast(this.labyChatClient.getPacketHandler());
    }
}
