package org.freakz.pmud.pmudserver.pmud;

import org.freakz.pmud.common.objects.Location;
import org.freakz.pmud.common.objects.PMudPlayer;

public class VerbResponse {

    private final PMudPlayer player;

    private Location from;
    private String fromRoom = null;

    private Location to;
    private String toRoom = null;

    private String toSender = null;
    private String toWorld = null;

    boolean doQuit = false;

    public VerbResponse(PMudPlayer player) {
        this.player = player;
    }

    public PMudPlayer getPlayer() {
        return player;
    }

    public Location getFrom() {
        return from;
    }

    public Location getTo() {
        return to;
    }

    public String getFromRoom() {
        return fromRoom;
    }

    public void setFromRoom(Location location, String fromRoom) {
        this.from = location;
        this.fromRoom = fromRoom;
    }

    public void setFromRoomF(Location location, String format, String... args) {
        this.from = location;
        this.fromRoom = String.format(format, args);
    }

    public String getToRoom() {
        return toRoom;
    }

    public void setToRoom(Location location, String toRoom) {
        this.to = location;
        this.toRoom = toRoom;
    }

    public void setToRoomF(Location location, String format, String... args) {
        this.to = location;
        this.toRoom = String.format(format, args);
    }

    public String getToSender() {
        return toSender;
    }

    public void setToSender(String toSender) {
        this.toSender = toSender;
    }

    public void setToSenderF(String format, String... args) {
        this.toSender = String.format(format, args);
    }


    public String getToWorld() {
        return toWorld;
    }

    public void setToWorld(String toWorld) {
        this.toWorld = toWorld;
    }

    public boolean isDoQuit() {
        return doQuit;
    }

    public void setDoQuit(boolean doQuit) {
        this.doQuit = doQuit;
    }
}
