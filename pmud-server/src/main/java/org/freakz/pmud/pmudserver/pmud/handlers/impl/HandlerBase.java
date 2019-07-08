package org.freakz.pmud.pmudserver.pmud.handlers.impl;

import org.freakz.pmud.common.objects.Location;
import org.freakz.pmud.common.objects.PMudPlayer;
import org.freakz.pmud.pmudserver.pmud.CommandHandlerService;
import org.freakz.pmud.pmudserver.pmud.VerbRequest;
import org.freakz.pmud.pmudserver.pmud.VerbResponse;
import org.freakz.pmud.pmudserver.pmud.handlers.PMudVerbHandler;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class HandlerBase implements PMudVerbHandler {

    @Autowired
    protected CommandHandlerService commandHandlerService;


    VerbResponse invokeVerb(String verb, PMudPlayer player) {
        return commandHandlerService.invokeVerb(verb, player);
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


}
