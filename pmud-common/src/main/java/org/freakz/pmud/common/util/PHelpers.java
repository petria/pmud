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

}
