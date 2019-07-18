package org.freakz.pmud.pmudserver.pmud.handlers.impl;

import org.freakz.pmud.common.enums.Exits;
import org.freakz.pmud.common.objects.Location;
import org.freakz.pmud.common.objects.Mobile;
import org.freakz.pmud.common.objects.PMudPlayer;
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

        if (player(req).isSitting()) {
            response.setToSender("You'll have to stand up, first.\n");
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

                toGo = world.getLocationByZoneAndNum(args(req));
                if (toGo == null) {

                    toGo = world.isMobileLocation(args(req));
                    if (toGo == null) {
                        toGo = world.isPlayerLocation(args(req));
                        if (toGo == null) {
                            toGo = world.getLocationByName2(args(req));
                        }
                    }
                }
            }

        } else {
            toGo = req.getPlayer().getStartLocation();
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

    @AcceptVerbs(verbs = {"sit"})
    public void handleSit(VerbRequest req, VerbResponse resp) {
        PMudPlayer p = player(req);
        if (p.getPosition() == Mobile.PPosition.SITTNG) {
            resp.setToSender("You're already sitting.\n");
            return;
        }
        p.setPosition(Mobile.PPosition.SITTNG);

        resp.setToSender("You assume the lotus position.\n");
        resp.setToRoomF(p.getLocation(), "%s sits down.\n", p.getName());

    }

    @AcceptVerbs(verbs = {"stand"})
    public void handleStand(VerbRequest req, VerbResponse resp) {
        PMudPlayer p = player(req);
        if (p.getPosition() == Mobile.PPosition.STANDING) {
            resp.setToSender("You're already standing.\n");
            return;
        }
        p.setPosition(Mobile.PPosition.STANDING);

        resp.setToSender("You clamber to your feet.\n");
        resp.setToRoomF(p.getLocation(), "%s clambers to %s feet.\n", p.getName(), hisOrHer(p));

    }


}