package org.freakz.pmud.common.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Zone extends PMudObject implements Serializable {

    private List<Location> locations = new ArrayList<>();
    private List<Mobile> mobiles = new ArrayList<>();

    public Zone() {
        super();
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public Location findLocationByName(String name) {
        for (Location location : locations) {
            if (location.getName().equalsIgnoreCase(name)) {
                return location;
            }
        }
        return null;
    }

    public void addMobile(Mobile mobile) {
        this.mobiles.add(mobile);
    }

    public List<Mobile> getMobiles() {
        return mobiles;
    }
}
