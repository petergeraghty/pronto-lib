package org.github.prontolib.irdata.ecf.model;

public class LEDOff extends TwentyBitDuration {

    public static int TYPE = 0x4;

    @Override
    public String description() {
        return "Turn LED off for %s microseconds";
    }

    @Override
    public int type() {
        return TYPE;
    }

}
