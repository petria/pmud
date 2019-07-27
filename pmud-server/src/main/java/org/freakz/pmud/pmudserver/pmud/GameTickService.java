package org.freakz.pmud.pmudserver.pmud;

import lombok.extern.slf4j.Slf4j;
import org.freakz.pmud.common.enums.Exits;
import org.freakz.pmud.common.enums.PClass;
import org.freakz.pmud.common.objects.Location;
import org.freakz.pmud.common.objects.Mobile;
import org.freakz.pmud.common.objects.PMudPlayer;
import org.freakz.pmud.common.util.PHelpers;
import org.freakz.pmud.pmudserver.World.World;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import static org.freakz.pmud.common.util.PHelpers.randperc;

@Service
@Slf4j
public class GameTickService {

    @Autowired
    private PMudEngine engine;

    @Autowired
    private FightService fight;

    @Autowired
    private World world;


    @Scheduled(initialDelay = 2000, fixedRate = 2000)
    public void gameTick() {
        fight.handleFightTick();
        reGeneratePlayers();
        moveMoveMobiles();
    }

    private void moveMoveMobiles() {
        for (Mobile m : world.getMobilesAndPlayers()) {
            if (!m.isMobile()) {
                continue;
            }
            if (m.getSpeed() > 0) {
                int rnd = Math.abs(PHelpers.my_random()) % 30;
                if (rnd < m.getSpeed()) {
                    Location l = m.getLocation();
                    if (l.getExitsMap().values().size() > 0) {
                        Exits toMoveExit = PHelpers.getRandomExit(l);
                        if (toMoveExit != null) {

                            Location toMove = l.getExitsMap().get(toMoveExit.getDir());

                            if (toMove.getZone() != l.getZone()) {
                                continue;
                            } else {
//                                log.debug("Move: {} - {} -> {}", m.name(), m.getLocation().getName2(), toMove.getName2());
                                world.moveMobile(m, l, toMove);
                                world.sendToLocationF(l, null, null, "%s goes to %s.\n", m.name(), toMoveExit.getNiceL());
                                world.sendToLocationF(toMove, null, null, "\001p%s\003 %s.\n", m.name(), toMoveExit.getMobEnter());

                            }
                        }

                    }

                }
                //rand() % 30 < pspeed(mon)
            }
        }

    }


    private void reGeneratePlayers() {
        for (PMudPlayer p : world.getPlayers()) {

            Location l = p.getLocation();

            int chance;     // strength
            int mchance;    // magic

            if (p.getpClass() == PClass.PRIEST && p.getLevelNum() >= 4) {
                chance = 55;
                mchance = 75;
            } else {
                chance = 50;
                mchance = 50;
            }

            if (p.isSitting()) {
                chance += 20;
                mchance += 20;
            }

            // TODO OFL_REGENHEALTH && OFL_REGENMANA

            if (!p.isFighting()) {
                if (randperc() < chance) {
                    int strength = p.getStrength();
                    if (strength < p.getMaxStrength()) {
                        if (p.getMaxStrength() == strength + 1) {
                            world.sendToPlayerF(p, "&+GYour strength has returned to you.\n");
                        }
                        p.setStrength(strength + 1);
                    }
                }

                if (randperc() < mchance) {
                    int mana = p.getMana();
                    if (mana < p.getMaxMana()) {
                        if (p.getMaxMana() == mana + 1) {
                            world.sendToPlayerF(p, "&+MYour mana has been replenished.\n");
                        }
                        p.setMana(mana + 1);
                    }
                }
            }
        }
    }

}
