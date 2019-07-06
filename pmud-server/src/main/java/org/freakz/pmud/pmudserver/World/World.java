package org.freakz.pmud.pmudserver.World;

import org.freakz.pmud.common.objects.Location;
import org.freakz.pmud.common.objects.Zone;

public interface World {

    void init();

    void addZone(Zone zone);

    void addLocation(Location location);

    Location getLocationByName(String name);

    Location getLocationByName2(String name2);

    int getLocationCount();
}
