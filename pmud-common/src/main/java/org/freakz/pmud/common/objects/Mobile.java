package org.freakz.pmud.common.objects;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Mobile extends PMudObject implements Serializable {


    public enum PSex {
        MALE("male"),
        FEMALE("female");

        private final String sex;

        PSex(String sex) {
            this.sex = sex;
        }

        public String getSex() {
            return sex;
        }

        public String text() {
            if (sex.equals("male")) {
                return "he";
            } else {
                return "she";
            }
        }

        public String textB() {
            if (sex.equals("male")) {
                return "He";
            } else {
                return "She";
            }
        }
    }

    public enum PPosition {
        STANDING("standing", "Standing"),
        SITTNG("Sitting", "Standing");

        private final String text;
        private final String textB;

        PPosition(String text, String textB) {
            this.text = text;
            this.textB = textB;
        }

        public String text() {
            return text;
        }

        public String textB() {
            return textB;
        }

    }

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

    private boolean isFighting = false;
    private Mobile fightingTo;

    private Map<Integer, PObject> carried = new HashMap<>();

    private Map<Integer, PObject> worn = new HashMap<>();

    private PObject wielded = null;

    private PSex sex = PMudPlayer.PSex.MALE;

    private PPosition position = PMudPlayer.PPosition.STANDING;

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

    public void addCarried(PObject object) {
        object.setCarried(true);
        object.setCarrier(this);
        this.carried.put(object.getId(), object);
    }

    public void removeCarried(PObject object) {
        object.setCarried(false);
        object.setCarrier(null);
        this.carried.remove(object.getId());
    }

    public Map<Integer, PObject> getCarried() {
        return carried;
    }

    public Map<Integer, PObject> getWorn() {
        return worn;
    }

    public void addWorn(PObject object) {
        object.setWorn(true);
        object.setWornBy(this);
        this.worn.put(object.getId(), object);
    }

    public void removeWorn(PObject object) {
        object.setWorn(false);
        object.setWornBy(null);
        this.worn.remove(object.getId());
    }

    public PObject getWielded() {
        return wielded;
    }

    public void setWielded(PObject object) {
        object.setWielded(true);
        object.setWieldedBy(this);
        this.wielded = object;
    }

    public void removeWielded(PObject object) {
        object.setWielded(false);
        object.setWieldedBy(null);
        this.wielded = null;
    }

    public void setSex(PSex sex) {
        this.sex = sex;
    }

    public PSex getSex() {
        return sex;
    }

    public PPosition getPosition() {
        return position;
    }

    public void setPosition(PPosition position) {
        this.position = position;
    }

    public boolean isSitting() {
        return position == PPosition.SITTNG;
    }

    public boolean isStanding() {
        return position == PPosition.STANDING;
    }


    public boolean isFighting() {
        return this.isFighting;
    }

    public void setFightingTo(Mobile other) {
        this.fightingTo = other;
        this.isFighting = true;
    }

    public void removeFightingTo() {
        this.fightingTo = null;
        this.isFighting = false;
    }

    public Mobile getFightingTo() {
        return this.fightingTo;
    }
}
