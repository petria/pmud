package org.freakz.pmud.common.objects;

import org.freakz.pmud.common.util.PHelpers;

import java.io.Serializable;

public class PMudPlayer extends Mobile implements Serializable {

    private long pid;

    private String name;
    private Location location;

    private int qPoints = 0;
    private int age = 20;
    private int coins = 0;
    private int acAvg = 0;

    public PMudPlayer(Zone zone) {
        super(zone);
        setMobile(false);
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


    public PObject isCarrying(String name) {
        for (PObject o : getCarried().values()) {
            if (PHelpers.matchToObject(o, name)) {
                return o;
            }
        }
        return null;
    }
}
