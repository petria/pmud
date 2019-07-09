package org.freakz.pmud.pmudserver.pmud.handlers.impl;

import org.freakz.pmud.common.objects.PMudPlayer;
import org.freakz.pmud.pmudserver.pmud.VerbRequest;
import org.freakz.pmud.pmudserver.pmud.VerbResponse;
import org.freakz.pmud.pmudserver.pmud.handlers.AcceptVerbs;
import org.freakz.pmud.pmudserver.pmud.handlers.PMudVerbAcceptor;
import org.freakz.pmud.pmudserver.service.ScoreAndLevelsService;
import org.springframework.stereotype.Component;

@Component
@PMudVerbAcceptor
public class PMudInfoHandler extends HandlerBase {

    @AcceptVerbs(verbs = {"who"})
    public void handleWho(VerbRequest req, VerbResponse resp) {
        String msg = "";
        msg += "+-------------+---------------------------------------------------------------+\n";
        msg += "| Level       | Title                                                         |\n";
        msg += "I-------------I---------------------------------------------------------------I\n";
        for (PMudPlayer p : world.getNameToPlayerMap().values()) {
            msg += String.format("| %10s  | %-61s |\n", p.getLevel(), p.getName() + " " + p.getTitle());
        }
        msg += "I-------------+---------------------------------------------------------------I\n";
        msg += String.format("| There are currently %2s visible players on PMud                              |\n", world.playerCount());
        msg += "+-----------------------------------------------------------------------------+\n";

        resp.setToSender(msg);

    }

    @AcceptVerbs(verbs = {"levels"})
    public void hadleLevels(VerbRequest req, VerbResponse resp) {
        String msg = "";
        msg += "Level   Points        Warrior     Mage        Thief       Priest\n";
        msg += "----------------------------------------------------------------\n";
        for (ScoreAndLevelsService.Level l : levelsService.getLevels()) {
            msg += String.format("%-2d %11d %14s     %-11s %-11s %-14s\n", l.level, l.minScore, l.title(0), l.title(1), l.title(2), l.title(3));
        }
        resp.setToSender(msg);

    }

}
