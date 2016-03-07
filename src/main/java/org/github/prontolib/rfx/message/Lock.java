package org.github.prontolib.rfx.message;

import java.io.IOException;

import com.igormaznitsa.jbbp.io.JBBPOut;

public class Lock extends Message {

    public static int TYPE = 0x3000;

    // 16 bits
    private int unknown8;

    public Lock() {
    }

    // 16 bits
    private int timeout;

    // length - 8 bits
    private String owner;

    @Override
    public int type() {
        return TYPE;
    }

    @Override
    protected JBBPOut buildOutput(JBBPOut out) throws IOException {
        return super.buildOutput(out).Short(unknown8).Short(timeout).Utf8(owner);
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
        setLength(owner.length() * 8 + 8);
    }

    public int getUnknown8() {
        return unknown8;
    }

    public void setUnknown8(int unknown8) {
        this.unknown8 = unknown8;
    }

}
