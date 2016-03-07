package org.github.prontolib.rfx.client;

import org.github.prontolib.irdata.conversion.HexToECF;
import org.github.prontolib.irdata.ecf.model.ProntoECF;
import org.github.prontolib.irdata.hex.model.ProntoHex;
import org.github.prontolib.irdata.hex.parser.HexParser;
import org.junit.Before;
import org.junit.Test;

import com.google.common.io.BaseEncoding;

public class RFXClientTest {

    private static String HEX_SONY_MUTE = "0000 0067 0000 000D 0060 0018 0018 0018 0018 0018 0030 0018 0018 0018 0030 0018 0018 0018 0018 0018 0030 0018 0018 0018 0018 0018 0018 0018 0018 041F";

    private static String HEX_SONY_VOL_UP = "0000 0067 0000 000d 0060 0018 0018 0018 0030 0018 0018 0018 0018 0018 0030 0018 0018 0018 0018 0018 0030 0018 0018 0018 0018 0018 0018 0018 0018 0420";
    private String ECF_SONY_MUTE = "ffff 0060 0100 0008 0a04 da61 d1c0 4074 7a60 7470 4074 6060 7470 4074 6060 e8e0 4074 8a60 7470 4074 6060 e8e0 4074 8a60 7470 4074 6060 7470 4074 6060 e8e0 4074 8a60 7470 4074 6060 7470 4074 6060 7470 4074 6060 7470 5050 0040 0016 c300 0000";

    private RFXClient testObj;

    @Before
    public void setUp() throws Exception {
        testObj = new RFXClient("192.168.0.28", 65442);
    }

    @Test
    public void test() throws Exception {
        byte[] binaryHexData = BaseEncoding.base16().decode(HEX_SONY_MUTE.toUpperCase().replaceAll("\\s", ""));
        ProntoHex prontoHex = new HexParser().deserialise(binaryHexData);
        ProntoECF payload = new HexToECF().convert(prontoHex);

        testObj.sendOnce(payload);
    }

    // @Test
    // public void testLong() throws Exception {
    // byte[] binaryHexData = BaseEncoding.base16().decode(HEX_SONY_VOL_UP.toUpperCase().replaceAll("\\s", ""));
    // ProntoHex prontoHex = new HexParser().deserialise(binaryHexData);
    // ProntoECF payload = new HexToECF().convert(prontoHex);
    //
    // testObj.send(payload);
    // }

}
