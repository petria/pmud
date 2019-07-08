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

    private Scanner scanner = new Scanner(System.in);

    private static String player = "Someone";


    @Autowired
    private MessageSender sender;

    @Override
    public void run(String... args) throws Exception {

        if (args[0] != null) {
            player = args[0];
        }
        log.debug("Connecting player: {}", player);

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
        System.out.print("pmud> ");
    }


    @JmsListener(destination = "pmud-clients.topic")
    public void receiveMessage(PMudMessage message) {
        if (message.getReplyToPid() == ProcessHandle.current().pid()) {
            if (message.getMessage() != null) {
                if (message.getMessage().equals("SERVER_QUIT")) {
                    log.debug("SERVER DID QUIT");
                    System.exit(0);
                } else {
                    System.out.println(message.getMessage());
                    prompt();
                }
            } else {
                log.error("Null response");
                prompt();
            }
        }
    }


    @PreDestroy
    public void shutdown() {
        log.debug("Shutting down!");
        sender.sendToServer("DISCONNECTED", player);
    }


}
