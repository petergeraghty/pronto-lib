package org.github.prontolib.rfx.message;

import static com.igormaznitsa.jbbp.io.JBBPOut.BeginBin;

import java.io.IOException;

import com.igormaznitsa.jbbp.io.JBBPOut;

public abstract class Message {

    // 16 bits
    private int unknown1;

    // 8 bits
    private int packetId;

    // 8 bits
    private int unknown2;

    // 16 bits
    private int unknown3;

    // 16 bits
    private int unknown4;

    // 16 bits
    private int unknown5;

    // 16 bits
    private int unknown6;

    // 16 bits
    // type();

    // 16 bits
    private int length;

    protected JBBPOut buildOutput(JBBPOut out) throws IOException {
        out.Short(unknown1).Byte(packetId).Byte(unknown2).Short(unknown3).Short(unknown4).Short(unknown5)
                .Short(unknown6).Short(type()).Short(length);
        return out;
    }

    public byte[] serialise() throws IOException {
        JBBPOut jbbpOut = BeginBin();
        buildOutput(jbbpOut);
        // TODO - add padding here and remove superfluous fields from messages
        // should be multiple of 4 bytes
        return jbbpOut.End().toByteArray();
    }

    public abstract int type();

    public int getUnknown1() {
        return unknown1;
    }

    public void setUnknown1(int unknown1) {
        this.unknown1 = unknown1;
    }

    public int getPacketId() {
        return packetId;
    }

    public void setPacketId(int packetId) {
        this.packetId = packetId;
    }

    public int getUnknown2() {
        return unknown2;
    }

    public void setUnknown2(int unknown2) {
        this.unknown2 = unknown2;
    }

    public int getUnknown3() {
        return unknown3;
    }

    public void setUnknown3(int unknown3) {
        this.unknown3 = unknown3;
    }

    public int getUnknown4() {
        return unknown4;
    }

    public void setUnknown4(int unknown4) {
        this.unknown4 = unknown4;
    }

    public int getUnknown5() {
        return unknown5;
    }

    public void setUnknown5(int unknown5) {
        this.unknown5 = unknown5;
    }

    public int getUnknown6() {
        return unknown6;
    }

    public void setUnknown6(int unknown6) {
        this.unknown6 = unknown6;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

}
