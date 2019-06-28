package org.freakz.pmud.pmudserver.World;

import lombok.extern.slf4j.Slf4j;
import org.freakz.pmud.pmudserver.objects.Zone;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class WorldImpl implements World {

    private Map<String, Zone> nameToZoneMap;

    @Override
    public void init() {
        nameToZoneMap = new HashMap<>();
    }

    @Override
    public void addZone(Zone zone) {
        this.nameToZoneMap.put(zone.getName(), zone);
    }
}
