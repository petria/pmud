package org.freakz.pmud.pmudserver.pmud;

import org.freakz.pmud.common.objects.PMudPlayer;

public class VerbResponse {

    private final PMudPlayer player;

    private String fromRoom;
    private String toRoom;
    private String toSender;

    public VerbResponse(PMudPlayer player) {
        this.player = player;
    }

    public PMudPlayer getPlayer() {
        return player;
    }

    public String getFromRoom() {
        return fromRoom;
    }

    public void setFromRoom(String fromRoom) {
        this.fromRoom = fromRoom;
    }

    public String getToRoom() {
        return toRoom;
    }

    public void setToRoom(String toRoom) {
        this.toRoom = toRoom;
    }

    public String getToSender() {
        return toSender;
    }

    public void setToSender(String toSender) {
        this.toSender = toSender;
    }
}
