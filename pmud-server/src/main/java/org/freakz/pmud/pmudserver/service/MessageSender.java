package org.freakz.pmud.pmudserver.service;

import lombok.extern.slf4j.Slf4j;
import org.freakz.pmud.common.message.PMudMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MessageSender {

    @Autowired
    private JmsTemplate jmsTemplate;

    public void sendReply(String message, String player) {
        log.debug("Start send");
        PMudMessage mudMessage = new PMudMessage(message, player);
        jmsTemplate.convertAndSend("pmud-clients.topic", mudMessage);
        log.debug("Sent!");
    }

    public void sendServerQuit() {
        PMudMessage mudMessage = new PMudMessage("SERVER_QUIT", null);
        jmsTemplate.convertAndSend("pmud-clients.topic", mudMessage);
        log.debug("Sent SERVER_QUIT");
    }
}
