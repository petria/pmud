package org.freakz.pmud.pmudserver.pmud;

import lombok.extern.slf4j.Slf4j;
import org.freakz.pmud.common.message.PMudMessage;
import org.freakz.pmud.common.objects.Location;
import org.freakz.pmud.common.objects.PMudPlayer;
import org.freakz.pmud.pmudserver.World.World;
import org.freakz.pmud.pmudserver.service.MessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
                } else {
                    log.warn("Unknown command: ");
                }
            } else {
                log.error("No player!?");
            }
        }
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
        pMudPlayer.setLocation(world.getLocationByName("Green@village"));
        return pMudPlayer;
    }

    private void handleLook(PMudPlayer player) {
        Location location = player.getLocation();
        String msg = "\n" + location.getTitle() + "\n\n";
        msg += location.getDescription() + "\n\n";

        sender.sendReply(msg, player.getName());

    }

}
