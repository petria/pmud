package org.freakz.pmud.pmudserver.pmud.handlers.impl;

import org.freakz.pmud.common.objects.Mobile;
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

    @AcceptVerbs(verbs = {"flee"})
    public void handleFlee(VerbRequest req, VerbResponse resp) {

        PMudPlayer p = player(req);
        if (!p.isFighting()) {
            resp.setToSender("You are not fighting!\n");
            return;

        }

        p.getFightingTo().removeFightingTo();
        p.removeFightingTo();

        resp.setToSenderF("You flee from fight!!\n");

    }

    @AcceptVerbs(verbs = {"kill", "k"})
    public void handleKill(VerbRequest req, VerbResponse resp) {
        PMudPlayer p = player(req);
        if (p.isFighting()) {
            resp.setToSender("You are already fighting!\n");
            return;
        }

        if (!hasArgs(req)) {
            resp.setToSender("Kill who?\n");
            return;
        }

        Mobile m = p.getLocation().getMobile(args(req));
        if (m != null) {
            if (m.isFighting()) {
                resp.setToSenderF("%s are already fighting!\n", m.getName());
                return;

            }
            p.setFightingTo(m);
            m.setFightingTo(p);

            resp.setToSenderF("You charge into battle with %s.\n", m.getName());

        } else {
            resp.setToSenderF("%s is not here!\n", args(req));
        }

    }


    @AcceptVerbs(verbs = {"stats"})
    public void handleStats(VerbRequest req, VerbResponse resp) {
        if (!hasArgs(req)) {
            resp.setToSender("Stats who?\n");
            return;
        }
        Mobile m = world.getMobileOrPlayer(args(req));
        if (m != null) {
            String msg = "";

            msg += String.format("&+WName       &+C: &+w%s\n", m.name());

            resp.setToSender(msg);

        } else {
            resp.setToSender("Who's that?\n");

        }
    }
}