package org.github.prontolib.irdata.ecf.model;

public class LEDModulate extends TwentyBitDuration {

    public static int TYPE = 0x6;

    @Override
    public String description() {
        return "Enable modulation of LED at carrier frequency for %s microseconds";
    }

    @Override
    public int type() {
        return TYPE;
    }

}
