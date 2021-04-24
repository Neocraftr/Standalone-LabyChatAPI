package de.neocraftr.labychatapi;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import de.neocraftr.labychatapi.handeling.PacketEncryptingDecoder;
import de.neocraftr.labychatapi.handeling.PacketEncryptingEncoder;
import de.neocraftr.labychatapi.handeling.PacketHandler;
import de.neocraftr.labychatapi.packets.Packet;
import de.neocraftr.labychatapi.packets.PacketDisconnect;
import de.neocraftr.labychatapi.packets.PacketHelloPing;
import de.neocraftr.labychatapi.user.ChatUser;
import de.neocraftr.labychatapi.utils.CryptManager;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.SecretKey;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class LabyChatClient {

    public static final Logger LOGGER = LogManager.getLogger("LabyChat");

    public static final String LABYCHAT_IP = "mod.labymod.net";
    public static final int LABYCHAT_PORT = 30336;

    private NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Chat#%d").build());
    private ExecutorService executorService = Executors.newFixedThreadPool(2, (new ThreadFactoryBuilder()).setNameFormat("Helper#%d").build());
    private NioSocketChannel nioSocketChannel;
    private Bootstrap bootstrap;
    private PacketHandler packetHandler;
    private ChatUser user;
    private String accessToken;
    private ConnectCallback connectCallback;

    public LabyChatClient(PacketHandler packetHandler) {
        this.packetHandler = packetHandler;
        this.packetHandler.setClient(this);
    }

    public void enableEncryption(SecretKey key) {
        this.nioSocketChannel.pipeline().addBefore("splitter", "decrypt", new PacketEncryptingDecoder(CryptManager.createNetCipherInstance(2, key)));
        this.nioSocketChannel.pipeline().addBefore("prepender", "encrypt", new PacketEncryptingEncoder(CryptManager.createNetCipherInstance(1, key)));
    }

    public void connect(final ChatUser user, final String accessToken, final ConnectCallback connectCallback) {
        this.user = user;
        this.accessToken = accessToken;
        this.connectCallback = connectCallback;

        if (this.nioSocketChannel != null && this.nioSocketChannel.isOpen()) {
            this.nioSocketChannel.close();
            this.nioSocketChannel = null;
        }

        this.bootstrap = new Bootstrap();
        this.bootstrap.group(this.nioEventLoopGroup);
        this.bootstrap.option(ChannelOption.TCP_NODELAY, true);
        this.bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);
        this.bootstrap.channel(NioSocketChannel.class);
        this.bootstrap.handler(new ClientChannelInitializer(this));
        this.executorService.execute(() -> {
            try {
                LOGGER.debug("Connecting to "+LABYCHAT_IP+":"+LABYCHAT_PORT);
                LabyChatClient.this.bootstrap.connect(LABYCHAT_IP, LABYCHAT_PORT).syncUninterruptibly();
                LabyChatClient.this.sendPacket(new PacketHelloPing(System.currentTimeMillis()));
            } catch(Exception e) {
                e.printStackTrace();
                LabyChatClient.LOGGER.error("Unable to connect to server: "+e);
                if(LabyChatClient.this.connectCallback != null) {
                    LabyChatClient.this.connectCallback.finished(false, "Unable to connect to server: "+e);
                }
            }
        });
    }

    public void disconnect(final boolean kicked) {
        this.executorService.execute(new Runnable() {
            public void run() {
                if (LabyChatClient.this.nioSocketChannel != null && !kicked) {
                    LabyChatClient.this.nioSocketChannel.writeAndFlush(new PacketDisconnect("Logout")).addListener(new ChannelFutureListener() {
                        public void operationComplete(ChannelFuture channelFuture) {
                            if (LabyChatClient.this.nioSocketChannel != null) {
                                LabyChatClient.this.nioSocketChannel.close();
                            }
                        }
                    });
                }
            }
        });
    }

    public boolean isConnected() {
        return this.nioSocketChannel != null && this.nioSocketChannel.isOpen() && this.nioSocketChannel.isWritable();
    }

    public void sendPacket(final Packet packet, final Consumer<NioSocketChannel> consumer) {
        if (this.nioSocketChannel != null) {
            if (this.nioSocketChannel.isOpen() && this.nioSocketChannel.isWritable()) {
                if (this.nioSocketChannel.eventLoop().inEventLoop()) {
                    this.nioSocketChannel.writeAndFlush(packet).addListeners(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
                    if (consumer != null) {
                        consumer.accept(this.nioSocketChannel);
                    }
                } else {
                    this.nioSocketChannel.eventLoop().execute(new Runnable() {
                        public void run() {
                            LabyChatClient.this.nioSocketChannel.writeAndFlush(packet).addListeners(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
                            if (consumer != null) {
                                consumer.accept(LabyChatClient.this.nioSocketChannel);
                            }

                        }
                    });
                }
            }
        }
    }

    public void sendPacket(Packet packet) {
        this.sendPacket(packet, null);
    }

    public void setNioSocketChannel(NioSocketChannel nioSocketChannel) {
        this.nioSocketChannel = nioSocketChannel;
    }

    public void setPacketHandler(PacketHandler packetHandler) {
        this.packetHandler = packetHandler;
    }

    public PacketHandler getPacketHandler() {
        return packetHandler;
    }

    public ChatUser getUser() {
        return user;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public ConnectCallback getConnectCallback() {
        return connectCallback;
    }
}
