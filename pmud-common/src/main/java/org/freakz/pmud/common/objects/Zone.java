package org.freakz.pmud.common.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Zone extends PMudObject implements Serializable {

    private List<Location> locations = new ArrayList<>();
    private List<Mobile> mobiles = new ArrayList<>();
    private List<PObject> objects = new ArrayList<>();

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

    public Location findLocationByName2(String name2) {
        for (Location location : locations) {
            if (location.getName2().equalsIgnoreCase(name2)) {
                return location;
            }
        }
        return null;
    }

    public Location findLocationByNumber(String num) {
        String nameNumber = getName().toLowerCase() + num;
        return findLocationByName2(nameNumber);
    }

    public void addMobile(Mobile mobile) {
        this.mobiles.add(mobile);
    }

    public List<Mobile> getMobiles() {
        return mobiles;
    }

    public void addObject(PObject object) {
        this.objects.add(object);
    }

    public PObject findObjectByName(String to) {
        for (PObject pObject : this.objects) {
            if (pObject.getName().equals(to)) {
                return pObject;
            }
        }
        return null;

    }

    public Mobile findMobileByName(String name) {
        for (Mobile mobile : this.mobiles) {
            if (mobile.getName().equals(name)) {
                return mobile;
            }
        }
        return null;
    }


    @Override
    public boolean equals(Object obj) {
        Zone other = (Zone) obj;
        return name.equals(other.getName());
    }
}
