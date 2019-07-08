package org.freakz.pmud.pmudserver.pmud.handlers.impl;

import org.freakz.pmud.common.objects.PMudPlayer;
import org.freakz.pmud.common.objects.PObject;
import org.freakz.pmud.pmudserver.World.World;
import org.freakz.pmud.pmudserver.pmud.VerbRequest;
import org.freakz.pmud.pmudserver.pmud.VerbResponse;
import org.freakz.pmud.pmudserver.pmud.handlers.AcceptVerbs;
import org.freakz.pmud.pmudserver.pmud.handlers.PMudVerbAcceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@PMudVerbAcceptor
public class PMudSpellsHandler extends HandlerBase {

    @Autowired
    private World world;


    @AcceptVerbs(verbs = {"where"})
    public void handleWhereSpell(VerbRequest req, VerbResponse response) {
        String toFind = args(req);
        PMudPlayer f = world.findPlayer(toFind);
        if (f != null) {
            response.setToSender(String.format("[%5d]%15s - %-30s| %s", f.getId(), f.getName(), f.getLocation().getTitle(), f.getLocation().getName2()));
            return;
        }
        List<PObject> f2 = world.findObjects(toFind);
        if (f2.size() > 0) {
            String msg = "";
            for (PObject o : f2) {
                msg += String.format("[%5d]%15s - %-30s| %s\n", o.getId(), "key", o.getLocation().getTitle(), o.getLocation().getName2());
            }
            response.setToSender(msg);
            return;
        }

        response.setToSender("I don't know what that is.");
    }

}
