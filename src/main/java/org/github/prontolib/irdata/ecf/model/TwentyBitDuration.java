package org.github.prontolib.irdata.ecf.model;

import static com.igormaznitsa.jbbp.io.JBBPOut.BeginBin;

import java.io.IOException;

import com.igormaznitsa.jbbp.io.JBBPBitInputStream;
import com.igormaznitsa.jbbp.io.JBBPByteOrder;
import com.igormaznitsa.jbbp.io.JBBPOut;

public abstract class TwentyBitDuration extends Control {

    private int duration;

    @Override
    public void parse(JBBPBitInputStream input, int durationPrefix) throws IOException {
        setDuration(durationPrefix * 0xffff + input.readUnsignedShort(JBBPByteOrder.BIG_ENDIAN));
    }

    @Override
    // TODO - should this be "serialise"?
    public JBBPOut deserialise(JBBPOut jbbpOut) throws IOException {
        byte[] array = BeginBin().Int((type() << 20) + getDuration()).End().toByteArray();
        return jbbpOut.Byte(array[1]).Byte(array[2]).Byte(array[3]);
    }

    public abstract String description();

    @Override
    public String toString() {
        return String.format(description(), getDuration() * .02);
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

}
