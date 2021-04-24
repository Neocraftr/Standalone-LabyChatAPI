Standalone API for the Minecraft LabyMod chat which doesn't requires the original game at all.

Example usage:
```java
LabyChatClient chatClient = new LabyChatClient(new PacketHandler() {
    @Override
    public void handle(PacketMessage packet) {
        System.out.println(String.format("Received message from %s: %s", packet.getSender().getGameProfile().getName(), packet.getMessage()));
    }
});

GameProfile gameProfile = new GameProfile(UUID.fromString("<minecraft uuid>"), "<minecraft playername>");
ChatUser user = new ChatUser(gameProfile, UserStatus.ONLINE, "Test");
String accessToken = "<minecraft access token>";

chatClient.connect(user, accessToken, new ConnectCallback() {
    @Override
    public void finished(boolean success, String errorMessage) {
        if(success) {
            System.out.println("Successfully connected to LabyChat :D");
        }
    }
});
```