package de.neocraftr.labychatapi.user;

import com.mojang.authlib.GameProfile;

public class ChatRequest extends ChatUser {
    public ChatRequest(GameProfile gameProfile) {
        super(gameProfile, UserStatus.OFFLINE, "", null,
                "", 0L, 0L, 0);
    }
}
