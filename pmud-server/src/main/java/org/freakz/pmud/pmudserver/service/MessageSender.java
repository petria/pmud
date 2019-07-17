package org.freakz.pmud.pmudserver.service;

import lombok.extern.slf4j.Slf4j;
import org.freakz.pmud.common.message.*;
import org.freakz.pmud.common.objects.PMudPlayer;
import org.freakz.pmud.pmudserver.World.World;
import org.freakz.pmud.pmudserver.pmud.VerbResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MessageSender {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private World world;

    public void sendReply(String message, String player, long replyToPid) {
//        log.debug("Start send");
        PMudMessage mudMessage = new PMudMessage(message, player);
        mudMessage.setReplyToPid(replyToPid);
        jmsTemplate.convertAndSend("pmud-clients.topic", mudMessage);
//        log.debug("Sent!");
    }

    public void sendServerQuit() {
        jmsTemplate.convertAndSend("pmud-clients-server-quit.topic", new PMudServerQuitMessage());
        log.debug("Sent SERVER_QUIT");
    }

    public void sendReply(VerbResponse response) {
        PMudMessage mudMessage = new PMudMessage(response.getToSender(), response.getPlayer().getName());
        mudMessage.setReplyToPid(response.getPlayer().getPid());

        if (response.getFromRoom() != null) {
            for (PMudPlayer inRoom : response.getFrom().getPlayers().values()) {
                if (inRoom == response.getPlayer()) {
                    continue;
                }
                sendReply(inRoom, response.getFromRoom());
            }
        }
        if (response.getToRoom() != null) {
            for (PMudPlayer inRoom : response.getTo().getPlayers().values()) {
                if (inRoom == response.getPlayer()) {
                    continue;
                }
                sendReply(inRoom, response.getToRoom());
            }
        }
        if (response.getToWorld() != null) {
            sendReplyToAll(response.getToWorld(), response.getPlayer().getPid());
        }
        jmsTemplate.convertAndSend("pmud-clients.topic", mudMessage);

    }

    public void sendReplyToAll(String toWorld, long pid) {
        jmsTemplate.convertAndSend("pmud-clients-all.topic", new PMudMessageToAllClients(toWorld, pid));
    }

    public void sendReply(PMudPlayer player, String message, String prompt) {
        PMudMessage mudMessage = new PMudMessage(message, player.getName());
        mudMessage.setReplyToPid(player.getPid());
        mudMessage.setPrompt(prompt);
        jmsTemplate.convertAndSend("pmud-clients.topic", mudMessage);

    }

    public void sendReply(PMudPlayer player, String message) {
        sendReply(player, message, null);
    }

    public void sendLoginReply(PMudLoginReplyMessage reply) {
        jmsTemplate.convertAndSend("pmud-clients-login-reply.topic", reply);
    }

    public void sendPlayerQuitMessage(long pid) {
        jmsTemplate.convertAndSend("pmud-clients-quit.topic", new PMudQuitClientMessage(pid));
    }

    public void sendPlayerDiedMessage(long pid, String quitMessage) {
        jmsTemplate.convertAndSend("pmud-clients-player-died.topic", new PMudQuitClientMessage(pid, quitMessage));
    }
}
