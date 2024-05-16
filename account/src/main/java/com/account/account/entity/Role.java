package com.account.account.entity;

import java.util.stream.Stream;

public enum Role {
    ADMIN(1), USER(2);

    private int value;

    Role(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Role parse(int value) {
        return Stream.of(Role.values()).filter(i -> value == i.value).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid value " + value));
    }

    public static Role parse(String value) {
        return Stream.of(Role.values()).filter(i -> i.name().equalsIgnoreCase(value)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid value " + value));
    }

}
