package org.freakz.pmud.pmudserver.pmud.handlers.impl;

import org.freakz.pmud.common.objects.Location;
import org.freakz.pmud.pmudserver.World.World;
import org.freakz.pmud.pmudserver.pmud.VerbRequest;
import org.freakz.pmud.pmudserver.pmud.VerbResponse;
import org.freakz.pmud.pmudserver.pmud.handlers.AcceptVerbs;
import org.freakz.pmud.pmudserver.pmud.handlers.PMudVerbAcceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.freakz.pmud.common.objects.Location.Exits.NONE;
import static org.freakz.pmud.common.objects.Location.Exits.getExit;

@Component
@PMudVerbAcceptor
public class PMudMovementsHandler extends HandlerBase {

    @Autowired
    private World world;

    @AcceptVerbs(verbs = {"go", "north", "n", "east", "e", "south", "s", "west", "w", "up", "u", "down", "d"})
    public void handleMove(VerbRequest req, VerbResponse response) {
        String dir = req.getArgs().getCmd();
        if (dir.equals("go")) {
            dir = req.getArgs().getArgs();
        }
        Location.Exits exit = getExit(dir);
        Location toGo = req.getPlayer().getLocation().getExit(exit);
        if (exit == NONE || toGo == null) {
            response.setToSender("You can't go that way.");
            return;
        }

        response.setFromRoom(location(req), playerName(req) + " goes to " + exit.getNice());
        world.playerToNewLocation(player(req), location(req), toGo);

        response.setToRoom(toGo, playerName(req) + " has arrived.");
        response.setToSender(look(req));

    }



    @AcceptVerbs(verbs = {"goto"})
    public void handleGoto(VerbRequest req, VerbResponse response) {

        Location toGo = world.getLocationByName2(req.getArgs().getArgs());
        if (toGo == null) {
            response.setToSender("Unknown player, object or room.");
        } else {

            response.setFromRoom(location(req), playerName(req) + " vanishes in a puff of smoke.");
            world.playerToNewLocation(player(req), location(req), toGo);

            response.setToSender(look(req));
            response.setToRoom(location(req), playerName(req) + " appears with an ear-splitting bang.\n");

        }
    }


}
