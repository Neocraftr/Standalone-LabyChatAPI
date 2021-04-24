package de.neocraftr.labychatapi.user;

import com.mojang.authlib.GameProfile;

public class ChatUser {
    private GameProfile gameProfile;
    private UserStatus status;
    private String statusMessage;
    private ServerInfo currentServerInfo;
    private String timeZone;
    private long lastOnline;
    private long firstJoined;
    private int contactAmount;

    public boolean isFriendRequest() {
        return this instanceof ChatRequest;
    }

    public boolean isOnline() {
        return this.status != UserStatus.OFFLINE;
    }

    public boolean equals(ChatUser chatUser) {
        return chatUser.getGameProfile().getId().equals(this.gameProfile.getId());
    }

    public GameProfile getGameProfile() {
        return this.gameProfile;
    }

    public UserStatus getStatus() {
        return this.status;
    }

    public String getStatusMessage() {
        return this.statusMessage;
    }

    public ServerInfo getCurrentServerInfo() {
        return this.currentServerInfo;
    }

    public String getTimeZone() {
        return this.timeZone;
    }

    public long getLastOnline() {
        return this.lastOnline;
    }

    public long getFirstJoined() {
        return this.firstJoined;
    }

    public int getContactAmount() {
        return this.contactAmount;
    }

    public ChatUser(GameProfile gameProfile, UserStatus status, String statusMessage, ServerInfo currentServerInfo, String timeZone, long lastOnline, long firstJoined, int contactAmount) {
        this.gameProfile = gameProfile;
        this.status = status;
        this.statusMessage = statusMessage;
        this.currentServerInfo = currentServerInfo;
        this.timeZone = timeZone;
        this.lastOnline = lastOnline;
        this.firstJoined = firstJoined;
        this.contactAmount = contactAmount;
    }

    public ChatUser(GameProfile gameProfile, UserStatus status, String statusMessage) {
        this.gameProfile = gameProfile;
        this.status = status;
        this.statusMessage = statusMessage;
    }

    public void setGameProfile(GameProfile gameProfile) {
        this.gameProfile = gameProfile;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public void setCurrentServerInfo(ServerInfo currentServerInfo) {
        this.currentServerInfo = currentServerInfo;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public void setLastOnline(long lastOnline) {
        this.lastOnline = lastOnline;
    }

    public void setFirstJoined(long firstJoined) {
        this.firstJoined = firstJoined;
    }

    public void setContactAmount(int contactAmount) {
        this.contactAmount = contactAmount;
    }
}