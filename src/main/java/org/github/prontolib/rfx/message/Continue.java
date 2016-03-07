package org.github.prontolib.rfx.message;

import java.io.IOException;

import com.igormaznitsa.jbbp.io.JBBPOut;

public class Continue extends Message {

    public static int TYPE = 0x4100;

    // 16 bits
    private int unknown8;

    // 8 bits
    private int resumeId;

    // 8 bits
    private int unknown9;

    // 16 bits
    private int unknown10;

    // 16 bits
    private int timeout;

    public Continue() {
        setLength(12);
    }

    @Override
    public int type() {
        return TYPE;
    }

    @Override
    protected JBBPOut buildOutput(JBBPOut out) throws IOException {
        return super.buildOutput(out).Short(unknown8).Byte(resumeId).Byte(unknown9).Short(unknown10).Short(timeout);
    }

    public int getResumeId() {
        return resumeId;
    }

    public void setResumeId(int resumeId) {
        this.resumeId = resumeId;
    }

    public int getUnknown9() {
        return unknown9;
    }

    public void setUnknown9(int unknown9) {
        this.unknown9 = unknown9;
    }

    public int getUnknown10() {
        return unknown10;
    }

    public void setUnknown10(int unknown10) {
        this.unknown10 = unknown10;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getUnknown8() {
        return unknown8;
    }

    public void setUnknown8(int unknown8) {
        this.unknown8 = unknown8;
    }

}
