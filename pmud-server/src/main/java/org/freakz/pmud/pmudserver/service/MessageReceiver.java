package org.freakz.pmud.pmudserver.service;

import lombok.extern.slf4j.Slf4j;
import org.freakz.pmud.common.message.PMudMessage;
import org.freakz.pmud.pmudserver.pmud.PMudEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MessageReceiver {

    @Autowired
    private MessageSender messageSender;

    @Autowired
    private PMudEngine engine;


    @JmsListener(destination = "pmud-server.topic")
    public void receiveMessage(PMudMessage pMudMessage) {
        log.debug("Received <{}>", pMudMessage.toString());
//        messageSender.sendReply("Hello client with pid: " + pMudMessage.getPid() + ", this is Server reply to: " + pMudMessage.getMessage());
        engine.handlePMudMessage(pMudMessage);
    }


}
