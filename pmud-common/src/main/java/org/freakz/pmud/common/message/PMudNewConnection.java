package org.freakz.pmud.common.message;

import java.io.Serializable;

public class PMudNewConnection implements Serializable {


    private final String remoteHost;

    private final long pid;

    public PMudNewConnection(String remoteHost, long pid) {
        this.remoteHost = remoteHost;
        this.pid = pid;
    }

    public String getRemoteHost() {
        return remoteHost;
    }

    public long getPid() {
        return pid;
    }

}
