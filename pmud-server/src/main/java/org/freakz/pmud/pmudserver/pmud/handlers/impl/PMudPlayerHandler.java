package org.freakz.pmud.pmudserver.pmud.handlers.impl;

import org.freakz.pmud.common.objects.PMudPlayer;
import org.freakz.pmud.common.objects.PObject;
import org.freakz.pmud.common.player.Level;
import org.freakz.pmud.pmudserver.pmud.VerbRequest;
import org.freakz.pmud.pmudserver.pmud.VerbResponse;
import org.freakz.pmud.pmudserver.pmud.handlers.AcceptVerbs;
import org.freakz.pmud.pmudserver.pmud.handlers.PMudVerbAcceptor;
import org.springframework.stereotype.Component;

@Component
@PMudVerbAcceptor
public class PMudPlayerHandler extends HandlerBase {


    @AcceptVerbs(verbs = {"drink"})
    public void handleDrink(VerbRequest req, VerbResponse resp) {
        if (!hasArgs(req)) {
            resp.setToSender("What's that?\n");
            return;
        }
        PMudPlayer p = player(req);
        PObject o = world.findClosestObject(player(req), args(req));
        if (o != null) {
            if (o.getZone().getName().equals("icecave") && o.name().equals("fountain")) {
                if (p.getLevelNum() >= 3) {
                    resp.setToSender("Faintly magical by the taste.\n");
                    return;
                }
                Level level = world.addPlayerScore(p, 40);
                String msg = "";
                if (level != null) {
                    msg += String.format("You are now %s the %s\n", p.getName(), p.getTitle());
                }
                msg += "You feel a wave of energy sweeping through you.\n";
                resp.setToSender(msg);
                return;
            }
        }
    }
}