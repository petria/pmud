package org.freakz.pmud.common.objects;

import java.io.Serializable;

public class PMudPlayer extends Mobile implements Serializable {

    private long pid;

    private String name;
    private String title;

    private String level;

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


    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
