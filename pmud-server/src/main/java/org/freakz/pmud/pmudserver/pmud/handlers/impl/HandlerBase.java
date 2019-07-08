package org.freakz.pmud.pmudserver.pmud.handlers.impl;

import org.freakz.pmud.common.objects.PMudPlayer;
import org.freakz.pmud.pmudserver.pmud.CommandHandlerService;
import org.freakz.pmud.pmudserver.pmud.VerbResponse;
import org.freakz.pmud.pmudserver.pmud.handlers.PMudVerbHandler;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class HandlerBase implements PMudVerbHandler {

    @Autowired
    protected CommandHandlerService commandHandlerService;


    protected VerbResponse invokeVerb(String verb, PMudPlayer player) {
        return commandHandlerService.invokeVerb(verb, player);
    }

}
