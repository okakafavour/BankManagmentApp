package org.example.enums;

public enum Direction {
    SENT,
    RECEIVED,
    SELF;


    public static Direction fromString(String value) {
        try {
            return Direction.valueOf(value.toUpperCase());
        } catch (Exception e) {
            return SELF;
        }
    }
}
