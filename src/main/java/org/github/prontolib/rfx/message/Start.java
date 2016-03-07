package org.github.prontolib.rfx.message;

import java.io.IOException;

import org.github.prontolib.irdata.ecf.model.ProntoECF;
import org.github.prontolib.irdata.ecf.parser.ECFParser;

import com.igormaznitsa.jbbp.io.JBBPOut;

public class Start extends Message {

    public static int TYPE = 0x4000;

    public Start() {
    }

    @Override
    public int type() {
        return TYPE;
    }

    // 8bits
    private int port;

    // 8 bits
    private int unknown7;

    // 16 bits
    private int unknown8;

    // 16 bits
    private int unknown9;

    // 16 bits
    private int timeout;

    // 16 bits
    private int unknown11;

    // 16 bits
    private int unknown12;

    // variable length
    private ProntoECF payload;

    @Override
    protected JBBPOut buildOutput(JBBPOut out) throws IOException {
        return super.buildOutput(out).Byte(port).Byte(unknown7).Short(unknown8).Short(unknown9).Short(timeout)
                .Short(unknown11).Short(unknown12).Byte(new ECFParser().serialise(payload));
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getUnknown7() {
        return unknown7;
    }

    public void setUnknown7(int unknown7) {
        this.unknown7 = unknown7;
    }

    public int getUnknown8() {
        return unknown8;
    }

    public void setUnknown8(int unknown8) {
        this.unknown8 = unknown8;
    }

    public int getUnknown9() {
        return unknown9;
    }

    public void setUnknown9(int unknown9) {
        this.unknown9 = unknown9;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getUnknown11() {
        return unknown11;
    }

    public void setUnknown11(int unknown11) {
        this.unknown11 = unknown11;
    }

    public int getUnknown12() {
        return unknown12;
    }

    public void setUnknown12(int unknown12) {
        this.unknown12 = unknown12;
    }

    public ProntoECF getPayload() {
        return payload;
    }

    public void setPayload(ProntoECF payload) {
        this.payload = payload;
    }

}
