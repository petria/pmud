package org.freakz.pmud.pmudserver.pmud;

import org.freakz.pmud.common.objects.PMudPlayer;
import org.freakz.pmud.common.util.CommandArgs;

public class VerbRequest {


    private final String line;
    private final CommandArgs args;
    private PMudPlayer player;


    public VerbRequest(String line, PMudPlayer player) {
        this.line = line;
        this.args = new CommandArgs(line, player);
        this.player = player;
    }


    public void setPlayer(PMudPlayer player) {
        this.player = player;
    }

    public PMudPlayer getPlayer() {
        return this.player;
    }

    public CommandArgs getArgs() {
        return args;
    }
}
