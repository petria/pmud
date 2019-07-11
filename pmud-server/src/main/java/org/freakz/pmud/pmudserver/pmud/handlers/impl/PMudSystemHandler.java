package org.freakz.pmud.pmudserver.pmud.handlers.impl;

import org.freakz.pmud.pmudserver.pmud.CommandHandlerService;
import org.freakz.pmud.pmudserver.pmud.VerbRequest;
import org.freakz.pmud.pmudserver.pmud.VerbResponse;
import org.freakz.pmud.pmudserver.pmud.handlers.AcceptVerbs;
import org.freakz.pmud.pmudserver.pmud.handlers.PMudVerbAcceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@PMudVerbAcceptor
public class PMudSystemHandler extends HandlerBase {

    @Autowired
    private CommandHandlerService commands;

    @AcceptVerbs(verbs = {"clear"})
    public void handleClear(VerbRequest req, VerbResponse resp) {
        resp.setToSender("\033[H\033[2J");
    }

    @AcceptVerbs(verbs = {"commands"})
    public void handleCommands(VerbRequest req, VerbResponse resp) {
        Map<String, CommandHandlerService.Handler> map = commands.getHandlers();
        String msg = "";
        msg += "+------------------+----------------------------------------------------------------------------------------------------------I\n";
        msg += "|      Keyword     | method()                Java handler class\n";
        msg += "I------------------I----------------------------------------------------------------------------------------------------------I\n";
        for (String key : map.keySet()) {
            CommandHandlerService.Handler handler = map.get(key);
            msg += String.format("|  %15s | %-20s :: %s\n", key, handler.method.getName(), handler.clazz.getClass().getName());
        }
        msg += String.format("|  %15s |\n", "");
        msg += String.format("|  %15s | %-20s \n", "quit", "exit boring game!");

        msg += "+------------------+----------------------------------------------------------------------------------------------------------I\n";
        msg += "+                  |  Total " + map.keySet().size() + " of implemented commands / verbs in system!\n";
        msg += "+------------------+----------------------------------------------------------------------------------------------------------I\n";

        resp.setToSender(msg);
    }

    @AcceptVerbs(verbs = {"quit"})
    public void handleQuit(VerbRequest req, VerbResponse resp) {
        world.quitPlayer(player(req));
        resp.setDoQuit(true);
        String msg = "";
        msg += "---PMudMUD-------------------------------------------------------------------\n\n";
        msg += "Thank you for playing PMudMUD!\n\n";
        msg += "-----------------------------------------------------------------------------\n";
        resp.setToWorld("*** QUIT: " + playerName(req) + "\n");
        resp.setToSender(msg);
    }

}