package org.github.prontolib.rfx.message;

import java.io.IOException;

import com.igormaznitsa.jbbp.io.JBBPOut;

public class Stop extends Message {

    public static int TYPE = 0x4200;

    // 16 bits
    private int unknown8;

    // 8 bits
    private int startId;

    // 8 bits
    private int unknown9;

    public Stop() {
        setLength(8);
    }

    @Override
    public int type() {
        return TYPE;
    }

    @Override
    protected JBBPOut buildOutput(JBBPOut out) throws IOException {
        return super.buildOutput(out).Short(unknown8).Byte(startId).Byte(unknown9);
    }

    public int getUnknown8() {
        return unknown8;
    }

    public void setUnknown8(int unknown8) {
        this.unknown8 = unknown8;
    }

    public int getStartId() {
        return startId;
    }

    public void setStartId(int startId) {
        this.startId = startId;
    }

    public int getUnknown9() {
        return unknown9;
    }

    public void setUnknown9(int unknown9) {
        this.unknown9 = unknown9;
    }

}
