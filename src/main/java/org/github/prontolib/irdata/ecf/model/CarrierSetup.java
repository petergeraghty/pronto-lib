package org.github.prontolib.irdata.ecf.model;

import java.io.IOException;

import com.igormaznitsa.jbbp.io.JBBPBitInputStream;
import com.igormaznitsa.jbbp.io.JBBPByteOrder;
import com.igormaznitsa.jbbp.io.JBBPOut;

public class CarrierSetup extends Control {

    public static int TYPE = 0x0;

    int unknown;
    int period;

    @Override
    public void parse(JBBPBitInputStream input, int durationPrefix) throws IOException {
        unknown = durationPrefix;
        period = input.readUnsignedShort(JBBPByteOrder.BIG_ENDIAN);
    }

    @Override
    public JBBPOut deserialise(JBBPOut jbbpOut) throws IOException {
        return jbbpOut.Byte(unknown).Short(period);
    }

    @Override
    public int type() {
        return TYPE;
    }

    public int getUnknown() {
        return unknown;
    }

    public void setUnknown(int unknown) {
        this.unknown = unknown;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

}
