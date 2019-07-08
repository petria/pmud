package org.freakz.pmud.pmudserver.service;

import lombok.extern.slf4j.Slf4j;
import org.freakz.pmud.common.message.PMudMessage;
import org.freakz.pmud.common.objects.PMudPlayer;
import org.freakz.pmud.pmudserver.pmud.VerbResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MessageSender {

    @Autowired
    private JmsTemplate jmsTemplate;

    public void sendReply(String message, String player, long replyToPid) {
//        log.debug("Start send");
        PMudMessage mudMessage = new PMudMessage(message, player);
        mudMessage.setReplyToPid(replyToPid);
        jmsTemplate.convertAndSend("pmud-clients.topic", mudMessage);
//        log.debug("Sent!");
    }

    public void sendServerQuit() {
        PMudMessage mudMessage = new PMudMessage("SERVER_QUIT", null);
        mudMessage.setReplyToPid(Long.MIN_VALUE);
        jmsTemplate.convertAndSend("pmud-clients.topic", mudMessage);
        log.debug("Sent SERVER_QUIT");
    }

    public void sendReply(VerbResponse response) {
        PMudMessage mudMessage = new PMudMessage(response.getToSender(), response.getPlayer().getName());
        mudMessage.setReplyToPid(response.getPlayer().getPid());
        jmsTemplate.convertAndSend("pmud-clients.topic", mudMessage);

    }

    public void sendReply(PMudPlayer player, String message) {
        PMudMessage mudMessage = new PMudMessage(message, player.getName());
        mudMessage.setReplyToPid(player.getPid());
        jmsTemplate.convertAndSend("pmud-clients.topic", mudMessage);

    }
}
