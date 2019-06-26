package org.freakz.pmud.pmudserver.service;

import lombok.extern.slf4j.Slf4j;
import org.freakz.pmud.pmudclient.common.message.PMudMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MessageReceiver {

    @Autowired
    private MessageSender messageSender;

    @JmsListener(destination = "pmud-server.topic")
    public void receiveMessage(PMudMessage message) {
        log.debug("Received <{}>", message.getMessage());
        messageSender.sendReply("Hello client with pid: " + message.getPid() + ", this is Server reply to: " + message.getMessage());
    }


}
