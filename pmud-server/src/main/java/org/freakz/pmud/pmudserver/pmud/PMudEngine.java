package org.freakz.pmud.pmudserver.pmud;

import lombok.extern.slf4j.Slf4j;
import org.freakz.pmud.common.enums.PClass;
import org.freakz.pmud.common.message.PMudLoginMessage;
import org.freakz.pmud.common.message.PMudLoginReplyMessage;
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
            PMudPlayer player = reloadPlayer(pMudMessage.getPlayer(), pMudMessage.getPid());
            VerbResponse response = new VerbResponse(player);

            response.setToRoomF(player.getLocation(), "%s entered game.\n", player.getName());
            response.setToSender(commandHandlerService.invokeVerb("look", player).getToSender());
            response.setToWorld("** Player entered game: " + player.getName() + "\n");

            sender.sendReply(response);


        } else if (pMudMessage.getMessage().equals("DISCONNECTED")) {

            log.debug("Player disconnected: {}", pMudMessage.getPlayer());
            world.removePlayer(pMudMessage.getPlayer());

        } else {
            PMudPlayer player = world.findPlayer(pMudMessage.getPlayer());
            if (player == null) {
                player = reloadPlayer(pMudMessage.getPlayer(), pMudMessage.getPid());
                log.debug("Reloaded: {}", player.getName());
            }

            VerbRequest request = new VerbRequest(pMudMessage.getMessage(), player);
            VerbResponse response = new VerbResponse(player);
            boolean success = commandHandlerService.invokeVerbHandler(request, response);
            if (success) {
                sender.sendReply(response);
                if (response.doQuit) {
                    sender.sendQuitClientMessage(player.getPid());
                }
            } else {
                sender.sendReply(player, "Pardon?\n");
            }

        }
    }


    private PMudPlayer reloadPlayer(String playerName, long pid) {
//        String playerName = login.getPlayer();
        PMudPlayer player = world.findPlayer(playerName);
        if (player == null) {
            player = new PMudPlayer(world.getZone("start"));
            player.setHomeLocation(world.getLocationByName2("home1"));
            player.setLevelNum(1);
            player.setStrength(75);
            player.setMana(15);
            player.setName(playerName);
            player.setpClass(PClass.THIEF);
            player.setScore(0);
            player.setTitle("the Newbie");
            player.setPid(pid);
            world.addPlayer(player);
            world.addPlayerScore(player, 0);
        } else {
            log.debug("Existing player, old pid: {} - new pid: {}", player.getPid(), pid);
            world.quitPlayer(player);
            sender.sendQuitClientMessage(player.getPid());
            player.setPid(pid);
        }

        Location start = world.getLocationByName2("start1");
        player.setLocation(start);

        sendMessageToRoom(player, start, playerName + " entered game\n");

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


    public void handlePMudLoginMessage(PMudLoginMessage login) {
        log.debug("Login: pid {} / player: {}", login.getPid(), login.getPlayer());

        PMudPlayer player = reloadPlayer(login.getPlayer(), login.getPid());
//        sender.sendReply(player, commandHandlerService.invokeVerb("look", player).getToSender());
        PMudLoginReplyMessage reply = new PMudLoginReplyMessage(player.getName());
        reply.setReplyToPid(login.getPid());
        reply.setResult(PMudLoginReplyMessage.Result.OK);
        sender.sendLoginReply(reply);
    }
}
