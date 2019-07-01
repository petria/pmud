package org.freakz.pmud.pmudclient.service;

import lombok.extern.slf4j.Slf4j;
import org.freakz.pmud.pmudclient.common.message.PMudMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import java.util.Scanner;

@Service
@Slf4j
public class CommandReader implements CommandLineRunner {

    private Scanner  scanner = new Scanner(System.in);

    @Autowired
    private MessageSender sender;

    @Override
    public void run(String... args) throws Exception {
        while (true) {
            System.out.print("\npmud> ");
            String message = scanner.next();
            if (!message.isEmpty()) {
                if (message.equals("quit")) {
                    System.out.print("\nBye bye!\n");
                    System.exit(0);
                } else {
                    sender.testSend(message);
                }
            }

        }
    }

    @JmsListener(destination = "pmud-clients.topic")
    public void receiveMessage(PMudMessage message) {
        System.out.println(message.getMessage());
    }

}
