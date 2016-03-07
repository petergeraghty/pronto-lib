package org.github.prontolib.irdata.ecf.model;

public class EndBurst extends TwentyBitDuration {

    public static int TYPE = 0x5;

    @Override
    public String description() {
        return "End of burst with total duration of %s microseconds";
    }

    @Override
    public int type() {
        return TYPE;
    }

}
