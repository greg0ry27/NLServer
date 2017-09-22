package com.nl.data;

public class PlayerCharacter {
    private long id;
    private String name;
    private int level;
    private PlayerCharacterType type;
    private String location;

    public PlayerCharacter(long id, String name, int level, PlayerCharacterType type) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public PlayerCharacterType getType() {
        return type;
    }

    public String getLocation() {
        return location;
    }
}
