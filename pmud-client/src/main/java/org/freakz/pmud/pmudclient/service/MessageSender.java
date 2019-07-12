package org.freakz.pmud.pmudclient.service;

import lombok.extern.slf4j.Slf4j;
import org.freakz.pmud.common.message.PMudLoginMessage;
import org.freakz.pmud.common.message.PMudMessage;
import org.freakz.pmud.common.message.PMudNewConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MessageSender  {

    @Autowired
    private JmsTemplate jmsTemplate;

    public void sendToServer(String message, String player) {

//        log.debug("Start send");
        PMudMessage mudMessage = new PMudMessage(message, player);

        jmsTemplate.convertAndSend("pmud-server.topic", mudMessage);
//        log.debug("Sent!");
    }

    public void sendLogin(PMudLoginMessage login) {
        jmsTemplate.convertAndSend("pmud-server-login.topic", login);

    }

    public void newConnection(PMudNewConnection connection) {
        jmsTemplate.convertAndSend("pmud-server-new-connection.topic", connection);
    }

}
