package org.freakz.pmud.common.objects;

import java.util.List;

public class Mobile extends PMudObject {

    private Zone zone;
    private Location location;

    private String pName;

    private int strength;
    private int damage;
    private int aggression;
    private int armor;
    private int speed;

    private List<String> sFlags;
    private List<String> pFlags;
    private List<String> mFlags;

    private String description;
    private String examine;

    public Mobile(Zone zone) {
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

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getAggression() {
        return aggression;
    }

    public void setAggression(int aggression) {
        this.aggression = aggression;
    }

    public int getArmor() {
        return armor;
    }

    public void setArmor(int armor) {
        this.armor = armor;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public List<String> getsFlags() {
        return sFlags;
    }

    public void setsFlags(List<String> sFlags) {
        this.sFlags = sFlags;
    }

    public List<String> getpFlags() {
        return pFlags;
    }

    public void setpFlags(List<String> pFlags) {
        this.pFlags = pFlags;
    }

    public List<String> getmFlags() {
        return mFlags;
    }

    public void setmFlags(List<String> mFlags) {
        this.mFlags = mFlags;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExamine() {
        return examine;
    }

    public void setExamine(String examine) {
        this.examine = examine;
    }
}
