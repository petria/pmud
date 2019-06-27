package org.freakz.pmud.pmudserver;

import org.freakz.pmud.pmudserver.data.CDirtDataParser;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PmudserverApplicationTests {

    private static final String ZONES_DIR = "../c-dirt/data/ZONES/";

    @Autowired
    CDirtDataParser dataParser;

    @Test
    public void testCDirtDataParserGetZoneFiles() throws IOException {
        Set<String> files = dataParser.getFiles(ZONES_DIR, 1, ".zone");
        Assert.assertEquals(75, files.size());
    }

    @Test
    public void testCDirtDataParserParseZoneFiles() throws IOException {

        dataParser.setZonesDir(ZONES_DIR);

        Set<String> fileSet = new HashSet<>();
        fileSet.add(ZONES_DIR + "abyss.zone");

        Map<String, CDirtDataParser.ParsedZone> parsedZoneMap = dataParser.parseZoneFiles(fileSet);

        Assert.assertEquals(1, parsedZoneMap.size());

        CDirtDataParser.ParsedZone zone = parsedZoneMap.values().iterator().next();

        Assert.assertEquals("abyss.zone", zone.name);

        Assert.assertEquals(20, zone.mobiles.size());
        Assert.assertEquals(61, zone.objects.size());
        Assert.assertEquals(86, zone.locations.size());

    }

}
