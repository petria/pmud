package org.freakz.pmud.pmudserver.pmud;

import lombok.extern.slf4j.Slf4j;
import org.freakz.pmud.common.enums.PClass;
import org.freakz.pmud.common.objects.Location;
import org.freakz.pmud.common.objects.Mobile;
import org.freakz.pmud.common.objects.PMudPlayer;
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
    private World world;


    @Scheduled(initialDelay = 2000, fixedRate = 2000)
    public void gameTick() {
        fight();
        reGeneratePlayers();
    }

    private void fight() {
        if (world.getFighterMap() != null) {
            for (Mobile attacker : world.getFighterMap().values()) {
                Mobile victim = attacker.getFightingTo();
                log.debug("Attack {} -> {}", attacker.getName(), victim.getName());
                hitPlayer(attacker, victim);
            }
        }
    }

    private void hitPlayer(Mobile attacker, Mobile victim) {
        int damage = 0;

        if (attacker.getWielded() != null) {

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
