package org.freakz.pmud.pmudserver.World;

import lombok.extern.slf4j.Slf4j;
import org.freakz.pmud.common.objects.Location;
import org.freakz.pmud.common.objects.Zone;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class WorldImpl implements World {

    private Map<String, Zone> nameToZoneMap;

    private Map<String, Location> nameToLocationMap;

    @Override
    public void init() {
        nameToZoneMap = new HashMap<>();
        nameToLocationMap = new HashMap<>();
    }


    public Map<String, Location> getNameToLocationMap() {
        return nameToLocationMap;
    }

    public void setNameToLocationMap(Map<String, Location> nameToLocationMap) {
        this.nameToLocationMap = nameToLocationMap;
    }

    @Override
    public void addLocation(Location location) {
        this.nameToLocationMap.put(location.getName(), location);
    }

    @Override
    public void addZone(Zone zone) {
        this.nameToZoneMap.put(zone.getName(), zone);
    }

    @Override
    public Location getLocationByName(String name) {
        return this.nameToLocationMap.get(name);
    }
}
