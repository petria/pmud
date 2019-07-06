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

                List<String> lines2 = removeCommented(lines);

                List<MultiZone> multiZoneList = checkMultipleZones(lines2, zoneFile);

                for (MultiZone multiZone : multiZoneList) {
                    ParsedZone parsedZone = parseOneZone(multiZone);
                    if (parsedZone != null) {
                        parsedZone.name = multiZone.name;
                        parsedZoneMap.put(parsedZone.name, parsedZone);
                    } else {
                        log.error("Zone file not parsed: {}", zoneFile);
                    }

                }

            } catch (Exception e) {

                log.error("Zone file not parsed: {}", zoneFile, e);
                //e.printStackTrace();
            }
        }
        return parsedZoneMap;
    }

    private List<String> removeCommented(List<String> lines) {
        StringBuilder sb = new StringBuilder();
        boolean inComment = false;
        for (String line : lines) {
            String trim = line.trim();
            if (trim.startsWith("/*") && trim.endsWith("*/")) {
                continue;
            }
            if (trim.startsWith("/*") && !trim.contains("*/") && !inComment) {
                inComment = true;
                continue;
            }
            if (trim.endsWith("*/") && inComment) {
                inComment = false;
                continue;
            }
            if (!inComment) {
                sb.append(line).append("\n");
            }
        }

        return Arrays.asList(sb.toString().split("\n"));
    }

    class MultiZone {
        String name = null;
        boolean hasMobiles = false;
        boolean hasObjects = false;
        boolean hasLocations = false;
        List<String> lines = new ArrayList<>();
    }

    private List<MultiZone> checkMultipleZones(List<String> lines, String zoneFile) {
        List<MultiZone> multiZoneList = new ArrayList<>();

        boolean isMultiZone = false;

        for (String line : lines) {
            if (line.startsWith("%zone:")) {
                isMultiZone = true;
            }
        }

        if (isMultiZone) {
            MultiZone multiZone = new MultiZone();
            multiZoneList.add(multiZone);
            for (String line : lines) {
                if (line.startsWith("%zone:")) {
                    if (multiZone.name == null) {
                        multiZone.name = line.replaceFirst("%zone:", "");
                    } else {
                        multiZone = new MultiZone();
                        multiZone.name = line.replaceFirst("%zone:", "");
                        multiZoneList.add(multiZone);
                    }
                } else {
                    if (line.toLowerCase().contains("%mobiles")) {
                        multiZone.hasMobiles = true;
                    }
                    if (line.toLowerCase().contains("%objects")) {
                        multiZone.hasObjects = true;
                    }
                    if (line.toLowerCase().contains("%locations")) {
                        multiZone.hasLocations = true;
                    }

                    multiZone.lines.add(line);
                }
            }
        } else {
            MultiZone multiZone = new MultiZone();
            multiZone.name = zoneFile.split("\\.")[0];
            for (String line : lines) {
                if (line.toLowerCase().contains("%mobiles")) {
                    multiZone.hasMobiles = true;
                }
                if (line.toLowerCase().contains("%objects")) {
                    multiZone.hasObjects = true;
                }
                if (line.toLowerCase().contains("%locations")) {
                    multiZone.hasLocations = true;
                }
                multiZone.lines.add(line);
            }
            multiZoneList.add(multiZone);
        }


        return multiZoneList;
    }

    public Map<String, ParsedZone> parseZoneFiles(Set<String> files) throws IOException {
        return parseZoneFiles(files, zonesDir);
    }

    private ParsedZone parseOneZone(MultiZone multiZone) {
        List<String> lines = multiZone.lines;
        Iterator<String> iterator = lines.iterator();
        ParsedZone parsedZone = new ParsedZone();
        if (multiZone.hasMobiles) {
            parsedZone.mobiles = parseSection(iterator, "%objects");
        }
        if (multiZone.hasObjects) {
            parsedZone.objects = parseSection(iterator, "%locations");
        }
        if (multiZone.hasLocations) {
            parsedZone.locations = parseLocations(iterator);
        }
        return parsedZone;
    }

    private List<String> parseLocations(Iterator<String> iterator) {
        List<String> sectionLines = new ArrayList<>();
        StringBuffer sb = new StringBuffer();
        while (iterator.hasNext()) {
            String line = iterator.next();
            if (line.isEmpty() || line.isBlank() || line.startsWith("%") || line.startsWith("#include")) {
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

    private List<String> parseSection(Iterator<String> iterator, String until) {
        List<String> sectionLines = new ArrayList<>();
        StringBuffer sb = new StringBuffer();
        boolean inSection = false;
        while (iterator.hasNext()) {
            String line = iterator.next();
            if (line.toLowerCase().equals(until)) {
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

        }
        return sectionLines;
    }

}
