package org.freakz.pmud.pmudserver.pmud.handlers.impl;

import lombok.extern.slf4j.Slf4j;
import org.freakz.pmud.common.objects.*;
import org.freakz.pmud.pmudserver.World.World;
import org.freakz.pmud.pmudserver.pmud.CommandHandlerService;
import org.freakz.pmud.pmudserver.pmud.ScoreAndLevelsService;
import org.freakz.pmud.pmudserver.pmud.VerbRequest;
import org.freakz.pmud.pmudserver.pmud.VerbResponse;
import org.freakz.pmud.pmudserver.pmud.handlers.PMudVerbHandler;
import org.freakz.pmud.pmudserver.service.MessageSender;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public abstract class HandlerBase implements PMudVerbHandler {

    @Autowired
    protected CommandHandlerService commandHandlerService;

    @Autowired
    private MessageSender sender;

    @Autowired
    World world;

    @Autowired
    ScoreAndLevelsService levelsService;

    VerbResponse invokeVerb(String verb, PMudPlayer player) {
        return commandHandlerService.invokeVerb(verb, player);
    }

    String args(VerbRequest req) {
        return req.getArgs().getArgs();
    }

    String argsOrMe(VerbRequest req) {
        if (req.getArgs().getArgs().equals("me")) {
            return req.getPlayer().getName();
        }
        return req.getArgs().getArgs();
    }


    boolean isNumericArg(VerbRequest req) {
        String args = args(req);
        return args.matches("\\d*?");
    }

    int getNumericArg(VerbRequest req) {
        return Integer.parseInt(args(req));
    }

    boolean hasArgs(VerbRequest req) {
        return req.getArgs().hasArgs();
    }

    String playerName(VerbRequest request) {
        return request.getPlayer().getName();
    }

    PMudPlayer player(VerbRequest req) {
        return req.getPlayer();
    }

    Location location(VerbRequest req) {
        return req.getPlayer().getLocation();
    }

    String look(VerbRequest req) {
        return invokeVerb("look", player(req)).getToSender();
    }

    String hisOrHer(PMudPlayer p) {
        if (p.getSex() == Mobile.PSex.MALE) {
            return "his";
        } else {
            return "her";
        }
    }

    void invokeVerbAndSendReply(String verb, PMudPlayer p) {
        VerbResponse resp = commandHandlerService.invokeVerb(verb, p);
        if (resp != null) {
            sender.sendReply(resp);
        } else {
            log.error("Verb invoke failed: {}", verb);
        }
    }

    void setPn(VerbRequest req, PMudObject o) {
        if (o instanceof Mobile) {
            setHimOrHer(req, (Mobile) o);
        } else {
            setIt(req, (PObject) o);
        }
    }

    void setIt(VerbRequest req, PObject o) {
        player(req).setIt(o.name());
    }


    void setHimOrHer(VerbRequest req, Mobile m) {
        if (m.getSex() == Mobile.PSex.MALE) {
            player(req).setHim(m.name());
        } else {
            player(req).setHer(m.name());
        }
    }

}
