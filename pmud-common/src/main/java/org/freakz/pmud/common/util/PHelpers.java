package org.freakz.pmud.common.util;

import org.freakz.pmud.common.objects.PObject;

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

}
