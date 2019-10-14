package org.freakz.pmud.pmudserver.pmud.handlers.impl;

import org.freakz.pmud.common.objects.Location;
import org.freakz.pmud.common.objects.Mobile;
import org.freakz.pmud.common.objects.PMudPlayer;
import org.freakz.pmud.common.objects.PObject;
import org.freakz.pmud.pmudserver.pmud.VerbRequest;
import org.freakz.pmud.pmudserver.pmud.VerbResponse;
import org.freakz.pmud.pmudserver.pmud.handlers.AcceptVerbs;
import org.freakz.pmud.pmudserver.pmud.handlers.PMudVerbAcceptor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@PMudVerbAcceptor
public class PMudSpellsHandler extends HandlerBase {


    @AcceptVerbs(verbs = {"heal"})
    public void handleHeal(VerbRequest req, VerbResponse resp) {
        if (!hasArgs(req)) {
            resp.setToSender("Heal who?\n");
            return;
        }
        Mobile m = world.findPlayerOrMobile(argsOrMe(req));
        if (m != null) {
            setHimOrHer(req, m);
            m.setStrength(m.getMaxStrength());
            resp.setToSenderF("Power radiates from your hands to heal %s.\n", m.getName());
            resp.setToRoomF(m.getLocation(), "%s heals all %s wounds.", playerName(req), m.getName());
        } else {
            resp.setToSender("Heal who?\n");
        }

    }

    @AcceptVerbs(verbs = {"summon", "translocate"})
    public void handleSummonSpell(VerbRequest req, VerbResponse resp) {
        if (!hasArgs(req)) {
            resp.setToSender("Summon who?\n");
            return;
        }
        boolean ok = false;
        Mobile m = world.findPlayerOrMobile(args(req));
        if (m != null) {
            resp.setFromRoomF(m.getLocation(), "The %s vanishes in a puff of smoke!\n", m.name());
            if (m.isMobile()) {
                world.moveMobile(m, m.getLocation(), player(req).getLocation());
            } else {
                world.playerToNewLocation((PMudPlayer) m, m.getLocation(), player(req).getLocation());
            }
            resp.setToSender("You cast the summoning...\n");
            world.sendToLocationF(m.getLocation(), m, null, "%s appears with an ear-splitting bang.\n", m.name());
            ok = true;
        } else {

            PObject o = world.findClosestPObject(player(req), args(req));
            if (o != null) {
                setPn(req, o);
                Location oldL = o.location();
                String oldW = o.where();
                resp.setFromRoomF(o.getLocation(), "The %s vanishes!\n", o.name());
                world.playerSummonObject(player(req), o);
                resp.setToRoomF(o.getLocation(), "%s fetches something from another dimension.\n", playerName(req));
                resp.setToSenderF("The %s flies into your hand.\nIt was: %-25s | %s\n", o.name(), oldW, oldL.getName2());
                setIt(req, o);
                ok = true;
            }
        }
        if (!ok) {
            resp.setToSenderF("Who or what is %s?\n", args(req));
        }

    }

    @AcceptVerbs(verbs = {"where"})
    public void handleWhereSpell(VerbRequest req, VerbResponse resp) {
        String toFind = args(req);
        PMudPlayer f = world.findPlayer(toFind);
        if (f != null) {
            setPn(req, f);
            resp.setToSender(String.format("[%5d]%25s - %-30s| %s\n", f.getId(), f.getName(), f.getLocation().getTitle(), f.getLocation().getName2()));
            return;
        }
        List<PObject> f2 = world.findPObjects(toFind);
        if (f2.size() > 0) {
            String msg = "";
            for (PObject o : f2) {
                msg += String.format("[%5d]%25s - %-30s| %s\n", o.getId(), o.name(), o.where(), o.location().getName2());
                setPn(req, o);
            }
            resp.setToSender(msg);
            return;
        }
        List<Mobile> f3 = world.findMobiles(toFind);
        if (f3.size() > 0) {
            String msg = "";
            for (Mobile o : f3) {
                msg += String.format("[%5d]%25s - %-30s| %s\n", o.getId(), o.getName(), o.getLocation().getTitle(), o.getLocation().getName2());
                setPn(req, o);
            }
            resp.setToSender(msg);
            return;
        }

        resp.setToSender("I don't know what that is.\n");
    }

}
