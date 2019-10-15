package org.freakz.pmud.common.enums;

import java.util.Random;

import static org.freakz.pmud.common.MudTexts.MOB_ENTER;

public enum Exits {


    NORTH("North", "n", MOB_ENTER[0]),
    EAST("East", "e", MOB_ENTER[1]),
    SOUTH("South", "s", MOB_ENTER[2]),
    WEST("West", "w", MOB_ENTER[3]),
    UP("Up", "u", MOB_ENTER[4]),
    DOWN("Down", "d", MOB_ENTER[5]),

    NONE("None", "none", "n/a");

    private final String nice;
    private final String dir;
    private final String mobEnter;

    Exits(String nice, String dir, String mobEnter) {
        this.nice = nice;
        this.dir = dir;
        this.mobEnter = mobEnter;
    }

    public static Exits getRandomExit() {
        Random rnd = new Random();
        return values()[rnd.nextInt(values().length)];
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

    public String getNiceL() {
        return nice.toLowerCase();
    }

    public String getDir() {
        return dir;
    }

    public String getMobEnter() {
        return mobEnter;
    }
}
