package org.github.prontolib.irdata.ecf.model;

public class Stop extends TwentyBitDuration {

    public static int TYPE = 0xc;

    @Override
    public String description() {
        return "Stop reading command stream";
    }

    @Override
    public int type() {
        return TYPE;
    }

}
