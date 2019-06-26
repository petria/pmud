package org.freakz.pmud.pmudserver.data;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Handles reading C-Dirt data files for following
 * <p>
 * A:actions
 * Z:zones
 * L:locations
 * C:mobiles
 * E:levels
 * H:hours
 * I:intermud
 * M:messages
 * O:objects
 * P:pflags
 * V:verbs
 * W:wizlist
 */
@Service
@Slf4j
public class CDirtDataParser {

    public class ParsedZone {
        String name;

        List<String> mobiles;
        List<String> objects;
        List<String> locations;


    }

    private static final String ZONES_DIR = "c-dirt/data/ZONES/";

    public Set<String> getFiles(String dir, int depth, String endsWith) throws IOException {
        try (Stream<Path> stream = Files.walk(Paths.get(dir), depth)) {
            return stream
                    .filter(file -> !Files.isDirectory(file))
                    .filter(file -> !file.endsWith(endsWith))
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .collect(Collectors.toSet());
        }
    }

    //    @Override
    public void run(String... args) throws Exception {
//        Set<String> files = getFiles(ZONES_DIR, 1, ".zone");
//        parseZoneFiles(files);
    }

    public Map<String, ParsedZone> parseZoneFiles(Set<String> files) throws IOException {
        Map<String, ParsedZone> parsedZoneMap = new HashMap<>();
        for (String zoneFile : files) {
            List<String> lines = Files.readAllLines(Path.of(zoneFile));
            ParsedZone parsedZone = parseOneZone(lines);
            if (parsedZone != null) {
                parsedZone.name = zoneFile.replaceFirst("c-dirt/data/ZONES/", "");
                parsedZoneMap.put(parsedZone.name, parsedZone);
            }
        }
        return parsedZoneMap;
    }

    private ParsedZone parseOneZone(List<String> lines) {
        Iterator<String> iterator = lines.iterator();
        while (true) {
            String line = findLine(iterator, "%mobiles");
            if (line != null) {
                ParsedZone parsedZone = new ParsedZone();
                parsedZone.mobiles = parseSection(iterator, "%objects", "mobiles");
                parsedZone.objects = parseSection(iterator, "%locations", "mobiles");
                parsedZone.locations = parseLocations(iterator);
                return parsedZone;
            }
            break;
        }
        return null;
    }

    private List<String> parseLocations(Iterator<String> iterator) {
        List<String> sectionLines = new ArrayList<>();
        StringBuffer sb = new StringBuffer();
        while (iterator.hasNext()) {
            String line = iterator.next();
            if (line.isEmpty()) {
                continue;
            }
            if (line.equals("^")) {
                sectionLines.add(sb.toString());
                sb = new StringBuffer();
            } else {
                sb.append(line).append("\n");
            }
        }
        return sectionLines;
    }

    private List<String> parseSection(Iterator<String> iterator, String until, String section) {
        List<String> sectionLines = new ArrayList<>();
        StringBuffer sb = new StringBuffer();
        boolean inSection = false;
        while (iterator.hasNext()) {
            String line = iterator.next();
            if (line.equals(until)) {
                break;
            }
            if (!inSection) {
                if (!line.isEmpty()) {
                    inSection = true;
                    sb.append(line).append("\n");
                }
            } else {
                if (line.isEmpty()) {
                    inSection = false;
                    sectionLines.add(sb.toString());
                    sb = new StringBuffer();
                } else {
                    sb.append(line).append("\n");
                }
            }
            log.debug("{} line: {}", section, line);

        }
        return sectionLines;
    }

    private void getLinesFromUntil(Iterator<String> iterator, String from, String until) {

    }

    private String findLine(Iterator<String> iterator, String match) {
        while (iterator.hasNext()) {
            String line = iterator.next();
            if (line.matches(match)) {
                return line;
            }
        }
        return null;
    }


}
