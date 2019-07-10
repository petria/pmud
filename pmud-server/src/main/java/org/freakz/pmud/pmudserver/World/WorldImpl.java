package org.freakz.pmud.pmudserver.World;

import lombok.extern.slf4j.Slf4j;
import org.freakz.pmud.common.objects.*;
import org.freakz.pmud.common.util.PHelpers;
import org.freakz.pmud.pmudserver.service.ScoreAndLevelsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class WorldImpl implements World {

    @Autowired
    private ScoreAndLevelsService levelsService;

    private Map<String, Zone> zoneNameToZoneMap;

    private Map<String, Location> nameToLocationMap;

    private Map<String, Location> name2ToLocationMap;

    private Map<String, Mobile> nameToMobileMap;

    private Map<String, PObject> nameToObjectMap;

    private Map<String, PMudPlayer> nameToPlayerMap = new HashMap<>();


    @Override
    public void init() {
        zoneNameToZoneMap = new HashMap<>();
        nameToLocationMap = new HashMap<>();
        name2ToLocationMap = new HashMap<>();
        nameToMobileMap = new HashMap<>();
        nameToObjectMap = new HashMap<>();
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
        this.nameToObjectMap.put(object.getName(), object);
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
        return this.nameToObjectMap.size();
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
        for (PObject o : nameToObjectMap.values()) {
            if (PHelpers.matchToObject(o, name)) {
                found.add(o);
            }
        }
        return found;
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
    public void addPlayerScore(PMudPlayer player, int score) {
        int scr = player.getScore() + score;
        player.setScore(scr);
        levelsService.setLevels(player);
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
