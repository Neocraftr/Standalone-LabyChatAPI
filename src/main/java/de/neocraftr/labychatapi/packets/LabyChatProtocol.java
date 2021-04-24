package de.neocraftr.labychatapi.packets;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class LabyChatProtocol {

    private static LabyChatProtocol instance = new LabyChatProtocol();
    private Map<Integer, Class<? extends Packet>> packets = new HashMap();

    public LabyChatProtocol() {
        register(0, PacketHelloPing.class);
        register(1, PacketHelloPong.class);
        register(2, PacketLoginStart.class);
        register(3, PacketLoginData.class);
        register(4, PacketLoginFriend.class);
        register(5, PacketLoginRequest.class);
        register(6, PacketLoginOptions.class);
        register(7, PacketLoginComplete.class);
        register(8, PacketLoginTime.class);
        register(9, PacketLoginVersion.class);
        register(10, PacketEncryptionRequest.class);
        register(11, PacketEncryptionResponse.class);
        register(14, PacketPlayPlayerOnline.class);
        register(16, PacketPlayRequestAddFriend.class);
        register(17, PacketPlayRequestAddFriendResponse.class);
        register(18, PacketPlayRequestRemove.class);
        register(19, PacketPlayDenyFriendRequest.class);
        register(20, PacketPlayFriendRemove.class);
        register(21, PacketPlayChangeOptions.class);
        register(22, PacketPlayServerStatus.class);
        register(23, PacketPlayFriendStatus.class);
        register(24, PacketPlayFriendPlayingOn.class);
        register(25, PacketPlayTyping.class);
        register(26, PacketMojangStatus.class);
        register(27, PacketActionPlay.class);
        register(28, PacketActionPlayResponse.class);
        register(29, PacketActionRequest.class);
        register(30, PacketActionRequestResponse.class);
        register(31, PacketUpdateCosmetics.class);
        register(32, PacketAddonMessage.class);
        register(33, PacketUserBadge.class);
        register(34, PacketAddonDevelopment.class);
        register(60, PacketDisconnect.class);
        register(61, PacketKick.class);
        register(62, PacketPing.class);
        register(63, PacketPong.class);
        register(64, PacketServerMessage.class);
        register(65, PacketMessage.class);
        register(66, PacketNotAllowed.class);
        register(67, PacketChatVisibilityChange.class);
        register(68, PacketPlayServerStatusUpdate.class);
    }

    private void register(int id, Class<? extends Packet> clazz) {
        try {
            clazz.newInstance();
            this.packets.put(id, clazz);
        } catch (Exception ignored) {
            System.err.println("Class " + clazz.getSimpleName() + " does not contain a default Constructor, this might break the game :/");
        }
    }

    public Packet getPacket(int id) throws IllegalAccessException, InstantiationException {
        if (!this.packets.containsKey(id)) {
            throw new RuntimeException("Packet with id " + id + " is not registered.");
        } else {
            return this.packets.get(id).newInstance();
        }
    }

    public int getPacketId(Packet packet) {
        Iterator<Map.Entry<Integer, Class<? extends Packet>>> iterator = this.packets.entrySet().iterator();

        Map.Entry<Integer, Class<? extends Packet>> entry;
        Class<? extends Packet> clazz;
        do {
            if (!iterator.hasNext()) {
                throw new RuntimeException("Packet " + packet + " is not registered.");
            }

            entry = iterator.next();
            clazz = entry.getValue();
        } while(!clazz.isInstance(packet));

        return entry.getKey();
    }

    public static LabyChatProtocol getProtocol() {
        return instance;
    }
}
