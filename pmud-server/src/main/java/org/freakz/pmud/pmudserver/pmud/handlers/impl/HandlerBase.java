package org.freakz.pmud.pmudserver.pmud.handlers.impl;

import org.freakz.pmud.common.objects.Location;
import org.freakz.pmud.common.objects.Mobile;
import org.freakz.pmud.common.objects.PMudPlayer;
import org.freakz.pmud.pmudserver.World.World;
import org.freakz.pmud.pmudserver.pmud.CommandHandlerService;
import org.freakz.pmud.pmudserver.pmud.ScoreAndLevelsService;
import org.freakz.pmud.pmudserver.pmud.VerbRequest;
import org.freakz.pmud.pmudserver.pmud.VerbResponse;
import org.freakz.pmud.pmudserver.pmud.handlers.PMudVerbHandler;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class HandlerBase implements PMudVerbHandler {

    @Autowired
    protected CommandHandlerService commandHandlerService;

    @Autowired
    World world;

    @Autowired
    ScoreAndLevelsService levelsService;

    VerbResponse invokeVerb(String verb, PMudPlayer player) {
        return commandHandlerService.invokeVerb(verb, player);
    }

    String args(VerbRequest req) {
        return req.getArgs().getArgs();
    }

    boolean isNumericArg(VerbRequest req) {
        String args = args(req);
        return args.matches("\\d*?");
    }

    int getNumericArg(VerbRequest req) {
        return Integer.parseInt(args(req));
    }

    boolean hasArgs(VerbRequest req) {
        return req.getArgs().hasArgs();
    }

    String playerName(VerbRequest request) {
        return request.getPlayer().getName();
    }

    PMudPlayer player(VerbRequest req) {
        return req.getPlayer();
    }

    Location location(VerbRequest req) {
        return req.getPlayer().getLocation();
    }

    String look(VerbRequest req) {
        return invokeVerb("look", player(req)).getToSender();
    }

    String hisOrHer(PMudPlayer p) {
        if (p.getSex() == Mobile.PSex.MALE) {
            return "his";
        } else {
            return "her";
        }
    }

}
