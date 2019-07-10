package org.freakz.pmud.pmudserver.pmud.handlers.impl;

import org.freakz.pmud.common.objects.Location;
import org.freakz.pmud.common.objects.PMudPlayer;
import org.freakz.pmud.common.objects.PObject;
import org.freakz.pmud.pmudserver.pmud.VerbRequest;
import org.freakz.pmud.pmudserver.pmud.VerbResponse;
import org.freakz.pmud.pmudserver.pmud.handlers.AcceptVerbs;
import org.freakz.pmud.pmudserver.pmud.handlers.PMudVerbAcceptor;
import org.springframework.stereotype.Component;

@Component
@PMudVerbAcceptor
public class PMudObjectsHandler extends HandlerBase {

    @AcceptVerbs(verbs = {"inventory"})
    public void handleInventory(VerbRequest req, VerbResponse resp) {
        PMudPlayer p = player(req);
        resp.setToRoomF(location(req), "%s rummages through his backpack.\n", playerName(req));
        if (p.getCarried().values().size() > 0) {
            String msg = "Your backpack contains:\n";
            for (PObject o : p.getCarried().values()) {
                msg += o.getName() + " ";
            }
            msg += "\n";
            resp.setToSender(msg);
        } else {
            resp.setToSender("You are carrying nothing.\n");
        }
    }

    @AcceptVerbs(verbs = {"drop"})
    public void handleDrop(VerbRequest req, VerbResponse resp) {
        if (!hasArgs(req)) {
            resp.setToSender("Drop what?\n");
            return;
        }
        PMudPlayer p = player(req);
        Location l = location(req);
        PObject o = p.isCarrying(args(req));
        if (o != null) {
            if (l.hasPit()) {
                world.playerDropObjectPit(p, l, o);
                resp.setToRoomF(l, "%s drops the %s into the pit.\n", playerName(req), o.getName());
                resp.setToSenderF("The %s disappears into the bottomless pit.....\n", o.getName());
                int value = world.getObjectValue(o);
                world.addPlayerScore(p, value);
            } else {
                world.playerDropObject(p, l, o);
                resp.setToRoomF(l, "%s drops %s\n", playerName(req), o.getName());
                resp.setToSender("Ok\n");
            }
        } else {
            resp.setToSender("What's that?");
        }
    }

    @AcceptVerbs(verbs = {"take"})
    public void handleTake(VerbRequest req, VerbResponse resp) {
        if (!hasArgs(req)) {
            resp.setToSender("Get what?\n");
            return;
        }
        Location l = location(req);
        PObject o = l.getObject(args(req));
        if (o != null) {
            world.playerTakeObject(player(req), l, o);
            resp.setToRoomF(l, "%s takes %s.\n", playerName(req), o.getName());
            resp.setToSenderF("You take %s.\n", o.getName());
        } else {
            resp.setToSender("What's a " + args(req) + "?\n");
        }
    }

}
