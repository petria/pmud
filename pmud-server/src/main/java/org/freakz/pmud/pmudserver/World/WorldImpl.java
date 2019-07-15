package org.freakz.pmud.pmudserver.World;

import lombok.extern.slf4j.Slf4j;
import org.freakz.pmud.common.objects.*;
import org.freakz.pmud.common.player.Level;
import org.freakz.pmud.common.util.PHelpers;
import org.freakz.pmud.pmudserver.service.ScoreAndLevelsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Slf4j
public class WorldImpl implements World {

    @Autowired
    private ScoreAndLevelsService levelsService;

    private Map<String, Zone> zoneNameToZoneMap;

    private Map<String, Location> nameToLocationMap;

    private Map<String, Location> name2ToLocationMap;

    private Map<String, Mobile> nameToMobileMap;

    private List<PObject> allPObjects;

    private Map<Integer, PMudObject> idToLocationAndMobileAndObjectMap;

    private Map<String, PMudPlayer> nameToPlayerMap = new HashMap<>();


    @Override
    public void init() {
        zoneNameToZoneMap = new HashMap<>();
        nameToLocationMap = new HashMap<>();
        name2ToLocationMap = new HashMap<>();
        nameToMobileMap = new HashMap<>();
        idToLocationAndMobileAndObjectMap = new HashMap<>();
        allPObjects = new ArrayList<>();
    }


    public Map<String, Location> getNameToLocationMap() {
        return nameToLocationMap;
    }

    public void setNameToLocationMap(Map<String, Location> nameToLocationMap) {
        this.nameToLocationMap = nameToLocationMap;
    }

    @Override
    public void addLocation(Location location) {
        String key = location.getNameWithZone();
//        log.debug("Add location: {}", key);
        this.nameToLocationMap.put(key, location);
        this.name2ToLocationMap.put(location.getName2(), location);
        this.idToLocationAndMobileAndObjectMap.put(location.getId(), location);
    }

    @Override
    public void addZone(Zone zone) {
        this.zoneNameToZoneMap.put(zone.getName().toLowerCase(), zone);
    }

    @Override
    public Zone getZone(String zoneName) {
        return this.zoneNameToZoneMap.get(zoneName.toLowerCase());
    }

    @Override
    public int getZoneCount() {
        return this.zoneNameToZoneMap.size();
    }

    @Override
    public Location getLocationByName(String name) {
        return this.nameToLocationMap.get(name);
    }

    @Override
    public Location getLocationByName2(String name2) {
        return this.name2ToLocationMap.get(name2);
    }

    @Override
    public Location getLocationByZoneAndNum(String name) {
        Pattern p = Pattern.compile("(\\D*?)(\\d*?)");
        Matcher m = p.matcher(name);
        if (m.matches()) {
            String zonePart = m.group(1);
            String num = m.group(2);
            for (Zone zone : zoneNameToZoneMap.values()) {
                if (zone.getName().startsWith(zonePart)) {
                    Location locationByNumber = zone.findLocationByNumber(num);
                    return locationByNumber;
                }
            }
        }
        return null;
    }

    @Override
    public Location getLocationById(int objId) {
        PMudObject pMudObject = idToLocationAndMobileAndObjectMap.get(objId);
        if (pMudObject != null) {
            if (pMudObject instanceof Location) {
                return (Location) pMudObject;
            } else if (pMudObject instanceof PObject) {
                return ((PObject) pMudObject).getLocation();
            } else {
                return ((Mobile) pMudObject).getLocation();
            }
        }
        return null;
    }

    @Override
    public int getLocationCount() {
        return name2ToLocationMap.size();
    }

    @Override
    public Map<String, Location> getName2ToLocationMap() {
        return name2ToLocationMap;
    }

    @Override
    public void addMobile(Mobile mobile) {
        this.nameToMobileMap.put(mobile.getName().toLowerCase(), mobile);
        this.idToLocationAndMobileAndObjectMap.put(mobile.getId(), mobile);
    }

    @Override
    public int getMobileCount() {
        return this.nameToMobileMap.size();
    }

    @Override
    public List<Mobile> findMobiles(String toFind) {
        List<Mobile> found = new ArrayList<>();
        for (Mobile o : nameToMobileMap.values()) {
            if (o.getName() != null) {
                if (o.getName().equalsIgnoreCase(toFind)) {
                    found.add(o);
                }
            }
            if (o.getpName() != null) {
                if (o.getpName().equalsIgnoreCase(toFind)) {
                    found.add(o);
                }
            }
        }
        return found;
    }

    @Override
    public void addObject(PObject object) {
        this.idToLocationAndMobileAndObjectMap.put(object.getId(), object);
        this.allPObjects.add(object);
//        this.nameToObjectMap.put(object.name(), object);
    }

    @Override
    public int getObjectValue(PObject o) {
        int visible = getVisiblePlayersCount();
        if (visible == 0) {
            visible = 1;
        }
        if (visible > 9) {
            visible = 9;
        }
        return (visible * o.getBaseValue() / 9);
    }

    private int getVisiblePlayersCount() {
        return this.playerCount();
    }

    @Override
    public int getObjectCount() {
        return this.allPObjects.size();
    }

    @Override
    public void playerCloseObject(PMudPlayer player, PObject o) {
        o.setState(1);
        if (o.getLinkedTo() != null) {
            PObject o2 = o.getLinkedTo();
            o2.setState(1);
        }
    }

    @Override
    public void playerOpenObject(PMudPlayer player, PObject o) {
        o.setState(0);
        if (o.getLinkedTo() != null) {
            PObject o2 = o.getLinkedTo();
            o2.setState(0);
        }
    }

    @Override
    public void playerSummonObject(PMudPlayer player, PObject o) {
        if (o.isInRoom()) {
            o.getInRoom().removeObject(o);
        }
        if (o.isInContainer()) {
            o.getContainer().removeContains(o);
        }
        if (o.isWielded()) {
            o.getWieldedBy().removeWielded(o);
        }
        if (o.isCarried()) {
            o.getCarrier().removeCarried(o);
        }
        player.addCarried(o);
        o.setLocation(player.getLocation());
    }

    @Override
    public void playerTakeObject(PMudPlayer player, Location l, PObject o) {
        l.removeObject(o);
        player.addCarried(o);
    }

    @Override
    public void playerDropObject(PMudPlayer player, Location location, PObject o) {
        player.removeCarried(o);
        location.addObject(o);
        o.setLocation(location);
    }

    @Override
    public void playerDropObjectPit(PMudPlayer player, Location location, PObject o) {
        player.removeCarried(o);
        Location pit = getLocationByName2("pit1");
        pit.addObject(o);
    }

    @Override
    public List<PObject> findObjects(String name) {
        List<PObject> found = new ArrayList<>();
        for (PObject o : allPObjects) {
            if (PHelpers.matchToObject(o, name)) {
                found.add(o);
            }
        }
        return found;
    }

    @Override
    public PObject findClosestObject(PMudPlayer p, String name) {

        List<PObject> objs = findObjects(name);
        if (objs.size() > 0) {
            for (PObject o : objs) {
                if (o.getLocation() == p.getLocation()) {
                    return o;
                }
            }
            return objs.get(0);
        }
        return null;
    }

    @Override
    public Location isPlayerLocation(String args) {
        for (PMudPlayer p : this.nameToPlayerMap.values()) {
            if (p.getName().toLowerCase().startsWith(args)) {
                return p.getLocation();
            }
        }
        return null;
    }

    @Override
    public PMudPlayer findPlayer(String toFind) {
        return nameToPlayerMap.get(toFind.toLowerCase());
    }

    @Override
    public void addPlayer(PMudPlayer player) {
        nameToPlayerMap.put(player.getName().toLowerCase(), player);
    }

    @Override
    public int playerCount() {
        return nameToPlayerMap.size();
    }


    @Override
    public Level addPlayerScore(PMudPlayer player, int score) {
        int scr = player.getScore() + score;
        player.setScore(scr);
        return levelsService.setLevels(player);
    }

    @Override
    public Map<String, PMudPlayer> getNameToPlayerMap() {
        return nameToPlayerMap;
    }

    @Override
    public PMudPlayer removePlayer(PMudPlayer player) {
        player.getLocation().removePlayer(player);
        return nameToPlayerMap.remove(player.getName().toLowerCase());
    }

    @Override
    public void quitPlayer(PMudPlayer player) {
        for (PObject o : player.getCarried().values()) {
            playerDropObject(player, player.getLocation(), o);
        }
        removePlayer(player);
    }

    @Override
    public PMudPlayer removePlayer(String playerName) {
        PMudPlayer player = findPlayer(playerName);
        if (player != null) {
            return removePlayer(player);
        }
        return null;
    }

    @Override
    public void playerToNewLocation(PMudPlayer mover, Location old, Location toGo) {
        old.removePlayer(mover);
        toGo.addPlayer(mover);
        mover.setLocation(toGo);
    }

    @Override
    public Location findLocationByNameAtZone(String nameAtZone) {
        String[] split = nameAtZone.split("@");
        String name = split[0];
        String zoneName = split[1].toLowerCase();
        Zone zone = this.zoneNameToZoneMap.get(zoneName);
        if (zone != null) {
            return zone.findLocationByName(name);
        }
        return null;
    }
}
