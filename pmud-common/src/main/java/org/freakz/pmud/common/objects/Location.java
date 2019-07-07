package org.freakz.pmud.common.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Location extends PMudObject implements Serializable {


    public enum Exits {
        NORTH("North", "n"),
        EAST("East", "e"),
        SOUTH("South", "s"),
        WEST("West", "w"),
        UP("Up", "u"),
        DOWN("Down", "d");

        private final String nice;
        private final String dir;

        Exits(String nice, String dir) {
            this.nice = nice;
            this.dir = dir;
        }

        public String getNice() {
            return nice;
        }

        public String getDir() {
            return dir;
        }
    }

    private String title;
    private String name2;
    private String description;

    private Zone zone;

    private Map<String, Location> exitsMap = new HashMap<>();
    private Map<String, String> rawExistMap = new HashMap<>();
    private List<String> locationFlags = new ArrayList<>();

    private Map<String, Mobile> mobiles = new HashMap<>();

    public Location(Zone zone) {
        this.zone = zone;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public Zone getZone() {
        return zone;
    }


    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public void addRawExit(String exit) {
        String[] split = exit.split(":");
        if (split.length == 2) {
            if (split[1].endsWith(";")) {
                rawExistMap.put(split[0], split[1].replaceFirst(";", ""));
            } else {
                rawExistMap.put(split[0], split[1]);
            }
        } else {
            int foo = 0;
        }
    }

    public Map<String, Location> getExitsMap() {
        return exitsMap;
    }

    public Map<String, String> getRawExistMap() {
        return rawExistMap;
    }

    public String getNameWithZone() {
        return String.format("%s@%s", getName(), zone.getName());
    }

    public void addRawLFlag(String flagString) {
        this.locationFlags.add(flagString);
    }

    public List<String> getLocationFlags() {
        return locationFlags;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public String getName2() {
        return name2;
    }

    public void addMobile(Mobile mobile) {
        this.mobiles.put(mobile.getName(), mobile);
    }

    public void removeMobile(Mobile mobile) {
        this.mobiles.remove(mobile.getName());
    }

    public Map<String, Mobile> getMobiles() {
        return mobiles;
    }
}
