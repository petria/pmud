package org.freakz.pmud.pmudserver.pmud;

import lombok.extern.slf4j.Slf4j;
import org.freakz.pmud.pmudserver.World.World;
import org.freakz.pmud.pmudserver.data.CDirtDataParser;
import org.freakz.pmud.pmudserver.objects.Location;
import org.freakz.pmud.pmudserver.objects.Zone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class BootStrap implements CommandLineRunner {

    private static final String ZONES_DIR = "c-dirt/data/ZONES/";


    @Autowired
    private CDirtDataParser dataParser;

    @Autowired
    private World world;


    @Override
    public void run(String... args) throws Exception {
        Set<String> files = dataParser.getFiles(ZONES_DIR, 1, ".zone");
        Map<String, CDirtDataParser.ParsedZone> parsedZoneMap = dataParser.parseZoneFiles(files, ZONES_DIR);
        createWorld(parsedZoneMap);
        log.debug("World is created!");
    }

    private void createWorld(Map<String, CDirtDataParser.ParsedZone> parsedZoneMap) {
        world.init();
        for (CDirtDataParser.ParsedZone parsedZone : parsedZoneMap.values()) {
            Zone zone = createZone(parsedZone);
            List<Location> locations = createLocations(parsedZone);

            world.addZone(zone);
        }
    }

    private List<Location> createLocations(CDirtDataParser.ParsedZone parsedZone) {
        List<Location> locations = new ArrayList<>();
        for (String locationLines : parsedZone.locations) {
            Map<String, String> map = mapLinesToKeyValues(locationLines);
        }
        return locations;
    }

    private Map<String, String> mapLinesToKeyValues(String locationLines) {

        Iterator<String> iterator = (Iterator<String>) Arrays.asList(locationLines.split("\n"));


        Map<String, String> map = new HashMap<>();
        for (String line : locationLines.split("\n")) {
            int foo = 0;
        }
        return map;
    }

    private Zone createZone(CDirtDataParser.ParsedZone parsedZone) {
        Zone zone = new Zone();
        zone.setName(parsedZone.name);
        return zone;
    }
}
