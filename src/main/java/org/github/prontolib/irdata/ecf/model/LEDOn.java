package org.github.prontolib.irdata.ecf.model;

import java.io.IOException;

import com.igormaznitsa.jbbp.io.JBBPBitInputStream;
import com.igormaznitsa.jbbp.io.JBBPOut;

public class LEDOn extends Control {

    public static int TYPE = 0x8;

    private int duration;

    @Override
    public void parse(JBBPBitInputStream input, int durationPrefix) throws IOException {
        setDuration(durationPrefix * 0xff + input.readByte());
    }

    @Override
    public JBBPOut deserialise(JBBPOut jbbpOut) throws IOException {
        return jbbpOut.Short((type() << 12) + duration);
    }

    @Override
    public String toString() {
        return String.format("Turn LED on for %s microseconds", getDuration() * .02);
    }

    @Override
    public int type() {
        return TYPE;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

}
