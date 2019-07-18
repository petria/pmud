package org.freakz.pmud.common.objects;

import org.freakz.pmud.common.enums.PClass;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Mobile extends PMudObject implements Serializable {

    private Zone zone;
    private Location location;
    private Location startLocation;

    private String pName;

    private int levelNum = 1;
    private int score = 0;

    private int strength = 0;
    private int maxStrength = 0;
    private int mana = 0;
    private int maxMana = 0;
    private int kills = 0;
    private int deaths = 0;
    private int wimpy = 0;


    private int damage;
    private int aggression;
    private int armor;
    private int speed;

    private List<String> sFlags;
    private List<String> pFlags;
    private List<String> mFlags;

    private String description;
    private String examine;

    private boolean isMobile = true;

    private boolean isFighting = false;
    private Mobile fightingTo;

    private boolean isDead;
    private String corpseText;

    private String title;

    private PClass pClass;


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

    public Location getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(Location startLocation) {
        this.startLocation = startLocation;
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
        if (getWielded() != null) {
            if (isMobile) {
                return damage + (getWielded().getDamage() / 2);
            } else {
                return damage + getWielded().getDamage();
            }
        }
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

    public boolean isDead() {
        return this.isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public String getCorpseText() {
        return corpseText;
    }

    public void setCorpseText(String corpseText) {
        this.corpseText = corpseText;
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


    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getWimpy() {
        return wimpy;
    }

    public void setWimpy(int wimpy) {
        this.wimpy = wimpy;
    }

    public int getMaxStrength() {
        if (isMobile) {
            return maxStrength;
        } else {
            return 75 + ((levelNum - 1) * 8);
        }
    }

    public void setMaxStrength(int maxStrength) {
        this.maxStrength = maxStrength;
    }

    public int getMaxMana() {
        return 15 + ((levelNum - 1) * 3);
    }

    public void setMaxMana(int maxMana) {
        this.maxMana = maxMana;
    }

    public int getLevelNum() {
        return levelNum;
    }

    public void setLevelNum(int levelNum) {
        this.levelNum = levelNum;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String name() {
        if (name != null) {
            return name;
        }
        return pName;
    }


    public boolean isMobile() {
        return isMobile;
    }

    public void setMobile(boolean mobile) {
        isMobile = mobile;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public PClass getpClass() {
        return pClass;
    }

    public void setpClass(PClass pClass) {
        this.pClass = pClass;
    }

    public String getLevelName() {
        return "TODO LevelName";
    }

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

        public String herHis() {
            if (sex.equals("male")) {
                return "his";
            } else {
                return "her";
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

}
