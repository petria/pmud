package org.freakz.pmud.pmudserver.pmud.handlers.impl;

import org.freakz.pmud.common.objects.Location;
import org.freakz.pmud.common.objects.PMudPlayer;
import org.freakz.pmud.pmudserver.World.World;
import org.freakz.pmud.pmudserver.pmud.VerbRequest;
import org.freakz.pmud.pmudserver.pmud.VerbResponse;
import org.freakz.pmud.pmudserver.pmud.handlers.AcceptVerbs;
import org.freakz.pmud.pmudserver.pmud.handlers.PMudVerbAcceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@PMudVerbAcceptor
public class PMudMovementHandlerBase extends HandlerBase {

    @Autowired
    private World world;

    @AcceptVerbs(verbs = {"go", "north", "east", "south", "west", "up", "down"})
    public void handleMove(VerbRequest request, VerbResponse response) {
        int fofof = 0;
    }

    @AcceptVerbs(verbs = {"goto"})
    public void handleGoto(VerbRequest request, VerbResponse response) {
        PMudPlayer player = request.getPlayer();

        Location newLocation = world.getLocationByName2(request.getArgs().getArgs());
        if (newLocation == null) {
            response.setToSender("Unknown player, object or room.");
        } else {
            player.setLocation(newLocation);
            response.setToSender(invokeVerb("look", player).getToSender());
            response.setFromRoom(player.getName() + " vanishes in a puff of smoke.");
            response.setToRoom(player.getName() + " appears with an ear-splitting bang.\n");

        }
    }


}
