package org.freakz.pmud.common.objects;

import org.freakz.pmud.common.enums.Exits;
import org.freakz.pmud.common.util.PHelpers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Location extends PMudObject implements Serializable {

    private String title;
    private String name2;
    private String description;

    private Zone zone;

    private Map<String, Location> exitsMap = new HashMap<>();
    private HashMap<String, PObject> linkedExitsMap = new HashMap<>();

    private Map<String, String> rawExistMap = new HashMap<>();
    private List<String> locationFlags = new ArrayList<>();

    private Map<Integer, Mobile> mobiles = new HashMap<>();
    private Map<Integer, PObject> objects = new HashMap<>();

    private Map<Long, PMudPlayer> players = new HashMap<>();

    public Location(Zone zone) {
        this.zone = zone;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public Location getExit(Exits exit) {
        return this.exitsMap.get(exit.getDir());
    }

    public void addRawExit(String exit) {
        addRawExit(exit, false);
    }

    public void addRawExit(String exit, boolean linked) {
        String[] split = exit.split(":");
        if (split.length == 2) {
            if (linked) {
                int foo = 0;
            }
            if (split[1].endsWith(";")) {
                rawExistMap.put(split[0], split[1].replaceFirst(";", ""));
            } else {
                rawExistMap.put(split[0], split[1]);
            }
        } else {
            int foo = 0;
        }
    }

    private Map<String, Location> getExitsMap() {
        return exitsMap;
    }

    public void setLinkedExit(String dir, PObject linkedExitObject) {
        this.linkedExitsMap.put(dir, linkedExitObject);
    }


    public Location getExitsLocation(String dir) {
        if (linkedExitsMap.get(dir) != null) {
            PObject linkedObject = linkedExitsMap.get(dir);
            if (linkedObject.getState() == 0) {
                return linkedObject.getLinkedTo().getLocation();
            }
            return null;
        } else {
            return this.exitsMap.get(dir);
        }
    }

    public void setExit(String dir, Location exitToLocation) {
        this.exitsMap.put(dir, exitToLocation);
    }

    public int getExitsCount() {
        return this.exitsMap.size(); // TODO check linked
    }


    public Map<String, String> getRawExistMap() {
        return rawExistMap;
    }

    public String getNameWithZone() {
        return String.format("%s@%s", getName(), zone.getName());
    }

    public void addRawLFlag(String flagString) {
        this.locationFlags.add(flagString);
    }

    public List<String> getLocationFlags() {
        return locationFlags;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public String getName2() {
        return name2;
    }

    public void addMobile(Mobile mobile) {
        this.mobiles.put(mobile.getId(), mobile);
    }

    public void removeMobile(Mobile mobile) {
        this.mobiles.remove(mobile.getId());
    }

    public Map<Integer, Mobile> getMobiles() {
        return mobiles;
    }

    public Map<Integer, PObject> getObjects() {
        return objects;
    }

    public void addObject(PObject object) {
        object.setIsInRoom(true);
        object.setInRoom(this);
        this.objects.put(object.getId(), object);
    }

    public void removeObject(PObject object) {
        object.setIsInRoom(false);
        object.setInRoom(null);
        this.objects.remove(object.getId());
    }

    public PObject getObject(String name) {
        for (PObject o : this.objects.values()) {
            if (PHelpers.matchToObject(o, name)) {
                return o;
            }
        }
        return null;
    }

    public boolean hasPit() {
        for (PObject o : this.objects.values()) {
            if (o.getZone().getName().equals("start") && PHelpers.matchToObject(o, "pit")) {
                return true;
            }
        }
        return false; // TODO
    }

    public Map<Long, PMudPlayer> getPlayers() {
        return players;
    }

    public void addPlayer(PMudPlayer player) {
        this.players.put(player.getPid(), player);
    }

    public void removePlayer(PMudPlayer player) {
        this.players.remove(player.getPid());
    }

    @Override
    public String toString() {
        return name2;
    }

    public Mobile getMobile(String name) {
        for (Mobile m : this.mobiles.values()) {
            if (PHelpers.matchToMobile(m, name)) {
                return m;
            }
        }
        return null;
    }

    public PMudPlayer getPlayer(String name, PMudPlayer ignore) {
        for (Mobile p : this.players.values()) {
            if (ignore != null) {
                if (p.getId() == ignore.getId()) {
                    continue;
                }
            }
            if (PHelpers.matchToMobile(p, name)) {
                return (PMudPlayer) p;
            }
        }
        return null;
    }

}
