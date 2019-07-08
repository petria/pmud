package org.freakz.pmud.pmudserver.pmud.handlers.impl;

import org.freakz.pmud.common.objects.Location;
import org.freakz.pmud.common.objects.Mobile;
import org.freakz.pmud.common.objects.PMudPlayer;
import org.freakz.pmud.common.objects.PObject;
import org.freakz.pmud.pmudserver.pmud.VerbRequest;
import org.freakz.pmud.pmudserver.pmud.VerbResponse;
import org.freakz.pmud.pmudserver.pmud.handlers.AcceptVerbs;
import org.freakz.pmud.pmudserver.pmud.handlers.PMudVerbAcceptor;
import org.springframework.stereotype.Component;

@Component
@PMudVerbAcceptor
public class PMudLookHandler {

    @AcceptVerbs(verbs = {"look", "l"})
    public void handleLook(VerbRequest request, VerbResponse response) {
        PMudPlayer player = request.getPlayer();
        Location location = player.getLocation();
        String msg = "\n";

        msg += String.format("[ID: %05d] %s [%s@%s]\n", location.getId(), location.getName2(), location.getName(), location.getZone().getName());

        msg += location.getTitle() + "\n";
        if (location.getLocationFlags().size() > 0) {
            for (String flag : location.getLocationFlags()) {
                msg += String.format("[%s] ", flag.toUpperCase());
            }
        }
        msg += "\n" + location.getDescription();

        for (PObject object : location.getObjects().values()) {
            String description = object.getDescription(object.getState());
            if (description == null) {
                msg += "<marker>" + object.getpName() + "\n";

            } else {
                msg += object.getDescription(object.getState()) + "\n";
            }
        }

        for (Mobile mobile : location.getMobiles().values()) {
            msg += mobile.getDescription() + "\n";
            if (mobile.getCarried().values().size() > 0) {
                msg += " " + mobile.getSex().textB() + " is carrying: ";
                for (PObject carrying : mobile.getCarried().values()) {
                    msg += carrying.getName() + " ";
                }
                msg += "\n";
            }
        }

        for (PMudPlayer plr : location.getPlayers().values()) {
            if (player == plr) {
                continue;
            }
            msg += plr.getName() + " is " + plr.getPosition().text() + " here. \n";
        }

        msg += "\nObvious exits are:\n";
        if (location.getExitsMap().size() > 0) {
            for (Location.Exits exit : Location.Exits.values()) {
                Location l = location.getExitsMap().get(exit.getDir());
                if (l != null) {
                    msg += String.format(" %6s : %-45s : %s\n", exit.getNice(), l.getTitle(), l.getName2());
                }
            }

        } else {
            msg += "None....\n";
        }
        response.setToSender(msg);

    }

}