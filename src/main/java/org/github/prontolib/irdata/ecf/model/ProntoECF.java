package org.github.prontolib.irdata.ecf.model;

import java.util.List;

public class ProntoECF {

    // 16 bits
    private int type = 0xffff;

    // 16 bits
    private int numberOfBytes;

    // 8 bits
    private int version = 1;

    // 16 bits
    private int unknown1;

    // 8 bits
    private int unknown2 = 0x08;

    // (numberOfBytes * 8) bits total. Variable size each
    private List<Control> controlStream;

    // 8 bits if present
    private int repeatOffset;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getNumberOfBytes() {
        return numberOfBytes;
    }

    public void setNumberOfBytes(int numberOfBytes) {
        this.numberOfBytes = numberOfBytes;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getUnknown1() {
        return unknown1;
    }

    public void setUnknown1(int unknown1) {
        this.unknown1 = unknown1;
    }

    public int getUnknown2() {
        return unknown2;
    }

    public void setUnknown2(int unknown2) {
        this.unknown2 = unknown2;
    }

    public List<Control> getControlStream() {
        return controlStream;
    }

    public void setControlStream(List<Control> controlStream) {
        this.controlStream = controlStream;
    }

    public int getRepeatOffset() {
        return repeatOffset;
    }

    public void setRepeatOffset(int repeatOffset) {
        this.repeatOffset = repeatOffset;
    }

}
