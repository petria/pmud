package org.freakz.pmud.common.objects;

import java.io.Serializable;

public class PMudPlayer extends Mobile implements Serializable {

    private long pid;

    private String name;
    private Location location;

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
}
