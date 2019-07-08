package org.freakz.pmud.pmudserver.pmud.handlers.impl;

import org.freakz.pmud.common.objects.PMudPlayer;
import org.freakz.pmud.pmudserver.pmud.VerbRequest;
import org.freakz.pmud.pmudserver.pmud.VerbResponse;
import org.freakz.pmud.pmudserver.pmud.handlers.AcceptVerbs;
import org.freakz.pmud.pmudserver.pmud.handlers.PMudVerbAcceptor;
import org.springframework.stereotype.Component;

@Component
@PMudVerbAcceptor
public class PMudInfoHandler extends HandlerBase {

    @AcceptVerbs(verbs = {"who"})
    public void handleWho(VerbRequest req, VerbResponse resp) {
        String msg = "";
        msg += "+-------------+---------------------------------------------------------------+\n";
        msg += "| Level       | Title                                                         |\n";
        msg += "I-------------I---------------------------------------------------------------I\n";
        for (PMudPlayer p : world.getNameToPlayerMap().values()) {
            msg += String.format("| %10s  | %-61s |\n", p.getLevel(), p.getName() + " " + p.getTitle());
        }
        msg += "I-------------+---------------------------------------------------------------I\n";
        msg += String.format("| There are currently %2s visible players on PMud                              |\n", world.playerCount());
        msg += "+-----------------------------------------------------------------------------+\n";

        resp.setToSender(msg);

    }

}
