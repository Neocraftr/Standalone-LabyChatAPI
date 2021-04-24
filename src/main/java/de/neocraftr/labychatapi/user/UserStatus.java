package de.neocraftr.labychatapi.user;

public enum UserStatus {
    ONLINE((byte)0, "a"),
    AWAY((byte)1, "b"),
    BUSY((byte)2, "5"),
    OFFLINE((byte)-1, "c");

    private byte id;
    private String chatColor;
    private String name;

    private UserStatus(byte id, String chatColor) {
        this.id = id;
        this.chatColor = chatColor;
        this.name = this.name().toLowerCase();
    }

    public static UserStatus getById(int id) {
        UserStatus[] statuses = values();

        for (UserStatus status : statuses) {
            if (status.id == id) {
                return status;
            }
        }

        return OFFLINE;
    }

    public byte getId() {
        return this.id;
    }

    public String getChatColor() {
        return this.chatColor;
    }

    public String getName() {
        return this.name;
    }
}
