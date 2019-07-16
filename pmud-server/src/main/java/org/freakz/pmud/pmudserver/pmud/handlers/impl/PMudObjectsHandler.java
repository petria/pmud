package org.freakz.pmud.pmudserver.pmud.handlers.impl;

import org.freakz.pmud.common.objects.Location;
import org.freakz.pmud.common.objects.PMudPlayer;
import org.freakz.pmud.common.objects.PObject;
import org.freakz.pmud.pmudserver.pmud.VerbRequest;
import org.freakz.pmud.pmudserver.pmud.VerbResponse;
import org.freakz.pmud.pmudserver.pmud.handlers.AcceptVerbs;
import org.freakz.pmud.pmudserver.pmud.handlers.PMudVerbAcceptor;
import org.springframework.stereotype.Component;

@Component
@PMudVerbAcceptor
public class PMudObjectsHandler extends HandlerBase {

    @AcceptVerbs(verbs = {"inventory"})
    public void handleInventory(VerbRequest req, VerbResponse resp) {
        PMudPlayer p = player(req);
        resp.setToRoomF(location(req), "%s rummages through his backpack.\n", playerName(req));
        if (p.getCarried().values().size() > 0) {
            String msg = "Your backpack contains:\n";
            for (PObject o : p.getCarried().values()) {
                msg += o.getName() + " ";
            }
            msg += "\n";
            resp.setToSender(msg);
        } else {
            resp.setToSender("You are carrying nothing.\n");
        }
    }

    @AcceptVerbs(verbs = {"drop"})
    public void handleDrop(VerbRequest req, VerbResponse resp) {
        if (!hasArgs(req)) {
            resp.setToSender("Drop what?\n");
            return;
        }
        PMudPlayer p = player(req);
        Location l = location(req);
        PObject o = p.isCarrying(args(req));
        if (o != null) {
            if (l.hasPit()) {
                world.playerDropObjectPit(p, l, o);
                resp.setToRoomF(l, "%s drops the %s into the pit.\n", playerName(req), o.getName());
                resp.setToSenderF("The %s disappears into the bottomless pit.....\n", o.getName());
                int value = world.getObjectValue(o);
                world.addPlayerScore(p, value);
            } else {
                world.playerDropObject(p, l, o);
                resp.setToRoomF(l, "%s drops %s\n", playerName(req), o.getName());
                resp.setToSender("Ok\n");
            }
        } else {
            resp.setToSender("What's that?");
        }
    }

    @AcceptVerbs(verbs = {"close"})
    public void handleClose(VerbRequest req, VerbResponse resp) {
        if (!hasArgs(req)) {
            resp.setToSender("Close what?\n");
            return;
        }
        Location l = location(req);
        PObject o = l.getObject(args(req));
        if (o != null) {
            if (o.isOpenable()) {
                if (o.getState() == 1) {
                    resp.setToSender("It's already closed.\n");
                } else {
                    world.playerCloseObject(player(req), o);
                    resp.setToSenderF("The %s is now closed.\n", o.name());
                    resp.setToRoomF(o.getLocation(), "%s closes %s.\n", playerName(req), o.name());
                }
            } else {
                resp.setToSender("You can't close that!\n");
            }
        }
    }


    @AcceptVerbs(verbs = {"open"})
    public void handleOpen(VerbRequest req, VerbResponse resp) {
        if (!hasArgs(req)) {
            resp.setToSender("Open what?\n");
            return;
        }
        Location l = location(req);
        PObject o = l.getObject(args(req));
        if (o != null) {
            if (o.isOpenable()) {
                if (o.getState() == 0) {
                    resp.setToSender("It's already open.\n");
                } else {
                    world.playerOpenObject(player(req), o);
                    resp.setToSender("Done.\n");
                    resp.setToRoomF(o.getLocation(), "%s opens %s.\n", playerName(req), o.name());
                }
            } else {
                resp.setToSender("You can't open that!\n");
            }
        }
    }


    @AcceptVerbs(verbs = {"show"})
    public void handleShow(VerbRequest req, VerbResponse resp) {
        if (!hasArgs(req)) {
            resp.setToSender("Show what?\n");
            return;
        }
        PObject o = world.findClosestObject(player(req), args(req));
        if (o != null) {
            String msg = "";
            msg += String.format("Item [%d] : %s\n", o.getId(), o.name());
            msg += String.format("Location  : %s\n", o.location());
            msg += String.format("Zone/Owner: %s\n", o.getZone().getName());

            msg += "\n";

            msg += String.format("State : %-5d Max State: %-5d Vis Level: %d\n", o.getState(), o.getMaxState(), o.getVisibilityLevel());
            msg += String.format("Damage: %-5d Armor Class: %-5d Size: %-5d\n", o.getDamage(), o.getArmorClass(), o.getSize());
            msg += String.format("Base Value: %-15d Current Value: %-5d\n", o.getBaseValue(), world.getObjectValue(o));

            msg += "\n";

            msg += "Properties:";
//            for (String flag: o.getoFlags()) {
            msg += " " + o.getoFlags();
            //          }

            msg += "\n";

            msg += "State   Description\n";
            msg += String.format("[0]  %s\n", o.getDescription(0));
            msg += String.format("[1]  %s\n", o.getDescription(1));
            msg += String.format("[2]  %s\n", o.getDescription(2));
            msg += String.format("[3]  %s\n", o.getDescription(3));

            resp.setToSender(msg);

        } else {
            resp.setToSender("What's that?\n");
        }
    }

    @AcceptVerbs(verbs = {"wield"})
    public void handleWield(VerbRequest req, VerbResponse resp) {
        if (!hasArgs(req)) {
            resp.setToSender("What's that?\n");
            return;
        }

        PMudPlayer p = player(req);
        PObject o = p.isCarrying(args(req));
        if (o != null) {
            if (!o.isWeapon()) {
                resp.setToSender("It's not a weapon\n");
                return;
            }
            if (p.getWielded() != null) {
                p.removeWielded(o);
            }
            p.setWielded(o);
            resp.setToSenderF("You are now wielding the %s\n", o.name());
            resp.setToRoomF(p.getLocation(), "%s wield the %s", p.getName(), o.name());

        } else {
            resp.setToSender("What's that?\n");
        }
    }

    @AcceptVerbs(verbs = {"take"})
    public void handleTake(VerbRequest req, VerbResponse resp) {
        if (!hasArgs(req)) {
            resp.setToSender("Get what?\n");
            return;
        }
        Location l = location(req);
        PObject o = l.getObject(args(req));
        if (o.isNoGet()) {
            resp.setToSender("You cant take that, silly.\n");
            return;
        }


        if (o != null) {
            world.playerTakeObject(player(req), l, o);
            resp.setToRoomF(l, "%s takes %s.\n", playerName(req), o.getName());
            resp.setToSenderF("You take %s.\n", o.getName());
        } else {
            resp.setToSender("What's a " + args(req) + "?\n");
        }
    }


}
