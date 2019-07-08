package org.freakz.pmud.pmudserver.pmud.handlers.impl;

import org.freakz.pmud.pmudserver.pmud.VerbRequest;
import org.freakz.pmud.pmudserver.pmud.VerbResponse;
import org.freakz.pmud.pmudserver.pmud.handlers.AcceptVerbs;
import org.freakz.pmud.pmudserver.pmud.handlers.PMudVerbAcceptor;
import org.springframework.stereotype.Component;

@Component
@PMudVerbAcceptor
public class PMudCommunicationsHandler extends HandlerBase {


    @AcceptVerbs(verbs = {"shout"})
    public void handleShout(VerbRequest req, VerbResponse resp) {

        if (!hasArgs(req)) {
            resp.setToSender("What do you want to shout?\n");
            return;
        }

        resp.setToWorld(String.format("%s shouts, \"%s\"\n", playerName(req), args(req)));
        resp.setToSender("You shout, \"" + args(req) + "\"\n");

    }

}
