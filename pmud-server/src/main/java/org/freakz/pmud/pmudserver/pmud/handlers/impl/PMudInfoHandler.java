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
    public void handleLevels(VerbRequest req, VerbResponse resp) {
        String msg = "";
        msg += "Level   Points        Warrior     Mage        Thief       Priest\n";
        msg += "----------------------------------------------------------------\n";
        for (ScoreAndLevelsService.Level l : levelsService.getLevels()) {
            msg += String.format("%-2d %11d %14s     %-11s %-11s %-14s\n", l.level, l.minScore, l.title(0), l.title(1), l.title(2), l.title(3));
        }

        resp.setToSender(msg);
    }

    @AcceptVerbs(verbs = {"score"})
    public void handleScore(VerbRequest req, VerbResponse resp) {
        PMudPlayer p = player(req);

        int maxStrength = 99;
        int maxMana = 42;

        String str1 = String.format("%d/%d", p.getStrength(), maxStrength);
        String str2 = String.format("%d/%d", p.getMana(), maxMana);

        String msg = "";
        msg += "+-----------------------------------------------------------------------------+\n";
        msg += String.format("| Name      : %-13s Level     : %-13s Score     : %d       |\n", p.getName(), p.getLevel(), p.getScore());
        msg += String.format("| Strength  : %-13s Mana      : %-13s Kills     : %-7d     |\n", str1, str2, p.getDeaths());
        msg += String.format("| Deaths    : %-13d Wimpy     : %-13d Q-Points  : %-7d     |\n", p.getDeaths(), p.getWimpy(), p.getDeaths());
        msg += String.format("| Age       : %-13d Coins     : %-13d AC Avg    : %-5s       |\n", p.getDeaths(), p.getWimpy(), p.getAcAvg() + "%");
        msg += "+-----------------------------------------------------------------------------+\n";
        resp.setToSender(msg);

    }
}