package org.freakz.pmud.pmudserver.pmud;

import lombok.extern.slf4j.Slf4j;
import org.freakz.pmud.common.objects.Location;
import org.freakz.pmud.common.objects.Mobile;
import org.freakz.pmud.common.objects.PObject;
import org.freakz.pmud.common.objects.Zone;
import org.freakz.pmud.pmudserver.World.World;
import org.freakz.pmud.pmudserver.data.CDirtDataParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.*;

import static org.freakz.pmud.common.objects.Mobile.PSex.FEMALE;

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
        zoneSet.add("xlimbo.zone");
        zoneSet.add("icecave.zone");
//        zoneSet.add("village.zone");
        return zoneSet;
    }

    @Override
    public void run(String... args) throws Exception {
        reloadWorld();
    }

    public void reloadWorld() {
        try {
            log.debug("Start world init!");

            world.init();

//            Set<String> files = getZoneSet(); //dataParser.getFiles(ZONES_DIR, 1, ".zone");
            Set<String> files = dataParser.getFiles(ZONES_DIR, 1, ".zone");
            Map<String, CDirtDataParser.ParsedZone> parsedZoneMap = dataParser.parseZoneFiles(files, ZONES_DIR);
            createWorld(parsedZoneMap);
            mapLocationExits();

            log.debug("Zones    : {}", world.getZoneCount());
            log.debug("Locations: {}", world.getLocationCount());
            log.debug("Mobiles  : {}", world.getMobileCount());
            log.debug("Objects  : {}", world.getObjectCount());

            world.snapshotResetFields();
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
                    String linkedObject = exitToName.replaceAll("\\^", "");
                    List<PObject> objects = world.findPObjects(linkedObject);
                    if (objects.size() == 1) {
                        PObject linkedToObj = objects.get(0);
                        toMap.setLinkedExit(dir, linkedToObj);
                    } else {
                        log.error("No LINKED exit location found {} / {} dir {} -> {}", toMap.getZone().getName(), toMap.getName(), dir, exitToName);
                    }

                } else {
                    Location exitToLocation = null;
                    if (exitToName.contains("@")) {
                        exitToLocation = world.findLocationByNameAtZone(exitToName);
                    } else {
                        exitToLocation = zone.findLocationByName(exitToName);
                        if (exitToLocation == null) {
                            int foo = 0;
                        }
                    }
                    if (exitToLocation != null) {
                        toMap.setExit(dir, exitToLocation);
//                        toMap.getExitsMap().put(dir, exitToLocation);

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

            int countL = createLocations(parsedZone, zone);
            log.debug("Zone: {} with {} Locations", zone.getName(), countL);
        }

        for (CDirtDataParser.ParsedZone parsedZone : parsedZoneMap.values()) {
            Zone zone = world.getZone(parsedZone.name);
            int countM = createMobiles(parsedZone, zone);
            log.debug("Zone: {} with {} Mobiles", zone.getName(), countM);

            int countO = createObjects(parsedZone, zone);
            log.debug("Zone: {} with {} Objects", zone.getName(), countO);
        }

    }

    class LinkedObject {
        String to;
        PObject from;
    }

    private int createObjects(CDirtDataParser.ParsedZone parsedZone, Zone zone) {
        int count = 0;
        if (parsedZone.objects == null) {
            return count;
        }
        Map<String, LinkedObject> linkedObjectMap = new HashMap<>();

        for (String objectLines : parsedZone.objects) {
            Iterator<String> iterator = getIterator(objectLines);
            Map<String, String> valuesMap = mapToValues(iterator, zone);

            if (valuesMap.size() > 0) {
                PObject object = new PObject(zone);
                object.setName(valuesMap.get("name"));
                object.setpName(valuesMap.get("pname"));
                object.setAltName(valuesMap.get("altname"));

                object.setSize(parseInt(valuesMap.get("size"), 0));
                object.setBaseValue(parseInt(valuesMap.get("bvalue"), 0));
                object.setWeight(parseInt(valuesMap.get("weight"), 0));
                object.setDamage(parseInt(valuesMap.get("damage"), 0));

                object.setState(parseInt(valuesMap.get("state"), 0));
                object.setMaxState(parseInt(valuesMap.get("maxstate"), 0));

                object.setoFlags(valuesMap.get("oflags"));
                object.flagsToProperties();

                object.setDescription(0, parseTrim(valuesMap.get("desc[0]")));
                object.setDescription(1, parseTrim(valuesMap.get("desc[1]")));
                object.setDescription(2, parseTrim(valuesMap.get("desc[2]")));
                object.setDescription(3, parseTrim(valuesMap.get("desc[3]")));

                object.setExamine(parseTrim(valuesMap.get("examine")));

                String locationName = valuesMap.get("location");
                Location location = null;
                if (locationName.startsWith("IN_ROOM:") || locationName.matches(".?@.?")) {
                    locationName = locationName.replaceFirst("IN_ROOM:", "");

                    if (locationName.contains("@")) {
                        location = world.findLocationByNameAtZone(locationName);
                    } else {
                        location = zone.findLocationByName(locationName);
                    }
                    if (location != null) {
                        location.addObject(object);
                        String linkedTo = valuesMap.get("linked");
                        if (linkedTo != null) {
                            if (linkedObjectMap.get(linkedTo) == null) {
                                LinkedObject linkedObject = new LinkedObject();
                                linkedObject.to = linkedTo;
                                linkedObject.from = object;
                                linkedObjectMap.put(linkedTo, linkedObject);
                            }
                        }
                    }
                } else if (locationName.startsWith("BOTH_BY:")) {
                    locationName = locationName.replaceFirst("BOTH_BY:", "");
                    Mobile carrier = zone.findMobileByName(locationName);
                    if (carrier != null) {
                        carrier.addCarried(object);
                        carrier.addWorn(object);
                        carrier.setWielded(object);
                        location = carrier.getLocation();
                    } else {
                        log.error("No wielded_by for Object: {} / {} - {}", zone.getName(), object.getName(), locationName);
                    }
                } else if (locationName.startsWith("WIELDED_BY:")) {
                    locationName = locationName.replaceFirst("WIELDED_BY:", "");
                    Mobile carrier = zone.findMobileByName(locationName);
                    if (carrier != null) {
                        carrier.addCarried(object);
                        carrier.setWielded(object);
                        location = carrier.getLocation();
                    } else {
                        log.error("No wielded_by for Object: {} / {} - {}", zone.getName(), object.getName(), locationName);
                    }

                } else if (locationName.startsWith("WORN_BY:")) {
                    locationName = locationName.replaceFirst("WORN_BY:", "");
                    Mobile carrier = zone.findMobileByName(locationName);
                    if (carrier != null) {
                        carrier.addCarried(object);
                        carrier.addWorn(object);
                        location = carrier.getLocation();
                    } else {
                        log.error("No worn_by for Object: {} / {} - {}", zone.getName(), object.getName(), locationName);
                    }

                } else if (locationName.startsWith("CARRIED_BY:")) {
                    locationName = locationName.replaceFirst("CARRIED_BY:", "");
                    Mobile carrier = zone.findMobileByName(locationName);
                    if (carrier != null) {
                        carrier.addCarried(object);
                        location = carrier.getLocation();
                    } else {
                        log.error("No carrier for Object: {} / {} - {}", zone.getName(), object.getName(), locationName);
                    }


                } else if (locationName.startsWith("IN_CONTAINER:")) {
                    locationName = locationName.replaceFirst("IN_CONTAINER:", "");
                    PObject container = zone.findObjectByName(locationName);
                    if (container != null) {
                        container.addContains(object);
                        location = container.getLocation();
                    } else {
                        log.error("No container for Object: {} / {} - {}", zone.getName(), object.getName(), locationName);
                    }
                }
                if (location != null) {
                    object.setLocation(location);
                    zone.addObject(object);
                    world.addObject(object);
                    count++;
                } else {
                    log.error("No location for Object: {} / {} - {}", zone.getName(), object.getName(), locationName);
                }
            }
        }
        for (LinkedObject linkedObject : linkedObjectMap.values()) {
            PObject linkTarget = zone.findObjectByName(linkedObject.to);
            if (linkTarget != null) {
                linkedObject.from.setLinkedTo(linkTarget);
                linkTarget.setState(linkedObject.from.getState());
                linkTarget.setMaxState(linkedObject.from.getMaxState());
                linkTarget.setLinkedTo(linkedObject.from);
            } else {
                log.error("No link target: {} -> {}", linkedObject.from.getName(), linkedObject.to);
            }
        }
        return count;
    }

    private int createMobiles(CDirtDataParser.ParsedZone parsedZone, Zone zone) {
        int count = 0;
        if (parsedZone.mobiles == null) {
            return count;
        }

        for (String mobileLines : parsedZone.mobiles) {
            Iterator<String> iterator = getIterator(mobileLines);

            Map<String, String> valuesMap = mapToValues(iterator, zone);

            if (valuesMap.size() > 0) {
                Mobile mobile = new Mobile(zone);
                mobile.setName(parseTrim(valuesMap.get("name")));
                mobile.setpName(parseTrim(valuesMap.get("pname")));
                mobile.setDescription(parseTrim(valuesMap.get("description")));
                mobile.setExamine(parseTrim(valuesMap.get("examine")));

                mobile.setMaxStrength(parseInt(valuesMap.get("strength"), 0));
                mobile.setStrength(mobile.getMaxStrength());

                mobile.setDamage(parseInt(valuesMap.get("damage"), 0));
                mobile.setArmor(parseInt(valuesMap.get("armor"), 0));
                mobile.setAggression(parseInt(valuesMap.get("aggression"), 0));
                mobile.setSpeed(parseInt(valuesMap.get("speed"), 0));
                String sflags = valuesMap.get("sflags");
                if (sflags != null) {
                    if (sflags.toLowerCase().contains("female")) {
                        mobile.setSex(FEMALE);
                    }
                }
                String locationName = valuesMap.get("location");
                Location location;
                if (locationName.contains("@")) {
                    location = world.findLocationByNameAtZone(locationName);
                } else {
                    location = zone.findLocationByName(locationName);
                }

                if (location != null) {
                    mobile.setLocation(location);
                    mobile.setStartLocation(location);
                    location.addMobile(mobile);
                    zone.addMobile(mobile);
                    world.addMobile(mobile);
                    count++;
                } else {
                    log.error("No location for mobile: {} / {} - {}", zone.getName(), mobile.getName(), locationName);
                }

            }

        }
        return count;
    }

    private Map<String, String> mapToValues(Iterator<String> iterator, Zone zone) {
        Map<String, String> valuesMap = new HashMap<>();

        while (iterator.hasNext()) {
            String line = iterator.next().replaceAll("\t", "");
            String[] split = line.split("=");

            if (split.length == 2 && split[1].trim().equals("\"")) {
                String key = split[0].toLowerCase();
                String value = "";
                while (iterator.hasNext()) {
                    line = iterator.next();
                    value += line.replaceAll("\"", "");
                    if (line.contains("\"")) {
                        break;
                    }
                    value += "\n";
                }
                valuesMap.put(key.trim(), value);
            } else if (split.length == 2 && split[1].trim().startsWith("\"") && split[1].endsWith("\"")) {
                valuesMap.put(split[0].trim().toLowerCase(), split[1].replaceAll("\"", ""));
            } else if (!line.contains("\"") && line.contains("=")) {
                if (split.length == 1) {
                    log.error("Invalid mobile: {} - {}", zone.getName(), line);
                    valuesMap.clear();
                    break;
                }
                valuesMap.put(split[0].trim().toLowerCase(), split[1].trim());
            } else if (line.contains("{")) {
                String[] strs = line.replaceAll("[{}]", "").split(" ");
                String key = null;
                String values = "";
                for (int i = 0; i < strs.length; i++) {
                    String trim = strs[i].trim();
                    if (i == 0) {
                        key = trim.toLowerCase();
                    } else {
                        values += trim + " ";
                    }
                }
                if (key != null) {
                    valuesMap.put(key, values);
                }
            }
        }
        return valuesMap;
    }

    private String parseTrim(String value) {
        if (value == null) {
            return null;
        }
        return value.trim();
    }

    private int parseInt(String val, int def) {
        try {
            return Integer.parseInt(val);
        } catch (NumberFormatException e) {
            return def;
        }
    }

    private int createLocations(CDirtDataParser.ParsedZone parsedZone, Zone zone) {
        int count = 0;
        if (parsedZone.locations == null) {
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

                        if (line.contains("lflags")) {
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
        String flags = line.replaceAll("lflags|\\{|}", "");
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
                    if (split.contains("^")) {
                        location.addRawExit(split, true);
                    } else {
                        location.addRawExit(split);
                    }
                } else {
                    log.warn("Unknown exit: {} / {} - {}", location.getZone().getName(), location.getName(), split);
                }
            }
        } else {
            location.setName(line.trim());
        }
        return location;
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
