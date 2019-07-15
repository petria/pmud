package org.freakz.pmud.pmudserver.World;

import org.freakz.pmud.common.objects.*;
import org.freakz.pmud.common.player.Level;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface World {

    void init();

    void addZone(Zone zone);

    Zone getZone(String zoneName);

    int getZoneCount();

    void addLocation(Location location);

    Location getLocationByName(String name);

    Location getLocationByName2(String name2);

    Location getLocationByZoneAndNum(String name);

    Location getLocationById(int objId);

    int getLocationCount();

    Map<String, Location> getName2ToLocationMap();

    Location findLocationByNameAtZone(String name);

    void addMobile(Mobile mobile);

    int getMobileCount();

    void addObject(PObject object);

    int getObjectValue(PObject o);

    int getObjectCount();

    void playerCloseObject(PMudPlayer player, PObject o);

    void playerOpenObject(PMudPlayer player, PObject o);

    void playerSummonObject(PMudPlayer player, PObject o);

    void playerTakeObject(PMudPlayer player, Location l, PObject o);

    void playerDropObject(PMudPlayer p, Location l, PObject o);

    void playerDropObjectPit(PMudPlayer player, Location location, PObject o);

    void playerToNewLocation(PMudPlayer mover, Location location, Location toGo);

    PObject findClosestObject(PMudPlayer p, String name);

    Location isPlayerLocation(String args);

    PMudPlayer findPlayer(String toFind);

    void addPlayer(PMudPlayer player);

    int playerCount();

    Level addPlayerScore(PMudPlayer player, int score);

    Map<String, PMudPlayer> getNameToPlayerMap();

    Collection<PMudPlayer> getPlayers();

    void quitPlayer(PMudPlayer player);

    PMudPlayer removePlayer(PMudPlayer player);

    PMudPlayer removePlayer(String player);

    List<PObject> findObjects(String toFind);

    List<Mobile> findMobiles(String toFind);

    void sendToPlayerF(PMudPlayer p, String format, String... params);

}
