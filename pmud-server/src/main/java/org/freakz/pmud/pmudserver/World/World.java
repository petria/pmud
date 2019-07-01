package org.freakz.pmud.pmudserver.World;

import org.freakz.pmud.pmudserver.objects.Location;
import org.freakz.pmud.pmudserver.objects.Zone;

public interface World {

    void init();

    void addZone(Zone zone);

    void addLocation(Location location);

    Location getLocationByName(String name);

}
