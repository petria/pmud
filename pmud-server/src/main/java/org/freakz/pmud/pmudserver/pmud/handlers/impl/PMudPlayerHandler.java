package org.freakz.pmud.pmudserver.pmud.handlers.impl;

import org.freakz.pmud.common.objects.Mobile;
import org.freakz.pmud.common.objects.PMudPlayer;
import org.freakz.pmud.common.objects.PObject;
import org.freakz.pmud.common.player.Level;
import org.freakz.pmud.common.util.ConsoleColors;
import org.freakz.pmud.pmudserver.pmud.VerbRequest;
import org.freakz.pmud.pmudserver.pmud.VerbResponse;
import org.freakz.pmud.pmudserver.pmud.handlers.AcceptVerbs;
import org.freakz.pmud.pmudserver.pmud.handlers.PMudVerbAcceptor;
import org.springframework.stereotype.Component;

import static org.freakz.pmud.pmudserver.pmud.FightService.MAXARMOR;
import static org.freakz.pmud.pmudserver.pmud.ScoreAndLevelsService.LVL_WIZARD;

@Component
@PMudVerbAcceptor
public class PMudPlayerHandler extends HandlerBase {


    @AcceptVerbs(verbs = {"drink"})
    public void handleDrink(VerbRequest req, VerbResponse resp) {
        if (!hasArgs(req)) {
            resp.setToSender("What's that?\n");
            return;
        }
        PMudPlayer p = player(req);
        PObject o = world.findClosestPObject(player(req), args(req));
        if (o != null) {
            if (o.getZone().getName().equals("icecave") && o.name().equals("fountain")) {
                if (p.getLevelNum() >= 3) {
                    resp.setToSender("Faintly magical by the taste.\n");
                    return;
                }
                Level level = world.addPlayerScore(p, 40);
                String msg = "";
                if (level != null) {
                    msg += String.format("You are now %s the %s\n", p.getName(), p.getTitle());
                }
                msg += "You feel a wave of energy sweeping through you.\n";
                resp.setToSender(msg);
                return;
            }
        }
    }

    String[] MobDesc = {
            "%s is of very weak build.\n",
            "%s is of weak build.\n",
            "%s appears to be of average strength.\n",
            "%s looks strong.\n",
            "%s looks very strong.\n",
            "%s seems almost invincible.\n"
    };
    String[] omess = {
            "%s is dead.",
            "%s is near death",
            "%s is near death",
            "%s is mortally wounded",
            "%s is seriously wounded",
            "%s has some wounds, but is still fairly strong",
            "%s has minor cuts and abrasions",
            "%s has minor cuts and abrasions",
            "%s seems a bit dazed",
            "%s is in better than average condition",
            "%s is in exceptional health",
            "%s is in exceptional health",
            "&+G%s is invincible"
    };

    String[] smess = {
            "You are dead.",
            "***You are near death***",
            "***You are near death***",
            "***You are mortally wounded***",
            "***You are seriously wounded***",
            "You have some wounds, but are still fairly strong.",
            "You have minor cuts and abrasions.",
            "You have minor cuts and abrasions.",
            "You feel a bit dazed.",
            "You are in better than average condition.",
            "You are in exceptional health.",
            "You are in exceptional health.",
            "&+GYou are invincible."
    };


    private String pmess(Mobile m, boolean self) {
        int p = 0;
        if (m.getLevelNum() >= LVL_WIZARD)
            p = 12;
        else if (m.getStrength() <= 0)
            p = 0;
        else {
            if (m.isMobile())
                p = (int) (m.getStrength() * 10) / m.getMaxStrength();
            else
                p = (int) (m.getStrength() * 10) / m.getMaxStrength();

            if (p >= 11) p = 11;
            else p++;
        }


        if (self)
            return smess[p];
        else
            return String.format(omess[p], m.getName());


    }

    @AcceptVerbs(verbs = {"examine"})
    public void handleExamine(VerbRequest req, VerbResponse resp) {
        String msg = "You see nothing special.\n";
        if (hasArgs(req)) {
            String name = args(req);
            boolean self = false;

            PMudPlayer p = player(req);

            Mobile m = null;
            PMudPlayer other = p.getLocation().getPlayer(name, null);
            if (other != null) {
                m = other;
            } else {
                m = p.getLocation().getMobile(name);
            }

            if (m != null) {
                int s = m.getStrength() + 25 * m.getDamage();
                if (between(0, 250, s)) {
                    msg = String.format(MobDesc[0], m.name());
                }
                if (between(200, 450, s)) {
                    msg = String.format(MobDesc[1], m.name());
                }
                if (between(450, 800, s)) {
                    msg = String.format(MobDesc[2], m.name());
                }
                if (between(800, 1200, s)) {
                    msg = String.format(MobDesc[3], m.name());
                }
                if (between(1200, 1500, s)) {
                    msg = String.format(MobDesc[4], m.name());
                }
                if (s >= 1500) {
                    msg = String.format(MobDesc[5], m.name());
                }

                msg += pmess(m, self) + "\n";

                resp.setToRoomF(p.getLocation(), "%s examines %s closely\n", p.getName(), m.name());

            } else {

                PObject o = p.getLocation().getObject(name);
                if (o != null) {
                    if (o.getExamine() != null) {
                        msg = o.getExamine() + "\n";
                    }
                    resp.setToRoomF(p.getLocation(), "%s examines %s closely\n", p.getName(), o.name());
                }

            }

        }
        resp.setToSender(msg);

    }

    private boolean between(int start, int end, int s) {
        return (s > start && s <= end);
    }

    @AcceptVerbs(verbs = {"flee"})
    public void handleFlee(VerbRequest req, VerbResponse resp) {

        PMudPlayer p = player(req);
        if (!p.isFighting()) {
            resp.setToSender("You are not fighting!\n");
            return;

        }

        p.getFightingTo().removeFightingTo();
        p.removeFightingTo();

        resp.setToSenderF("You flee from fight!!\n");

    }

    @AcceptVerbs(verbs = {"kill", "k"})
    public void handleKill(VerbRequest req, VerbResponse resp) {
        PMudPlayer p = player(req);
        if (p.isFighting()) {
            resp.setToSender("You are already fighting!\n");
            return;
        }

        if (!hasArgs(req)) {
            resp.setToSender("Kill who?\n");
            return;
        }

        Mobile m = p.getLocation().getMobile(args(req));
        if (m != null) {
            if (m.isFighting()) {
                resp.setToSenderF("%s are already fighting!\n", m.getName());
                return;

            }

            if (p.isSitting()) {
                invokeVerbAndSendReply("stand", p);
            }

            p.setFightingTo(m);
            m.setFightingTo(p);

            resp.setToSenderF("You charge into battle with %s.\n", m.getName());

        } else {
            resp.setToSenderF("%s is not here!\n", args(req));
        }

    }

    @AcceptVerbs(verbs = {"pronouns", "pn"})
    public void handlePronouns(VerbRequest req, VerbResponse resp) {
        String msg = "";

        msg += String.format("Current pronouns are\n");
        msg += String.format("Me    : %s\n", player(req).getMe());
        msg += String.format("It    : %s\n", player(req).getIt());
        msg += String.format("Him   : %s\n", player(req).getHim());
        msg += String.format("Her   : %s\n", player(req).getHer());
        msg += String.format("Them  : %s\n", player(req).getThem());

        resp.setToSender(msg);
    }

    @AcceptVerbs(verbs = {"stats"})
    public void handleStats(VerbRequest req, VerbResponse resp) {
        if (!hasArgs(req)) {
            resp.setToSender("Stats who?\n");
            return;
        }
        Mobile m = world.getMobileOrPlayer(args(req));
        if (m != null) {
            String msg = "";

            msg += String.format("&+WName       &+C: &+w%s", m.getName());
            if (!m.isMobile()) {
                msg += String.format("\n&+WTitle      &+C: &+w%s", m.getTitle());
                msg += String.format("\n&+WScore      &+C: &+w%d", m.getScore());
                msg += String.format("\n&+WLevel      &+C: %s - %s (level %d)",
                        m.getpClass().name(), m.getLevelName(),
                        m.getLevelNum());

            }

            msg += String.format("\n&+WStrength   &+C: &+w%d / %d", m.getStrength(), m.getMaxStrength());

            if (!m.isMobile()) {
                msg += String.format("\n&+WMagic      &+C: &+w%d / %d", m.getMana(), m.getMaxMana());
            }

            if (m.getWielded() != null) {
                msg += String.format("\n&+WDamage     &+C: &+w%d (Wielding: %s)", m.getDamage(), m.getWielded().name());
            } else {
                msg += String.format("\n&+WDamage     &+C: &+w%d", m.getDamage());
            }

            msg += String.format("\n&+WArmor      &+C: &+w%d / %d", m.getArmor(), MAXARMOR);


            if (!m.isMobile()) {
                msg += String.format("\n&+WKill/Death &+C: &+w%d / %d", m.getKills(), m.getDeaths());
            }
            msg += String.format("\n&+WVisibility &+C: &+w%d", m.getVisibilityLevel());

            if (m.isMobile()) {
                msg += String.format("\n&+WAggression &+C: &+w%d %%", m.getAggression());
                msg += String.format("\n&+WSpeed      &+C: &+w%d", m.getSpeed());
                msg += String.format("\n&+W%s      &+C: &+w%s", "Zone ", m.getZone().getName());
            }
            msg += String.format("\n&+WWimpy      &+C: &+w%d", m.getWimpy());

            msg += String.format("\n&+WStart      &+C: &+w%s &+B(&*%s&+B)",
                    m.getStartLocation().getTitle(), m.getStartLocation().getName2());

            msg += String.format("\n&+WLocation   &+C: &+w%s &+B(&*%s&+B)",
                    m.getLocation().getTitle(), m.getLocation().getName2());


            msg += "\n" + ConsoleColors.RESET;

            resp.setToSender(msg);

        } else {
            resp.setToSender("Who's that?\n");
        }
    }

}