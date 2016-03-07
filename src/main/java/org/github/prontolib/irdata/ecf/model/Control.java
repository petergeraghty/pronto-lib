package org.github.prontolib.irdata.ecf.model;

import java.io.IOException;

import com.igormaznitsa.jbbp.io.JBBPBitInputStream;
import com.igormaznitsa.jbbp.io.JBBPOut;

public abstract class Control {

    public abstract void parse(JBBPBitInputStream input, int durationPrefix) throws IOException;

    public abstract JBBPOut deserialise(JBBPOut jbbpOut) throws IOException;

    public abstract int type();

}
