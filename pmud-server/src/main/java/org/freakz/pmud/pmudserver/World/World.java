package org.freakz.pmud.pmudserver.World;

import org.freakz.pmud.common.objects.*;

import java.util.Map;

public interface World {

    void init();

    void addZone(Zone zone);

    Zone getZone(String zoneName);

    int getZoneCount();

    void addLocation(Location location);

    Location getLocationByName(String name);

    Location getLocationByName2(String name2);

    int getLocationCount();

    Map<String, Location> getName2ToLocationMap();

    Location findLocationByNameAtZone(String name);

    void addMobile(Mobile mobile);

    int getMobileCount();

    void addObject(PObject object);

    int getObjectCount();

    void playerToNewLocation(PMudPlayer mover, Location location, Location toGo);

    PMudPlayer findPlayer(String toFind);

    void addPlayer(PMudPlayer player);

    PMudPlayer removePlayer(PMudPlayer player);

    PMudPlayer removePlayer(String player);

}
