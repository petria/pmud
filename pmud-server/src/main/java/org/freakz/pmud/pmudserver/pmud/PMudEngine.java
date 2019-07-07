package org.freakz.pmud.pmudserver.pmud;

import lombok.extern.slf4j.Slf4j;
import org.freakz.pmud.common.message.PMudMessage;
import org.freakz.pmud.common.objects.Location;
import org.freakz.pmud.common.objects.Mobile;
import org.freakz.pmud.common.objects.PMudPlayer;
import org.freakz.pmud.common.objects.PObject;
import org.freakz.pmud.pmudserver.World.World;
import org.freakz.pmud.pmudserver.service.MessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class PMudEngine {

    private Map<String, PMudPlayer> playerMap = new HashMap<>();

    @Autowired
    private MessageSender sender;

    @Autowired
    private World world;


    public void handlePMudMessage(PMudMessage pMudMessage) {

        if (pMudMessage.getMessage().equals("CONNECTED")) {
            PMudPlayer pMudPlayer = handleConnected(pMudMessage);
            handleLook(pMudPlayer);
        } else if (pMudMessage.getMessage().equals("DISCONNECTED")) {
            log.debug("Player disconnected: {}", pMudMessage.getPlayer());
            playerMap.remove(pMudMessage.getPlayer());
        } else {
            PMudPlayer player = playerMap.get(pMudMessage.getPlayer());
            if (player != null) {
                if (pMudMessage.getMessage().equals("look")) {
                    handleLook(player);
                } else if (pMudMessage.getMessage().startsWith("goto")) {
                    handleGoto(player, getParams(pMudMessage.getMessage(), "goto"));
                } else {
                    log.warn("Unknown command: ");
                }
            } else {
                log.error("No player!?");
            }
        }
    }

    private String getParams(String message, String command) {
        return message.replaceFirst(command, "").trim();
    }

    private PMudPlayer handleConnected(PMudMessage message) {
        String player = message.getPlayer();
        PMudPlayer pMudPlayer = playerMap.get(player);
        if (pMudPlayer == null) {
            pMudPlayer = new PMudPlayer();
            pMudPlayer.setName(message.getPlayer());
            pMudPlayer.setPid(message.getPid());
            this.playerMap.put(pMudPlayer.getName(), pMudPlayer);
        }
        pMudPlayer.setLocation(world.getLocationByName2("blizzard70"));
        return pMudPlayer;
    }

    private void handleLook(PMudPlayer player) {
        Location location = player.getLocation();
        String msg = "\n";

        msg += String.format("[ID: %05d] %s [%s@%s]\n", location.getId(), location.getName2(), location.getName(), location.getZone().getName());

        msg += location.getTitle() + "\n";
        if (location.getLocationFlags().size() > 0) {
            for (String flag : location.getLocationFlags()) {
                msg += String.format("[%s] ", flag.toUpperCase());
            }
        }
        msg += "\n" + location.getDescription();

        for (PObject object : location.getObjects().values()) {
            String description = object.getDescription(object.getState());
            if (description == null) {
                msg += "<marker>" + object.getpName() + "\n";

            } else {
                msg += object.getDescription(object.getState()) + "\n";
            }
        }

        for (Mobile mobile : location.getMobiles().values()) {
            msg += mobile.getDescription() + "\n";
        }

        msg += "\nObvious exits are:\n";
        if (location.getExitsMap().size() > 0) {
            for (Location.Exits exit : Location.Exits.values()) {
                Location l = location.getExitsMap().get(exit.getDir());
                if (l != null) {
                    msg += String.format(" %6s : %-45s : %s\n", exit.getNice(), l.getTitle(), l.getName2());
                }
            }

        } else {
            msg += "None....\n";
        }

        sender.sendReply(msg, player.getName());

    }

    private void handleGoto(PMudPlayer player, String params) {
        Location newLocation = world.getLocationByName2(params);
        if (newLocation == null) {
            sender.sendReply("Unknown player, object or room.", player.getName());
        } else {
            player.setLocation(newLocation);
            handleLook(player);
        }
    }


    @PreDestroy
    public void preDestroy() {
        sender.sendServerQuit();
    }

}
