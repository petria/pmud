package org.freakz.pmud.pmudclient.service;

import lombok.extern.slf4j.Slf4j;
import org.freakz.pmud.common.message.PMudMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.util.Scanner;

@Service
@Slf4j
public class PMudClient implements CommandLineRunner {

    private Scanner  scanner = new Scanner(System.in);

    private static String player = "Someone";

    @Autowired
    private MessageSender sender;

    @Override
    public void run(String... args) throws Exception {

        sender.sendToServer("CONNECTED", player);
        while (true) {
            String message = scanner.nextLine();
            if (!message.isEmpty()) {
                if (message.equals("quit")) {
                    System.out.print("\nBye bye!\n");
                    System.exit(0);
                } else {
                    sender.sendToServer(message, player);
                }
            } else {
                prompt();
            }

        }
    }

    private void prompt() {
        System.out.print("\npmud> ");
    }


    @JmsListener(destination = "pmud-clients.topic")
    public void receiveMessage(PMudMessage message) {
        if (message.getMessage().equals("SERVER_QUIT")) {
            log.debug("SERVER DID QUIT");
            System.exit(0);
        } else {
            System.out.println(message.getMessage());
            prompt();
        }
    }


    @PreDestroy
    public void shutdown() {
        log.debug("Shutting down!");
        sender.sendToServer("DISCONNECTED", player);
    }


}
