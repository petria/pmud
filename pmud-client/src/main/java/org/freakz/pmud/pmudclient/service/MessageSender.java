package org.freakz.pmud.pmudclient.service;

import lombok.extern.slf4j.Slf4j;
import org.freakz.pmud.pmudclient.common.message.PMudMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MessageSender  {

    @Autowired
    private JmsTemplate jmsTemplate;

    public void testSend(String message) {

        log.debug("Start send");
        PMudMessage mudMessage = new PMudMessage(message);
        jmsTemplate.convertAndSend("pmud-server.topic", message);
        log.debug("Sent!");
    }

}
