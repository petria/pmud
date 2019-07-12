package org.freakz.pmud.pmudclient.service;

import lombok.extern.slf4j.Slf4j;
import org.freakz.pmud.common.message.*;
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

    private static final long MY_PID = ProcessHandle.current().pid();
    private Scanner scanner = new Scanner(System.in);

    @Autowired
    private MessageSender sender;
    private boolean doMainLoop = true;

    @Override
    public void run(String... args) throws Exception {
        String remoteHost = System.getenv("REMOTE_HOST");
        if (remoteHost == null) {
            remoteHost = "localhost";
        }
        System.out.printf("REMOTE_HOST: " + remoteHost);
        sendNewConnection(remoteHost);

        String player;
        if (args.length == 1) {
            player = args[0];
        } else {
            player = getPlayerName();
            if (player == null) {
                System.out.print("\nBye then!\n");
                doKill();
                System.exit((0));
            }
        }

        sendLoginMessage(player, "passwrd");

    }

    private String getPreLoginBanner() {
        String msg = "\n\n";
        msg += "Welcome to PMUD! - http://pmud.airiot.fi/\n" +
                "          _\n" +
                "         / \\   o    ____\n" +
                "        /  |  /  /\\  /    ReMAKE\n" +
                "       /__/  /  /   /      \n" +
                "                            \n" +
                "    o-------------------------------------------------------o\n" +
                "     Version         : 0.0.1\n" +
                "     Implementor     : Petri Airio (petri.j.airio.gmail@com)\n" +
                "     Up at           : pdirt.airiot.fi 6715 \n" +
                "     Server type     : x86_64 GNU/Linux\n" +
                "     Last code build : most likely today!\n" +
                "    o-------------------------------------------------------o\n" +
                "\n";
        return msg;
    }

    private void mainLoop(String player) {
        System.out.print("\033[H\033[2J");
        System.out.print("\n\n\n");

        System.out.print(">>> -------------    W E L C O M E  to  PMUD     ------------- <<<\n\n");
        System.out.print(">>>           USE commands TO SEE WHAT IS IMPLEMENTED! <<<\n\n");
        System.out.print(">>> ------------------                  ---------------------- <<<\n\n");

        String prev = "";
        String last = "";

        sender.sendToServer("look", player);

        while (doMainLoop) {
            String message = scanner.nextLine();
            if (!message.isEmpty()) {
                if (message.equals("!!")) {
                    sender.sendToServer(prev, player);
                } else if (message.equals("!")) {
                    sender.sendToServer(last, player);
                } else {
                    prev = last;
                    last = message;
                    pressed = true;
                    if (doMainLoop) {
                        sender.sendToServer(message, player);
                        if (message.equals("quit")) {
                            doMainLoop = false;
                        } else {
                            try {
                                Thread.sleep(150L);
                            } catch (InterruptedException e) {
                                //
                            }
                        }
                    }
                }
            } else {
                prompt();
            }

        }
        doKill();
        System.out.print(">> Exit Client main loop!");
    }

    private void doKill() {
        String[] cmd = {"kill", "-9", "" + MY_PID, "&>/dev/null"};
        try {
            Thread.sleep(1000L);
            Runtime.getRuntime().exec(cmd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean serverRespondedToLogin = false;

    private void sendNewConnection(String remoteHost) {
        PMudNewConnection connection = new PMudNewConnection(remoteHost, MY_PID);
        sender.newConnection(connection);
    }

    private void sendLoginMessage(String player, String password) {

        PMudLoginMessage login = new PMudLoginMessage(player, password, MY_PID);
        sender.sendLogin(login);
        Thread t = new Thread(this::checkLoginResponse);
        t.start();

    }

    private void checkLoginResponse() {
        try {
            Thread.sleep(2 * 1000L);
            if (!serverRespondedToLogin) {
                System.out.print("\nServer did not response to login!\n\nBye Bye!\n");
                doKill();
            }
        } catch (InterruptedException e) {
            // ignore;
        }
    }

    private String getPlayerName() {
        System.out.print("\033[H\033[2J");
        System.out.print(getPreLoginBanner());
        while (true) {
            System.out.print("\n\nBy what name should I call you? ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                return null;
            }
            if (name.length() < 5) {
                System.out.print("\nToo short, must be min 5 chars!\n\n");
            } else {
                return PHelpers.capitalize(name);
            }
        }

    }

    boolean pressed = false;

    private void prompt() {
        if (doMainLoop) {
            pressed = false;
            System.out.print("pmud> ");
        }
    }

    @JmsListener(destination = "pmud-clients-server-quit.topic")
    public void receiveServerQuitMessage(PMudServerQuitMessage quit) {
        System.out.print("\n** [SERVER DID SHUTDOWN, EXITING CLIENT!]\n");
        doKill();
    }


    @JmsListener(destination = "pmud-clients.topic")
    public void receiveMessage(PMudMessage message) {
        if (message.getReplyToPid() == MY_PID) {
            if (message.getMessage() != null) {
                if (!pressed) {
                    System.out.println();
                }
                System.out.print(message.getMessage());
                prompt();

            } else {
                log.error("Null response");
                prompt();
            }

        }
    }

    @JmsListener(destination = "pmud-clients-login-reply.topic")
    public void receiveMessageLoginReply(PMudLoginReplyMessage message) {
        serverRespondedToLogin = true;
        if (message.getReplyToPid() == MY_PID) {
            if (message.getPlayer() != null) {
                doMainLoop = true;
                log.debug("{} login ok!", message.getPlayer());
                Thread t = new Thread(() -> mainLoop(message.getPlayer()));
                t.setName("PMud client: " + message.getPlayer());
                t.start();
            } else {
                System.out.print("Login failed!\n");
                System.exit(0);
            }
        }

    }

    //
    @JmsListener(destination = "pmud-clients-quit.topic")
    public void receiveClientQuit(PMudQuitClientMessage quit) {
        if (quit.getReplyToPid() == MY_PID) {
            doMainLoop = false;
//            System.out.print("Got quit message, exiting...");
            System.exit(0);
        }
    }

    @JmsListener(destination = "pmud-clients-all.topic")
    public void receiveMessageAll(PMudMessageToAllClients message) {
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
//        sender.sendToServer("DISCONNECTED", player);
    }


}
