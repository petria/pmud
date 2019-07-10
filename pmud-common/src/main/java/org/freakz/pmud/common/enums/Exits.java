package org.freakz.pmud.common.enums;

public enum Exits {
    NORTH("North", "n"),
    EAST("East", "e"),
    SOUTH("South", "s"),
    WEST("West", "w"),
    UP("Up", "u"),
    DOWN("Down", "d"),

    NONE("None", "none");

    private final String nice;
    private final String dir;

    Exits(String nice, String dir) {
        this.nice = nice;
        this.dir = dir;
    }

    public static Exits getExit(String dir) {
        if (dir.matches("north|n")) {
            return NORTH;
        }
        if (dir.matches("east|e")) {
            return EAST;
        }
        if (dir.matches("south|s")) {
            return SOUTH;
        }
        if (dir.matches("west|w")) {
            return WEST;
        }
        if (dir.matches("up|u")) {
            return UP;
        }
        if (dir.matches("down|d")) {
            return DOWN;
        }
        return NONE;
    }

    public String getNice() {
        return nice;
    }

    public String getDir() {
        return dir;
    }
}
