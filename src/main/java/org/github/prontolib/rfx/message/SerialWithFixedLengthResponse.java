package org.github.prontolib.rfx.message;

import java.io.IOException;

import com.igormaznitsa.jbbp.io.JBBPOut;

public class SerialWithFixedLengthResponse extends Serial {

    public SerialWithFixedLengthResponse() {
    }

    // // 4bits
    // private int port;
    //
    // // 4bits
    // private BaudRate rate;
    //
    // // 8 bits total for next 3 fields
    // private StopBits stopBits;
    // private Parity parity;
    // private DataBits dataBits;
    //
    // // 8 bits
    // private int unknown7;
    //
    // // 16 bits
    // private int unknown8;
    //
    // // 16 bits
    // private int unknown9;
    //
    // // 16 bits
    // private int timeout;
    //
    // // 16 bits
    // private int unknown11;
    //
    // // 16 bits
    // private int repeatCount = 1;
    //
    // // variable length
    // private byte[] payload;
    //
    // // 16 bits
    // private int unknown12;
    //
    // // 8 bits
    // private int unknown13;

    @Override
    protected JBBPOut buildOutput(JBBPOut out) throws IOException {
        int subType = 0x5300;
        int subLength = 12;
        int expectedResponseLength = 13;
        int responseTimeout = 1000;
        return super.buildOutput(out).Short(subType).Short(subLength).Byte((getPort() << 4) + getRate().getFlag())
                .Byte(getStopBits().getFlag() + getParity().getFlag() + getDataBits().getFlag())
                .Short(expectedResponseLength).Short(0).Short(responseTimeout);
    }

}
