package org.freakz.pmud.pmudserver.World;

import lombok.extern.slf4j.Slf4j;
import org.freakz.pmud.common.objects.Location;
import org.freakz.pmud.common.objects.Mobile;
import org.freakz.pmud.common.objects.Zone;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class WorldImpl implements World {

    private Map<String, Zone> zoneNameToZoneMap;

    private Map<String, Location> nameToLocationMap;

    private Map<String, Location> name2ToLocationMap;

    private Map<String, Mobile> nameToMobileMap;

    @Override
    public void init() {
        zoneNameToZoneMap = new HashMap<>();
        nameToLocationMap = new HashMap<>();
        name2ToLocationMap = new HashMap<>();
        nameToMobileMap = new HashMap<>();
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
