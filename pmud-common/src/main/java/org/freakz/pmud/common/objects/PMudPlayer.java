package org.freakz.pmud.common.objects;

import org.freakz.pmud.common.enums.PClass;
import org.freakz.pmud.common.util.PHelpers;

import java.io.Serializable;

public class PMudPlayer extends Mobile implements Serializable {

    private long pid;

    private String name;
    private String title;

    private PClass pClass;

    private Location location;

    private int levelNum;
    private int score = 0;
    private int strength = 0;
    private int maxStrength = 0;
    private int mana = 0;
    private int maxMana = 0;
    private int kills = 0;
    private int deaths = 0;
    private int wimpy = 0;
    private int qPoints = 0;
    private int age = 20;
    private int coins = 0;
    private int acAvg = 0;

    public PMudPlayer(Zone zone) {
        super(zone);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object obj) {
        PMudPlayer other = (PMudPlayer) obj;
        return other.getName().equals(this.name);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getLevelNum() {
        return levelNum;
    }

    public void setLevelNum(int levelNum) {
        this.levelNum = levelNum;
    }

    public PClass getpClass() {
        return pClass;
    }

    public void setpClass(PClass pClass) {
        this.pClass = pClass;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public int getStrength() {
        return strength;
    }

    @Override
    public void setStrength(int strength) {
        this.strength = strength;
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

    public int getqPoints() {
        return qPoints;
    }

    public void setqPoints(int qPoints) {
        this.qPoints = qPoints;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public int getAcAvg() {
        return acAvg;
    }

    public void setAcAvg(int acAvg) {
        this.acAvg = acAvg;
    }

    public int getMaxStrength() {
        return maxStrength;
    }

    public void setMaxStrength(int maxStrength) {
        this.maxStrength = maxStrength;
    }

    public int getMaxMana() {
        return maxMana;
    }

    public void setMaxMana(int maxMana) {
        this.maxMana = maxMana;
    }

    public PObject isCarrying(String name) {
        for (PObject o : getCarried().values()) {
            if (PHelpers.matchToObject(o, name)) {
                return o;
            }
        }
        return null;
    }
}
