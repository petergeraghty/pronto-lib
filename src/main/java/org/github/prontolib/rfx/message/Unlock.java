package org.github.prontolib.rfx.message;

import java.io.IOException;

import com.igormaznitsa.jbbp.io.JBBPOut;

public class Unlock extends Message {

    public static int TYPE = 0x3100;

    public Unlock() {
    }

    @Override
    public int type() {
        return TYPE;
    }

    // (length - 4) bits
    private String owner;

    @Override
    protected JBBPOut buildOutput(JBBPOut out) throws IOException {
        return super.buildOutput(out).Utf8(owner);
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
        setLength(owner.length() + 4);
    }

}
