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

    private Set<String> getZoneSet() {
        Set<String> zoneSet = new HashSet<>();
        zoneSet.add("mithdan.zone");
//        zoneSet.add("village.zone");
        return zoneSet;
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            log.debug("Start world init!");

            world.init();

//            Set<String> files = getZoneSet(); //dataParser.getFiles(ZONES_DIR, 1, ".zone");
            Set<String> files = dataParser.getFiles(ZONES_DIR, 1, ".zone");
            Map<String, CDirtDataParser.ParsedZone> parsedZoneMap = dataParser.parseZoneFiles(files, ZONES_DIR);
            createWorld(parsedZoneMap);
            mapLocationExits();


            log.debug("Locations: {}", world.getLocationCount());
            log.debug("World is created!");

        } catch (Exception e) {
            log.error("FAIL", e);
        }
    }

    private void mapLocationExits() {
        Map<String, Location> name2ToLocationMap = world.getName2ToLocationMap();
        for (String key : name2ToLocationMap.keySet()) {
            Location toMap = name2ToLocationMap.get(key);
            Zone zone = toMap.getZone();

            Map<String, String> rawExistMap = toMap.getRawExistMap();
            for (String dir : rawExistMap.keySet()) {
                String exitToName = rawExistMap.get(dir);
                if (exitToName.startsWith("^")) {
//                    log.warn("exit via object link");
                } else {
                    Location exitToLocation;
                    if (exitToName.contains("@")) {
                        exitToLocation = world.findLocationByNameAtZone(exitToName);
                    } else {
                        exitToLocation = zone.findLocationByName(exitToName);
                        if (exitToLocation == null) {
                            int foo = 0;
                        }
                    }

                    if (exitToLocation != null) {
                        toMap.getExitsMap().put(dir, exitToLocation);
                    } else {
                        log.error("No exit location found {} / {} dir {} -> {}", toMap.getZone().getName(), toMap.getName(), dir, exitToName);
                    }
                }
            }
        }
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
        if (parsedZone.locations == null) {
            log.warn("No locations in zone: {}", zone.getName());
            return count;
        }
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
                        } else if (line.startsWith("Altitude")) {
                            int foo = 0;
                        }
                    }
                }

            }
            count++;

            location.setDescription(description.toString());
            location.setName2(String.format("%s%d", zone.getName().toLowerCase(), count));
            zone.getLocations().add(location);
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
            boolean firstSplit = true;
            for (String split : line.split(" ")) {
                split = split.trim();
                if (firstSplit) {
                    firstSplit = false;
                    location.setName(split);
                    continue;
                }
                if (split.isEmpty()) {
                    continue;
                }
                if (split.matches("([nsweud]):.*")) {
                    location.addRawExit(split);
                } else {
                    log.warn("Unknown exit: {} / {} - {}", location.getZone().getName(), location.getName(), split);
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
