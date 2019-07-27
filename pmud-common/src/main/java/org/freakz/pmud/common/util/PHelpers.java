package org.freakz.pmud.common.util;

import org.freakz.pmud.common.enums.Exits;
import org.freakz.pmud.common.objects.Location;
import org.freakz.pmud.common.objects.Mobile;
import org.freakz.pmud.common.objects.PObject;

import java.util.Random;

public class PHelpers {

    public static boolean matchToObject(PObject o, String toFind) {
        if (o.getName() != null) {
            if (o.getName().equalsIgnoreCase(toFind)) {
                return true;
            }
        }
        if (o.getpName() != null) {
            if (o.getpName().equalsIgnoreCase(toFind)) {
                return true;
            }
        }
        if (o.getAltName() != null) {
            if (o.getAltName().equalsIgnoreCase(toFind)) {
                return true;
            }
        }

        return false;
    }

    public static String capitalize(String str) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (i == 0) {
                sb.append(Character.toUpperCase(ch));
            } else {
                sb.append(Character.toLowerCase(ch));
            }
        }
        return sb.toString();
    }

    static Random r = new Random();

    public static int randperc() {
        int min = 0;
        int max = 100;
        return r.nextInt((max - min) + 1) + min;
    }

    public static int my_random() {
        return r.nextInt();
    }

    public static boolean matchToMobile(Mobile m, String name) {
        if (m.getName() != null) {
            if (m.getName().equalsIgnoreCase(name) || m.getName().startsWith(name)) {
                return true;
            }
        }
        if (m.getpName() != null) {
            if (m.getpName().equalsIgnoreCase(name) || m.getpName().startsWith(name)) {
                return true;
            }
        }
        return false;
    }

    public static String getQuitMsg(String message) {
        String msg = "";
        msg += "---PMudMUD-------------------------------------------------------------------\n\n";
        msg += message;
        msg += "-----------------------------------------------------------------------------\n";
        return msg;
    }

    public static Exits getRandomExit(Location l) {
        Random rnd = new Random();
        int size = l.getExitsMap().values().size();
        int i = rnd.nextInt(size);
        Location location = (Location) l.getExitsMap().values().toArray()[i];
        for (String key : l.getExitsMap().keySet()) {
            Location toGo = l.getExitsMap().get(key);
            if (toGo == location) {
                return Exits.getExit(key);
            }
        }
        return null;
    }
}
