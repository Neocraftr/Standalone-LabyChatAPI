package de.neocraftr.labychatapi.handeling;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.exceptions.InvalidCredentialsException;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import de.neocraftr.labychatapi.LabyChatClient;
import de.neocraftr.labychatapi.packets.*;
import de.neocraftr.labychatapi.utils.CryptManager;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.nio.NioSocketChannel;

import javax.crypto.SecretKey;
import java.math.BigInteger;
import java.net.Proxy;
import java.security.PublicKey;
import java.util.Calendar;
import java.util.UUID;
import java.util.function.Consumer;

@ChannelHandler.Sharable
public class PacketHandler extends SimpleChannelInboundHandler<Object> {

    private LabyChatClient client;

    protected void channelRead0(ChannelHandlerContext ctx, Object packet) {
        this.handlePacket((Packet)packet);
    }

    private void handlePacket(Packet packet) {
        packet.handle(this);
    }

    public void handle(PacketHelloPing packet) {}

    public void handle(PacketHelloPong packet) {
        GameProfile userProfile = this.client.getUser().getGameProfile();
        this.client.sendPacket(new PacketLoginData(userProfile.getId(), userProfile.getName(), this.client.getUser().getStatusMessage()));
        this.client.sendPacket(new PacketLoginOptions(true, this.client.getUser().getStatus(), Calendar.getInstance().getTimeZone()));
        this.client.sendPacket(new PacketLoginVersion(24, "1.8.9_3.8.5"));
    }

    public void handle(PacketLoginStart packet) {}

    public void handle(PacketLoginData packet) {}

    public void handle(PacketLoginFriend packet) {}

    public void handle(PacketLoginRequest packet) {}

    public void handle(PacketLoginOptions packet) {}

    public void handle(PacketLoginComplete packet) {
        LabyChatClient.LOGGER.debug("Connected to LabyChat!");
        if(PacketHandler.this.client.getConnectCallback() != null) {
            PacketHandler.this.client.getConnectCallback().finished(true, null);
        }
    }

    public void handle(PacketLoginTime packet) {}

    public void handle(PacketLoginVersion packet) {}

    public void handle(PacketEncryptionRequest packet) {
        SecretKey secretKey = CryptManager.createNewSharedKey();
        PublicKey publicKey = CryptManager.decodePublicKey(packet.getPublicKey());
        String serverId = packet.getServerId();
        String hash = new BigInteger(CryptManager.getServerIdHash(serverId, publicKey, secretKey)).toString(16);
        try {
            MinecraftSessionService sessionService = new YggdrasilAuthenticationService(Proxy.NO_PROXY, UUID.randomUUID().toString()).createMinecraftSessionService();
            sessionService.joinServer(this.client.getUser().getGameProfile(), this.client.getAccessToken(), hash);
            this.client.sendPacket(new PacketEncryptionResponse(secretKey, publicKey, packet.getVerifyToken()), new Consumer<NioSocketChannel>() {
                @Override
                public void accept(NioSocketChannel nioSocketChannel) {
                    PacketHandler.this.client.enableEncryption(secretKey);
                    LabyChatClient.LOGGER.debug("Authenticated with server");
                }
            });
            return;
        } catch (AuthenticationUnavailableException e) {
            LabyChatClient.LOGGER.error("Authentication failed: Mojang auth service unavaileable");
            if(this.client.getConnectCallback() != null) {
                this.client.getConnectCallback().finished(false, "Authentication failed: Mojang auth service unavaileable");
            }
        } catch(InvalidCredentialsException e) {
            LabyChatClient.LOGGER.error("Authentication failed: Invalid credentials");
            if(this.client.getConnectCallback() != null) {
                this.client.getConnectCallback().finished(false, "Authentication failed: Invalid credentials");
            }
        } catch(AuthenticationException e) {
            e.printStackTrace();
            LabyChatClient.LOGGER.error("Authentication failed: "+e);
            if(this.client.getConnectCallback() != null) {
                this.client.getConnectCallback().finished(false, "Authentication failed: "+e);
            }
        }
        this.client.disconnect(false);
    }

    public void handle(PacketEncryptionResponse packet) {}

    public void handle(PacketPlayPlayerOnline packet) {}

    public void handle(PacketPlayRequestAddFriend packet) {}

    public void handle(PacketPlayRequestAddFriendResponse packet) {}

    public void handle(PacketPlayRequestRemove packet) {}

    public void handle(PacketPlayDenyFriendRequest packet) {}

    public void handle(PacketPlayFriendRemove packet) {}

    public void handle(PacketPlayChangeOptions packet) {}

    public void handle(PacketPlayServerStatus packet) {}

    public void handle(PacketPlayFriendStatus packet) {}

    public void handle(PacketPlayFriendPlayingOn packet) {}

    public void handle(PacketPlayTyping packet) {}

    public void handle(PacketMojangStatus packet) {}

    public void handle(PacketActionPlay packet) {}

    public void handle(PacketActionPlayResponse packet) {}

    public void handle(PacketActionRequest packet) {}

    public void handle(PacketActionRequestResponse packet) {}

    public void handle(PacketUpdateCosmetics packet) {}

    public void handle(PacketAddonMessage packet) {}

    public void handle(PacketUserBadge packet) {}

    public void handle(PacketAddonDevelopment packet) {}

    public void handle(PacketDisconnect packet) {
        this.client.disconnect(true);
        LabyChatClient.LOGGER.debug("Got kicked: "+packet.getReason());
    }

    public void handle(PacketKick packet) {
        this.client.disconnect(true);
        LabyChatClient.LOGGER.debug("Got kicked: "+packet.getReason());
    }

    public void handle(PacketPing packet) {
        this.client.sendPacket(new PacketPong());
    }

    public void handle(PacketPong packet) {}

    public void handle(PacketServerMessage packet) {}

    public void handle(PacketMessage packet) {}

    public void handle(PacketNotAllowed packet) {}

    public void handle(PacketChatVisibilityChange packet) {}

    public void handle(PacketPlayServerStatusUpdate packet) {}

    public LabyChatClient getClient() {
        return client;
    }

    public void setClient(LabyChatClient client) {
        this.client = client;
    }
}

