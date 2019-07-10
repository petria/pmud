package org.freakz.pmud.common.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PObject extends PMudObject {

    private Zone zone;
    private Location location;

    private String pName;
    private String altName;

    private int size;
    private int baseValue;
    private int weight;
    private int damage;

    private int state = 0;
    private int maxState = 0;

    private List<String> oFlags = new ArrayList<>();
    private Map<Integer, String> descriptions = new HashMap<>();

    private Map<Integer, PObject> contains = new HashMap<>();

    private PObject container;

    private boolean isInContainer;

    private Mobile carrier;

    private boolean isCarried;

    private Mobile wornBy;

    private boolean isWorn;

    private Mobile wieldedBy;

    private boolean isWielded;

    private Location inRoom;

    private boolean isInRoom;

    private PObject linkedTo;

    public PObject(Zone zone) {
        this.zone = zone;
    }

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public Location getLocation() {
        return location;
    }

    public Location location() {
        if (isInRoom) {
            return location;
        }
        return carrier.getLocation();
    }


    public void setLocation(Location location) {
        this.location = location;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getAltName() {
        return altName;
    }

    public void setAltName(String altName) {
        this.altName = altName;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getBaseValue() {
        return baseValue;
    }

    public void setBaseValue(int baseValue) {
        this.baseValue = baseValue;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getMaxState() {
        return maxState;
    }

    public void setMaxState(int maxState) {
        this.maxState = maxState;
    }

    public List<String> getoFlags() {
        return oFlags;
    }

    public void setoFlags(List<String> oFlags) {
        this.oFlags = oFlags;
    }

    public Map<Integer, String> getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(Map<Integer, String> descriptions) {
        this.descriptions = descriptions;
    }

    public void setDescription(int state, String description) {
        this.descriptions.put(state, description);
    }

    public String getDescription(int state) {
        return this.descriptions.get(state);
    }

    public PObject getLinkedTo() {
        return linkedTo;
    }

    public void setLinkedTo(PObject linkedTo) {
        this.linkedTo = linkedTo;
    }

    public Map<Integer, PObject> getContains() {
        return contains;
    }

    public void addContains(PObject object) {
        object.setInContainer(true);
        object.setContainer(this);
        this.contains.put(object.getId(), object);
    }

    public void removeContains(PObject object) {
        object.setInContainer(false);
        object.setContainer(null);
        this.contains.remove((object.getId()));
    }

    public String name() {
        if (pName != null) {
            return pName;
        }
        if (altName != null) {
            return altName;
        }
        return getName().toLowerCase();
    }

    public PObject getContainer() {
        return container;
    }

    public void setContainer(PObject container) {
        this.container = container;
    }

    public boolean isInContainer() {
        return isInContainer;
    }

    public void setInContainer(boolean inContainer) {
        isInContainer = inContainer;
    }

    public Mobile getCarrier() {
        return carrier;
    }

    public void setCarrier(Mobile carrier) {
        this.carrier = carrier;
    }

    public boolean isCarried() {
        return isCarried;
    }

    public void setCarried(boolean carried) {
        isCarried = carried;
    }

    public Mobile getWornBy() {
        return wornBy;
    }

    public void setWornBy(Mobile wornBy) {
        this.wornBy = wornBy;
    }

    public boolean isWorn() {
        return isWorn;
    }

    public void setWorn(boolean worn) {
        isWorn = worn;
    }

    public Mobile getWieldedBy() {
        return wieldedBy;
    }

    public void setWieldedBy(Mobile wieldedBy) {
        this.wieldedBy = wieldedBy;
    }

    public boolean isWielded() {
        return isWielded;
    }

    public void setWielded(boolean wielded) {
        isWielded = wielded;
    }

    public Location getInRoom() {
        return inRoom;
    }

    public void setInRoom(Location inRoom) {
        this.inRoom = inRoom;
    }

    public boolean isInRoom() {
        return isInRoom;
    }

    public void setIsInRoom(boolean inRoom) {
        isInRoom = inRoom;
    }
}
