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
        this.contains.put(object.getId(), object);
    }

    public void removeContains(PObject object) {
        this.contains.remove((object.getId()));
    }

}
