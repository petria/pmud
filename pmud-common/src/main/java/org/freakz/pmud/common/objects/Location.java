package org.freakz.pmud.common.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Location extends PMudObject implements Serializable {

    private String title;
    private String description;

    private Zone zone;

    private Map<String, String> rawExistMap = new HashMap<>();
    private List<String> locationFlags = new ArrayList<>();

    public Location() {
        super();
    }

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
        if (split[1].endsWith(";")) {
            rawExistMap.put(split[0], split[1].replaceFirst(";", ""));
        } else {
            rawExistMap.put(split[0], split[1]);
        }
    }

    public String getNameWithZone() {
        return String.format("%s@%s", getName(), zone.getName());
    }

    public void addRawLFlag(String flagString) {
        this.locationFlags.add(flagString);
    }
}
