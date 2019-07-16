package org.freakz.pmud.pmudserver.pmud;

import lombok.extern.slf4j.Slf4j;
import org.freakz.pmud.common.objects.Location;
import org.freakz.pmud.common.objects.Mobile;
import org.freakz.pmud.common.objects.PMudPlayer;
import org.freakz.pmud.common.util.PHelpers;
import org.freakz.pmud.pmudserver.World.World;
import org.freakz.pmud.pmudserver.service.MessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FightService {

    /* chance to hit various parts */

    private final static int HEAD_CHANCE = 10;
    private final static int RARM_CHANCE = 10;
    private final static int LARM_CHANCE = 10;
    private final static int RLEG_CHANCE = 10;
    private final static int LLEG_CHANCE = 10;
    private final static int RFOOT_CHANCE = 5;
    private final static int LFOOT_CHANCE = 5;
    private final static int RHAND_CHANCE = 5;
    private final static int LHAND_CHANCE = 5;
    private final static int CHEST_CHANCE = 10;
    private final static int BACK_CHANCE = 10;
    private final static int FACE_CHANCE = 5;
    private final static int NECK_CHANCE = 5;

    /* base chance to hit */
    private static final int CTH_BASE = 30;

    @Autowired
    private MessageSender sender;

    @Autowired
    private World world;


    public void handleFightTick() {
        if (world.getFighterMap() != null) {
            for (Mobile attacker : world.getFighterMap().values()) {
                Mobile victim = attacker.getFightingTo();

                boolean weapon = attacker.getWielded() != null;

                HitResult h = hitPlayer(attacker, victim);
                if (h.hitloc != BodyPart.NONE) {
//                    log.debug("Attack {} -> {} :: HIT TO: {} / {}", attacker.getName(), victim.getName(), h.hitloc.name, h.damage);

                    if (h.missed == true) {
                        if (!weapon) {
                            fightmsg(attacker, victim, h.hitloc, handmiss);
                        } else {
                            fightmsg(attacker, victim, h.hitloc, miss);
                        }
                        return;
                    }

                }
                int s = victim.getStrength();
                s -= h.damage;
                victim.setStrength(s);

                if (!weapon) {
                    if (h.damage == 0) {
                        fightmsg(attacker, victim, h.hitloc, handhit0);
                        return;
                    } else if (h.hitpct < 33)
                        fightmsg(attacker, victim, h.hitloc, handhit1);
                    else if (h.hitpct < 66)
                        fightmsg(attacker, victim, h.hitloc, handhit1);
                    else
                        fightmsg(attacker, victim, h.hitloc, handhit3);
                } else {
                    if (h.damage == 0) {
                        fightmsg(attacker, victim, h.hitloc, hit0);
                        return;
                    } else if (h.hitpct < 33)
                        fightmsg(attacker, victim, h.hitloc, hit1);
                    else if (h.hitpct < 66)
                        fightmsg(attacker, victim, h.hitloc, hit2);
                    else
                        fightmsg(attacker, victim, h.hitloc, hit3);
                }
                if (s < 0) {
                    fightmsg(attacker, victim, h.hitloc, death);
                    playerDied(attacker, victim, -1);
                }
            }
        }
    }

    private void playerDied(Mobile attacker, Mobile victim, int type) {
        log.debug("{} Killed: {}", attacker.getName(), victim.getName());
        world.stopFight(attacker);
    }

    void fightmsg(Mobile attacker, Mobile victim,
                  BodyPart area, String[] msg) {
        int i;

        i = my_random() % msg.length;
        generalmsg(attacker, victim, area, msg[i]);
    }

    void generalmsg(Mobile attacker, Mobile victim, BodyPart area, String msg) {

        String toAttacker = addCodes(attacker, victim, area, null, ATTACKER, msg) + "\n";

        String toVictim = addCodes(attacker, victim, area, null, VICTIM, msg) + "\n";

        String toOther = addCodes(attacker, victim, area, null, OTHER, msg) + "\n";

        if (attacker instanceof PMudPlayer) {
            String prompt = String.format("[Your strength is now %d/%d]\n", attacker.getStrength(), ((PMudPlayer) attacker).getMaxStrength());
            sender.sendReply((PMudPlayer) attacker, toAttacker, prompt);
        }

        if (victim instanceof PMudPlayer) {
            String prompt = String.format("[Your strength is now %d/%d]\n", attacker.getStrength(), ((PMudPlayer) victim).getMaxStrength());
            sender.sendReply((PMudPlayer) victim, toVictim, "FIGHT!");
        }

        world.sendToLocationF(attacker.getLocation(), attacker, victim, toOther);


//        log.debug("{}", toAttacker);

    }

    private final static int ATTACKER = 0;
    private final static int VICTIM = 1;
    private final static int OTHER = 2;


    /*
   %b ... body part
   %w ... weapon
   %a ... attacker's name
   %e ... attacker's name : posessive
   %g ... gender of attacker : his/her
   %q ... gender of defender : he/she
   %d ... defender's name
   %p ... defender's name : posessive
   %r ... armor or body part
   %s ... add an 's' if attacker
   %S ... same but capital
   %t ... add an 's' if victim
   %T ... same but capital
*/

    String addCodes(Mobile attacker, Mobile victim, BodyPart area,
                    String deststr, int type, String msg) {

        String weapon = "";
        if (attacker.getWielded() != null) {
            weapon = attacker.getWielded().name();
        }

        String buff = msg;

        buff = buff.replaceAll("%b", area.name);
        buff = buff.replaceAll("%w", weapon);
        if (type == ATTACKER) {
            buff = buff.replaceAll("%a", "you");
        } else {
            buff = buff.replaceAll("%a", attacker.getName());
        }
        if (type == ATTACKER) {
            buff = buff.replaceAll("%g", "your");
        } else {
            if (attacker.getSex() == Mobile.PSex.FEMALE) {
                buff = buff.replaceAll("%g", "her");
            } else {
                buff = buff.replaceAll("%g", "his");
            }
        }
        if (type == VICTIM) {
            buff = buff.replaceAll("%d", "you");
        } else {
            buff = buff.replaceAll("%d", victim.getName());
        }
        if (type == VICTIM) {
            buff = buff.replaceAll("%p", "your");
        } else {
            buff = buff.replaceAll("%p", victim.getName());
        }
        if (type == ATTACKER) {
            buff = buff.replaceAll("%e", "your");
        } else {
            buff = buff.replaceAll("%e", attacker.getName());
        }
        // %r
        if (type == VICTIM) {
            buff = buff.replaceAll("%r", String.format("your %s", area.name));
        } else {
            buff = buff.replaceAll("%r", String.format("%s %s", victim.getSex().herHis(), area.name));
        }
        // %s
        if (type == ATTACKER) {
            buff = buff.replaceAll("%s", "");
        } else {
            buff = buff.replaceAll("%s", "s");
        }
        // %t
        if (type == ATTACKER || type == OTHER) {
            buff = buff.replaceAll("%t", "s");
        } else {
            buff = buff.replaceAll("%t", "");
        }
        // %S
        if (type == ATTACKER) {
            buff = buff.replaceAll("%S", "");
        } else {
            buff = buff.replaceAll("%S", "S");
        }
        // %T
        if (type == ATTACKER || type == OTHER) {
            buff = buff.replaceAll("%T", "S");
        } else {
            buff = buff.replaceAll("%T", "");
        }
        // %q
        if (type == VICTIM) {
            buff = buff.replaceAll("%q", "you");
        } else {
            buff = buff.replaceAll("%q", victim.getSex().text());
        }

        return buff;
    }

    private int my_random() {
        int rnd = 1 + (int) (Math.random() * Integer.MAX_VALUE);
        return rnd;

    }


    class HitResult {
        BodyPart hitloc;
        int damage = 0;
        boolean missed = false;
        int hitpct;
    }

    private HitResult hitPlayer(Mobile attacker, Mobile victim) {

        log.debug("Hit attacker {} ->  victim {}", attacker.getName(), victim.getName());
        PBody b = PBody.create();

        HitResult hitResult = new HitResult();
        hitResult.hitloc = BodyPart.NONE;

        int val = 0;
        BodyPart hitloc = BodyPart.NONE;
        int locpct = PHelpers.randperc();
        int hitpct = PHelpers.randperc();

        boolean missed = false;
        int damage = 0;

        if (locpct < (val = HEAD_CHANCE) && b.head.isAttached())
            hitloc = b.head;
        else if (locpct < (val += RARM_CHANCE) && b.rightArm.isAttached())
            hitloc = b.rightArm;
        else if (locpct < (val += LARM_CHANCE) && b.leftArm.isAttached())
            hitloc = b.leftArm;
        else if (locpct < (val += RLEG_CHANCE) && b.rightLeg.isAttached())
            hitloc = b.rightLeg;
        else if (locpct < (val += LLEG_CHANCE) && b.leftLeg.isAttached())
            hitloc = b.leftLeg;
        else if (locpct < (val += RFOOT_CHANCE) &&
                b.rightFoot.isAttached() && b.rightLeg.isAttached())
            hitloc = b.rightFoot;
        else if (locpct < (val += LFOOT_CHANCE) &&
                b.leftFoot.isAttached() && b.leftLeg.isAttached())
            hitloc = b.leftFoot;
        else if (locpct < (val += RHAND_CHANCE) &&
                b.rightHand.isAttached() && b.rightArm.isAttached())
            hitloc = b.rightHand;
        else if (locpct < (val += LHAND_CHANCE) &&
                b.leftHand.isAttached() && b.leftArm.isAttached())
            hitloc = b.leftHand;
        else if (locpct < (val += CHEST_CHANCE) && b.chest.isAttached())
            hitloc = b.chest;
        else if (locpct < (val += BACK_CHANCE) && b.back.isAttached())
            hitloc = b.back;
        else if (locpct < (val += FACE_CHANCE) && b.face.isAttached())
            hitloc = b.face;
        else if (locpct < (val += NECK_CHANCE) && b.neck.isAttached())
            hitloc = b.neck;
        else
            return hitResult;

        float absorbval, hitval;

        if (hitpct - victim.getArmor() < CTH_BASE) {
            hitResult.missed = true;

        } else {
            // HIT OK
            hitval = (float) hitpct / 100;

            if (hitloc.hasArmor())
                absorbval = (float) (100 - hitloc.getArmor()) / 100;
            else
                absorbval = (float) 1;

            damage = (int) (hitval * attacker.getDamage() * absorbval);

            if (damage < 0) {
                damage = 0;
            }
            hitResult.hitloc = hitloc;
            hitResult.hitpct = hitpct;
            hitResult.damage = damage;

        }
        return hitResult;
    }

    private int parmor(Mobile victim) {
        return 0;
    }


    enum BodyPart {
        NONE(true, 0, 0, "NONE", null),
        RIGHT_LEG(true, 0, 0, "right leg", null),
        LEFT_LEG(true, 0, 0, "left leg", null),
        RIGHT_ARM(true, 0, 0, "right arm", null),
        LEFT_ARM(true, 0, 0, "left arm", null),
        RIGHT_FOOT(true, 0, 0, "right foot", null),
        LEFT_FOOT(true, 0, 0, "left arm", null),
        RIGHT_HAND(true, 0, 0, "right hand", null),
        LEFT_HAND(true, 0, 0, "left hand", null),
        HEAD(true, 0, 0, "head", null),
        CHEST(true, 0, 0, "chest", null),
        BACK(true, 0, 0, "back", null),
        FACE(true, 0, 0, "face", null),
        NECK(true, 0, 0, "neck", null),
        ;

        private final boolean attach;
        private final int armor;
        private final int vital;
        private final String name;
        private final Location location;

        BodyPart(boolean attach, int armor, int vital, String name, Location location) {
            this.attach = attach;
            this.armor = armor;
            this.vital = vital;
            this.name = name;
            this.location = location;
        }

        public boolean isAttached() {
            return attach;
        }

        public boolean hasArmor() {
            return this.armor != -1;
        }

        public int getArmor() {
            return this.armor;
        }
    }

    static class PBody {

        public static PBody create() {
            return new PBody();
        }

        BodyPart rightLeg = BodyPart.RIGHT_LEG;
        BodyPart leftLeg = BodyPart.LEFT_LEG;

        BodyPart rightArm = BodyPart.RIGHT_ARM;
        BodyPart leftArm = BodyPart.LEFT_ARM;

        BodyPart rightFoot = BodyPart.RIGHT_FOOT;
        BodyPart leftFoot = BodyPart.LEFT_FOOT;

        BodyPart rightHand = BodyPart.RIGHT_HAND;
        BodyPart leftHand = BodyPart.LEFT_HAND;

        BodyPart head = BodyPart.HEAD;
        BodyPart chest = BodyPart.CHEST;
        BodyPart back = BodyPart.BACK;
        BodyPart face = BodyPart.FACE;
        BodyPart neck = BodyPart.NECK;
    }

    String[] miss = {
            "%a swing%s %g %w at %p %b, missing entirely.",
            "%a lunge%s at %p %b but %q sidestep%t the blow.",
            "%d ward%t off a fierce attack!",
            "%d narrowly evade%t a vicous attack!",
            "%d parry%t %e bungled blow.",
            "%d dodge%t a mighty lunge!",
            "%a lunge%s at %d with %g %w, but %q parry%t the blow!",
            "%a run%s at %d with bright &+Rred&N eyes, but %q evade%t the attack.",
            "%a &+Yswing%s&N at %d with %g &+W%w&N, but %q sidestep%t %a!",
            "%a come%s charging at %d with the %w, but fail%s.",
            "%a come%s very close to hitting %d with %g %w.",
            "%a aim%s for %p %b but the attack fails.",
            "%a run%s at %p %b, but the attempt misses.",
            "%a swing%s %g %w at %p %b, but it it misses by an inch!",
            "%a ward%s off %p fierce attack to %r!",
            "%a lunge%s at %d with %g %w but does not contact!",
            "%a ferociously aim%s for %p %b but %g dodges the blow!",
            "%a &+Rgrowl%s&N, and run%s at %p %b, but the shot is deflected!"
    };

    String[] handmiss = {
            "%d evade%t %e weak punch.",
            "%a curl%s %g fist, charge%s at %p %b, and trip%s.",
            "%a attempt%s to headbutt %d but fall%s.",
            "%d easily parry%t a pathetic punch.",
            "%d narrowly dodge%t a blow to %p %b.",
            "%d luckily evade%t %e fierce hit to %r!"
    };

    String[] death = {
            "%a gutt%s %d with the %w!",
            "%a impale%s %d with %g %w!",
            "%a slice%s %d to bits with the %w! Oh, the humanity!!",
            "%a flay%s, tar%s and feather%s %d!",
            "%a hit%s %d with a fatal blow!"
    };

    String[] hit3 =

            {
                    "%a &+CTOTALLY MASSACRE%S&N %d with the %w!!!",
                    "%a &+CUTTERLY DESTROY%S&N %d with the %w!!!",
                    "%a &+CANNIALATE%S&N %d with %g %w!!!",
                    "%a &+CBEAT%S&N %d with %g %w!!!",
                    "%a &+CCUT%S&N %d up with %g %w!!!",
                    "%a &+CTEAR%S&N %d up with a forceful hit!!!"
            };

    String[] hit2 =

            {
                    "%a deliver%s a good hit to %p %b.",
                    "%a hit%s %p %b firmly with the &+Y%w.",
                    "%a pierce%s %p %b with an average hit from the &+Y%w.",
                    "%a hit%s %p %b hard with %e &+Y%w."
            };

    String[] hit1 =

            {
                    "%a hit%s %r weakly with the %w.",
                    "%a barely hit%s %r with the %w.",
                    "%a braise%s %p %b with the %w.",
                    "%a hit%s %r very weakly with the %w.",
                    "%a run%s at %r with the %w, but barely make%s contact."
            };

    String[] hit0 =

            {
                    "%a hit%s %d with %g %w, but it is absorbed by %r.",
                    "%r absorbs %e blow from the %w.",
                    "%a hit%s %d, but it is absorbed by %r."
            };

    String[] handhit3 =

            {
                    "%a hit%s %d with a powerful uppercut!",
                    "%a drive%s forward, hitting %d forcefully with %g hand!",
                    "%a deliver%s a mighty blow to %p %b!",
                    "%d reel%t as %a jab%s %p %b!",
                    "%d stagger%t as %a give%s a brutal punch!"
            };

    String[] handhit2 =

            {
                    "%a hit%s %d with %g hand.",
                    "%a attack%s %p %b with %g hand.",
                    "%a give%s %d a mediocre punch to the %b.",
                    "%a throw%s an average punch to %p %b.",
                    "%a connect%s with a decent cut to %p %b."
            };

    String[] handhit1 =

            {
                    "%a deliver%s a weak punch to %p %b.",
                    "%a barely hit%s %p %b with %g bare hands.",
                    "A bungled uppercut by %a barely braises %p %b.",
                    "A weak jab by %a barely touches %p %b."
            };

    String[] handhit0 =

            {
                    "%a hit%s %d, but it's absorbed by %r.",
                    "%a punch%s %p %b, but %r absorbs the blow.",
                    "%a weakly hit%s %p %b, but there is no effect."
            };

}

