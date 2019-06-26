package org.freakz.pmud.pmudserver.service;

import lombok.extern.slf4j.Slf4j;
import org.freakz.pmud.pmudclient.common.message.PMudMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MessageSender {

    @Autowired
    private JmsTemplate jmsTemplate;

    public void sendReply(String message) {
        log.debug("Start send");
        PMudMessage mudMessage = new PMudMessage(message);
        jmsTemplate.convertAndSend("pmud-clients.topic", message);
        log.debug("Sent!");
    }

}
