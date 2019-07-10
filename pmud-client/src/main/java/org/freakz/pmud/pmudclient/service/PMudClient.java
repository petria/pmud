package org.freakz.pmud.pmudclient.service;

import lombok.extern.slf4j.Slf4j;
import org.freakz.pmud.common.message.PMudMessage;
import org.freakz.pmud.common.message.PMudMessageToAll;
import org.freakz.pmud.common.util.PHelpers;
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

        System.out.printf(">> %d\n", args.length);

        if (args.length == 1) {
            player = args[0];
        } else {
            player = getPlayerName();
        }
        System.out.printf(">>Player name: %s\n", player);

        log.debug("Connecting player: {}", player);

        String prev = "";
        String last = "";

        sender.sendToServer("CONNECTED", player);
        while (true) {
            String message = scanner.nextLine();
            if (!message.isEmpty()) {
                if (message.equals("quit")) {
                    System.out.print("\nBye bye!\n");
                    System.exit(0);
                } else if (message.equals("!!")) {
                    sender.sendToServer(prev, player);
                } else if (message.equals("!")) {
                    sender.sendToServer(last, player);
                } else {
                    prev = last;
                    last = message;
                    pressed = true;
                    sender.sendToServer(message, player);
                    Thread.sleep(150L);
                }
            } else {
                prompt();
            }

        }
    }

    private String getPlayerName() {
        System.out.print("By what name should I call you? ");
        String name = scanner.nextLine();
        return PHelpers.capitalize(name);
    }

    boolean pressed = false;

    private void prompt() {
        pressed = false;
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
                    if (!pressed) {
                        System.out.println();
                    }
                    System.out.print(message.getMessage());
                    prompt();
                }
            } else {
                log.error("Null response");
                prompt();
            }
        }
    }

    @JmsListener(destination = "pmud-clients-all.topic")
    public void receiveMessageAll(PMudMessageToAll message) {
        if (message.getMessage() != null && message.getExceptPid() != ProcessHandle.current().pid()) {
            if (!pressed) {
                System.out.println();
            }
            System.out.print(message.getMessage());
            prompt();
        }
    }


    @PreDestroy
    public void shutdown() {
        log.debug("Shutting down!");
        sender.sendToServer("DISCONNECTED", player);
    }


}
