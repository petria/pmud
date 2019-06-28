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
        public String name;

        public List<String> mobiles;
        public List<String> objects;
        public List<String> locations;
    }

    private String zonesDir = "c-dirt/data/ZONES/";

    public void setZonesDir(String zonesDir) {
        this.zonesDir = zonesDir;
    }


    public Set<String> getFiles(String dir, int depth, String endsWith) throws IOException {
        try (Stream<Path> stream = Files.walk(Paths.get(dir), depth)) {
            return stream
                    .filter(file -> !Files.isDirectory(file))
                    .filter(file -> file.toString().endsWith(endsWith))
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .collect(Collectors.toSet());
        }
    }

    public Map<String, ParsedZone> parseZoneFiles(Set<String> files, String zoneDir) throws IOException {
        Map<String, ParsedZone> parsedZoneMap = new HashMap<>();
        for (String zoneFile : files) {
            try {
                String zoneFilePath = zoneDir + zoneFile;
                List<String> lines = Files.readAllLines(Path.of(zoneFilePath));
                ParsedZone parsedZone = parseOneZone(lines);
                if (parsedZone != null) {
                    parsedZone.name = zoneFile.replaceFirst(zonesDir, "");
                    parsedZoneMap.put(parsedZone.name, parsedZone);
                } else {
                    log.error("Zone file not parsed: {}", zoneFile);
                }
            } catch (Exception e) {
                log.error("Zone file not parsed: {}", zoneFile);
                //e.printStackTrace();
            }
        }
        return parsedZoneMap;
    }

    public Map<String, ParsedZone> parseZoneFiles(Set<String> files) throws IOException {
        return parseZoneFiles(files, zonesDir);
    }

    private ParsedZone parseOneZone(List<String> lines) {
        Iterator<String> iterator = lines.iterator();
        String line = findLine(iterator, "%mobiles");
        if (line != null) {
            ParsedZone parsedZone = new ParsedZone();
            parsedZone.mobiles = parseSection(iterator, "%objects", "mobiles");
            parsedZone.objects = parseSection(iterator, "%locations", "objects");
            parsedZone.locations = parseLocations(iterator);
            return parsedZone;
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
//            log.debug("{} line: {}", section, line);

        }
        return sectionLines;
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
