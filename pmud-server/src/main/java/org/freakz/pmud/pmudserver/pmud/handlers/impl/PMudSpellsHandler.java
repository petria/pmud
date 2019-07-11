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

    @AcceptVerbs(verbs = {"summon", "translocate"})
    public void handleSummonSpell(VerbRequest req, VerbResponse resp) {
        if (!hasArgs(req)) {
            resp.setToSender("Summon who?\n");
            return;
        }
        List<PObject> ol = world.findObjects(args(req));
        PObject o = world.findClosestObject(player(req), args(req));
        if (o != null) {
            Location oldL = o.location();
            String oldW = o.where();
            resp.setFromRoomF(o.getLocation(), "The %s umbrella vanishes!\n", o.name());
            world.playerSummonObject(player(req), o);
            resp.setToRoomF(o.getLocation(), "%s fetches something from another dimension.\n", playerName(req));

            resp.setToSenderF("The %s flies into your hand.\nIt was: %-25s | %s\n", o.name(), oldW, oldL.getName2());
        }
    }

    @AcceptVerbs(verbs = {"where"})
    public void handleWhereSpell(VerbRequest req, VerbResponse resp) {
        String toFind = args(req);
        PMudPlayer f = world.findPlayer(toFind);
        if (f != null) {
            resp.setToSender(String.format("[%5d]%25s - %-30s| %s", f.getId(), f.getName(), f.getLocation().getTitle(), f.getLocation().getName2()));
            return;
        }
        List<PObject> f2 = world.findObjects(toFind);
        if (f2.size() > 0) {
            String msg = "";
            for (PObject o : f2) {
                msg += String.format("[%5d]%25s - %-30s| %s\n", o.getId(), o.name(), o.where(), o.location().getName2());
            }
            resp.setToSender(msg);
            return;
        }
        List<Mobile> f3 = world.findMobiles(toFind);
        if (f3.size() > 0) {
            String msg = "";
            for (Mobile o : f3) {
                msg += String.format("[%5d]%25s - %-30s| %s\n", o.getId(), o.getName(), o.getLocation().getTitle(), o.getLocation().getName2());
            }
            resp.setToSender(msg);
            return;
        }

        resp.setToSender("I don't know what that is.\n");
    }

}
