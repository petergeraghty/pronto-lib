package org.github.prontolib.rfx.message;

public enum BaudRate {

    RATE_2400(0x02),
    RATE_4800(0x03),
    RATE_9600(0x04),
    RATE_14400(0x05),
    RATE_19200(0x06),
    RATE_28800(0x07),
    RATE_31250(0x08),
    RATE_38400(0x09),
    RATE_57600(0x0a),
    RATE_115200(0x0b);

    private final int flag;

    private BaudRate(int flag) {
        this.flag = flag;
    }

    public int getFlag() {
        return flag;
    }

}
