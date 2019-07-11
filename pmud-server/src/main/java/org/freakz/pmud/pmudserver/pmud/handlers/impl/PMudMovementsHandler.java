package org.freakz.pmud.pmudserver.pmud.handlers.impl;

import org.freakz.pmud.common.enums.Exits;
import org.freakz.pmud.common.objects.Location;
import org.freakz.pmud.pmudserver.pmud.VerbRequest;
import org.freakz.pmud.pmudserver.pmud.VerbResponse;
import org.freakz.pmud.pmudserver.pmud.handlers.AcceptVerbs;
import org.freakz.pmud.pmudserver.pmud.handlers.PMudVerbAcceptor;
import org.springframework.stereotype.Component;

import static org.freakz.pmud.common.enums.Exits.NONE;
import static org.freakz.pmud.common.enums.Exits.getExit;

@Component
@PMudVerbAcceptor
public class PMudMovementsHandler extends HandlerBase {

    @AcceptVerbs(verbs = {"go", "north", "n", "east", "e", "south", "s", "west", "w", "up", "u", "down", "d"})
    public void handleMove(VerbRequest req, VerbResponse response) {
        String dir = req.getArgs().getCmd();
        if (dir.equals("go")) {
            dir = req.getArgs().getArgs();
        }
        Exits exit = getExit(dir);
        Location toGo = req.getPlayer().getLocation().getExit(exit);
        if (exit == NONE || toGo == null) {
            response.setToSender("You can't go that way.\n");
            return;
        }

        response.setFromRoom(location(req), playerName(req) + " goes to " + exit.getNice() + "\n");
        world.playerToNewLocation(player(req), location(req), toGo);

        response.setToRoom(toGo, playerName(req) + " has arrived.\n");
        response.setToSender(look(req));

    }


    @AcceptVerbs(verbs = {"goto"})
    public void handleGoto(VerbRequest req, VerbResponse resp) {
        Location toGo;
        if (hasArgs(req)) {
            if (isNumericArg(req)) {
                int objId = Integer.parseInt(args(req));
                toGo = world.getLocationById(objId);
            } else {
                toGo = world.getLocationByName2(args(req));
            }

        } else {
            toGo = req.getPlayer().getHomeLocation();
        }

        if (toGo == null) {
            resp.setToSender("Unknown player, object or room.\n");
        } else {
            resp.setFromRoom(location(req), playerName(req) + " vanishes in a puff of smoke.\n");
            world.playerToNewLocation(player(req), location(req), toGo);

            resp.setToSender(look(req));
            resp.setToRoom(location(req), playerName(req) + " appears with an ear-splitting bang.\n");

        }


    }


}
