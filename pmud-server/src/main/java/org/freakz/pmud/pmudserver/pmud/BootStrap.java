package org.freakz.pmud.pmudserver.pmud;

import lombok.extern.slf4j.Slf4j;
import org.freakz.pmud.common.objects.Location;
import org.freakz.pmud.common.objects.Zone;
import org.freakz.pmud.pmudserver.World.World;
import org.freakz.pmud.pmudserver.data.CDirtDataParser;
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

        log.debug("Start world init!");

        world.init();

//        Set<String> files = getZoneSet(); //dataParser.getFiles(ZONES_DIR, 1, ".zone");
        Set<String> files = dataParser.getFiles(ZONES_DIR, 1, ".zone");
        Map<String, CDirtDataParser.ParsedZone> parsedZoneMap = dataParser.parseZoneFiles(files, ZONES_DIR);
        createWorld(parsedZoneMap);

        log.debug("Locations: {}", world.getLocationCount());
        log.debug("World is created!");
    }

    private Set<String> getZoneSet() {
        Set<String> zoneSet = new HashSet<>();
        zoneSet.add("xlimbo.zone");
        zoneSet.add("village.zone");
        return zoneSet;
    }

    private void createWorld(Map<String, CDirtDataParser.ParsedZone> parsedZoneMap) {
        for (CDirtDataParser.ParsedZone parsedZone : parsedZoneMap.values()) {
            Zone zone = createZone(parsedZone);
            world.addZone(zone);
            int count = createLocations(parsedZone, zone);
            log.debug("Zone: {} with {} locations", zone.getName(), count);
        }

    }

    private int createLocations(CDirtDataParser.ParsedZone parsedZone, Zone zone) {
        int count = 0;
        for (String locationLines : parsedZone.locations) {
            Location location = new Location(zone);
            Iterator<String> iterator = getIterator(locationLines);
            boolean firstLine = true;
            boolean descline = false;
            StringBuilder description = new StringBuilder();
            while (iterator.hasNext()) {
                String line = iterator.next();
                if (firstLine) {
                    parseLocationNameAndExits(location, line);
                    firstLine = false;
                } else {
                    if (descline) {
                        if (!line.startsWith("^")) {
                            description.append(line).append("\n");
                        }
                    } else {
                        if (line.endsWith("^")) {
                            location.setTitle(line.replaceAll("\\^", ""));
                            descline = true;
                            continue;
                        }

                        if (line.startsWith("lflags")) {
                            parseLocationFlagsLine(location, line);
                            int foo = 0;
                        } else if (line.startsWith("Altitude")) {
                            int foo = 0;
                        }
                    }
                }

            }
            count++;

            location.setDescription(description.toString());
            location.setName2(String.format("%s%d", zone.getName().toLowerCase(), count));
            world.addLocation(location);

        }
        return count;
    }

    private void parseLocationFlagsLine(Location location, String line) {
        String flags = line.replaceAll("lflags \\{|}", "");
        for (String flagString : flags.split(" ")) {
            if (!flagString.isEmpty()) {
                location.addRawLFlag(flagString);
            }
        }
    }

    // n s w e u d
    private Location parseLocationNameAndExits(Location location, String line) {
        line = line.replaceAll(";", "");
        if (line.contains(":")) {
            for (String split : line.split(" ")) {
                if (split.matches("([nsweud]):.*")) {
                    location.addRawExit(split);
                } else {
                    location.setName(split);
                }
            }
        } else {
            location.setName(line.trim());
        }
        return location;
    }


    private Map<String, String> mapLinesToKeyValues(String locationLines) {
        Iterator<String> iter = getIterator(locationLines);
        Map<String, String> map = new HashMap<>();
        for (String line : locationLines.split("\n")) {
            int foo = 0;
        }
        return map;
    }

    private Iterator<String> getIterator(String locationLines) {
        String[] split = locationLines.split("\n");
        List<String> strings = Arrays.asList(split);
        return strings.iterator();
    }

    private Zone createZone(CDirtDataParser.ParsedZone parsedZone) {
        Zone zone = new Zone();
        zone.setName(parsedZone.name);
        return zone;
    }
}
