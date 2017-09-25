package org.github.prontolib.rfx.message;

import java.io.IOException;

import com.igormaznitsa.jbbp.io.JBBPOut;

public class Serial extends Message {

    public static int TYPE = 0x5002;

    public Serial() {
    }

    @Override
    public int type() {
        return TYPE;
    }

    // 4bits
    private int port;

    // 4bits
    private BaudRate rate;

    // 8 bits total for next 3 fields
    private StopBits stopBits;
    private Parity parity;
    private DataBits dataBits;

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
    private int repeatCount = 1;

    // variable length
    private byte[] payload;

    // 16 bits
    private int unknown12;

    // 8 bits
    private int unknown13;

    @Override
    protected JBBPOut buildOutput(JBBPOut out) throws IOException {
        return super.buildOutput(out).Byte((port << 4) + rate.getFlag())
                .Byte(stopBits.getFlag() + parity.getFlag() + dataBits.getFlag()).Short(unknown8).Short(unknown9)
                .Short(timeout).Short(unknown11).Short(repeatCount).Byte(payload).Short(unknown12).Byte(unknown13);
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

    public byte[] getPayload() {
        return payload;
    }

    public void setPayload(byte[] payload) {
        this.payload = payload;
    }

    public BaudRate getRate() {
        return rate;
    }

    public void setRate(BaudRate rate) {
        this.rate = rate;
    }

    public StopBits getStopBits() {
        return stopBits;
    }

    public void setStopBits(StopBits stopBits) {
        this.stopBits = stopBits;
    }

    public Parity getParity() {
        return parity;
    }

    public void setParity(Parity parity) {
        this.parity = parity;
    }

    public DataBits getDataBits() {
        return dataBits;
    }

    public void setDataBits(DataBits dataBits) {
        this.dataBits = dataBits;
    }

    public int getRepeatCount() {
        return repeatCount;
    }

    public void setRepeatCount(int repeatCount) {
        this.repeatCount = repeatCount;
    }

}
