package org.freakz.pmud.pmudserver.pmud;

import lombok.extern.slf4j.Slf4j;
import org.freakz.pmud.common.message.PMudMessage;
import org.freakz.pmud.common.objects.Location;
import org.freakz.pmud.common.objects.PMudPlayer;
import org.freakz.pmud.pmudserver.World.World;
import org.freakz.pmud.pmudserver.service.MessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;

@Service
@Slf4j
public class PMudEngine {


    @Autowired
    private CommandHandlerService commandHandlerService;

    @Autowired
    private MessageSender sender;

    @Autowired
    private World world;


    public void handlePMudMessage(PMudMessage pMudMessage) {

        if (pMudMessage.getMessage().equals("CONNECTED")) {
            PMudPlayer player = handleConnected(pMudMessage);
            sender.sendReply(player, commandHandlerService.invokeVerb("look", player).getToSender());
        } else if (pMudMessage.getMessage().equals("DISCONNECTED")) {

            log.debug("Player disconnected: {}", pMudMessage.getPlayer());
            world.removePlayer(pMudMessage.getPlayer());

        } else {
            PMudPlayer player = world.findPlayer(pMudMessage.getPlayer());
            if (player != null) {
                VerbRequest request = new VerbRequest(pMudMessage.getMessage(), player);
                VerbResponse response = new VerbResponse(player);
                boolean success = commandHandlerService.invokeVerbHandler(request, response);
                if (success) {
                    sender.sendReply(response);
                } else {
                    sender.sendReply(player, "Pardon?");
                }

            } else {
                log.error("No player!?");
            }
        }
    }


    private PMudPlayer handleConnected(PMudMessage message) {
        String playerName = message.getPlayer();
        PMudPlayer player = world.findPlayer(playerName);
        if (player == null) {
            player = new PMudPlayer(world.getZone("start"));
            player.setName(message.getPlayer());
            player.setPid(message.getPid());
            world.addPlayer(player);
        } else {
            player.getLocation().removePlayer(player);
        }

        Location start = world.getLocationByName2("blizzard70");
        player.setLocation(start);

        sendMessageToRoom(player, start, playerName + " entered game");

        start.addPlayer(player);

        return player;
    }

    private void sendMessageToRoom(PMudPlayer player, Location start, String message) {
        for (PMudPlayer inRoom : start.getPlayers().values()) {
            sender.sendReply(message, "dd", inRoom.getPid());
        }
    }


    @PreDestroy
    public void preDestroy() {
        sender.sendServerQuit();
    }

}
