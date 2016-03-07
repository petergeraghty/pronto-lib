package org.github.prontolib.irdata.conversion;

import static java.lang.String.format;
import static org.junit.Assert.*;

import java.util.List;

import org.github.prontolib.irdata.ecf.model.CarrierSetup;
import org.github.prontolib.irdata.ecf.model.Control;
import org.github.prontolib.irdata.ecf.model.ProntoECF;
import org.github.prontolib.irdata.ecf.model.TwentyBitDuration;
import org.github.prontolib.irdata.ecf.parser.ECFParser;
import org.github.prontolib.irdata.hex.model.ProntoHex;
import org.github.prontolib.irdata.hex.parser.HexParser;
import org.junit.Before;
import org.junit.Test;

import com.google.common.io.BaseEncoding;

public class HexToECFTest {

    private static boolean STRICT = false;

    private HexToECF testObj;

    private String HEX_SONY_MUTE = "0000 0067 0000 000D 0060 0018 0018 0018 0018 0018 0030 0018 0018 0018 0030 0018 0018 0018 0018 0018 0030 0018 0018 0018 0018 0018 0018 0018 0018 041F";
    private String ECF_SONY_MUTE = "ffff 0060 0100 0008 0a04 da61 d1c0 4074 7a60 7470 4074 6060 7470 4074 6060 e8e0 4074 8a60 7470 4074 6060 e8e0 4074 8a60 7470 4074 6060 7470 4074 6060 e8e0 4074 8a60 7470 4074 6060 7470 4074 6060 7470 4074 6060 7470 5050 0040 0016 c300 0000";

    private String ECF_2 = "ffff 0078 0100 0008 0a04 da60 7470 4074 6060 7470 4074 6060 7470 4074 6060 7470 4074 6061 d1c0 4074 7a60 7470 4074 6060 7470 4074 6060 e8e0 4074 8a60 7470 4074 6060 e8e0 4074 8a60 7470 4074 6060 7470 4074 6060 e8e0 4074 8a60 7470 4074 6060 7470 4074 6060 7470 4074 6060 7470 5050 0040 0016 c300 001b";

    private String HEX_3 = "0000 0067 0004 000E 0018 0018 0018 0018 0018 0018 0018 0018 0060 0018 0018 0018 0018 0018 0030 0018 0018 0018 0030 0018 0018 0018 0018 0018 0030 0018 0018 0018 0018 0018 0018 0018 0018 0018 0018 041F";
    // private String ECF_3 = "ffff 007e 0100 0008 0a04 da60 7470 4074 6060 7470 4074 6060 7470 4074 6060 7470 4074 6061
    // d1c0 4074 7a60 7470 4074 6060 7470 4074 6060 e8e0 4074 8a60 7470 4074 6060 e8e0 4074 8a60 7470 4074 6060 7470
    // 4074 6060 e8e0 4074 8a60 7470 4074 6060 7470 4074 6060 7470 4074 6060 7470 4074 6060 7470 5050 0040 0016 c300
    // 001b 0000";

    private String ECF_3 = "ffff 007e 0100 0008 0a04 da60 7470 4074 6060 7470 4074 6060 7470 4074 6060 7470 4074 6061 d1c0 4074 7a60 7470 4074 6060 7470 4074 6060 e8e0 4074 8a60 7470 4074 6060 e8e0 4074 8a60 7470 4074 6060 7470 4074 6060 e8e0 4074 8a60 7470 4074 6060 7470 4074 6060 7470 4074 6060 7470 4074 6060 7470 5050 0040 0016 c300 001b 0000";
    private String ECF_4 = "ffff 007e 0100 0008 0a04 da60 7470 4074 6060 7470 4074 6060 7470 4074 6060 7470 4074 6061 d1c0 4074 7a60 7470 4074 6060 7470 4074 6060 e8e0 4074 8a60 7470 4074 6060 e8e0 4074 8a60 7470 4074 6060 7470 4074 6060 e8e0 4074 8a60 7470 4074 6060 7470 4074 6060 7470 4074 6060 7470 4074 6060 7470 5050 0040 0016 c300 001b 0000";

    // private String FETCH_

    @Before
    public void setUp() {
        testObj = new HexToECF();
    }

    @Test
    public void testSonyMute() throws Exception {
        check(HEX_SONY_MUTE, ECF_SONY_MUTE);
    }

    @Test
    public void testSonyOther() throws Exception {
        check(HEX_3, ECF_3);
    }

    public void check(String hex, String eexpected) throws Exception {

        HexParser hexParser = new HexParser();
        ECFParser ecfParser = new ECFParser();

        hex = hex.replaceAll("\\s", "").toUpperCase();
        byte[] binaryHexData = BaseEncoding.base16().decode(hex);
        ProntoHex prontoHex = hexParser.deserialise(binaryHexData);

        ProntoECF resultEcf = testObj.convert(prontoHex);

        ProntoECF expectedEcf = ecfParser
                .deserialise(BaseEncoding.base16().decode(eexpected.toUpperCase().replaceAll("\\s", "")));

        assertEquals(expectedEcf.getVersion(), resultEcf.getVersion());
        assertEquals(expectedEcf.getNumberOfBytes(), resultEcf.getNumberOfBytes());
        assertEquals(expectedEcf.getRepeatOffset(), resultEcf.getRepeatOffset());
        assertEquals(expectedEcf.getType(), resultEcf.getType());
        assertEquals(expectedEcf.getUnknown1(), resultEcf.getUnknown1());
        assertEquals(expectedEcf.getUnknown2(), resultEcf.getUnknown2());

        compareBursts(expectedEcf.getControlStream(), resultEcf.getControlStream());
    }

    private void compareBursts(List<Control> expectedControl, List<Control> resultControl) {
        assertEquals(expectedControl.size(), resultControl.size());
        for (int i = 0; i < expectedControl.size(); i++) {

            assertEquals(expectedControl.getClass(), resultControl.getClass());
            if (expectedControl instanceof TwentyBitDuration) {
                TwentyBitDuration expectedControlWithDuration = (TwentyBitDuration) expectedControl;
                TwentyBitDuration resultControlWithDuration = (TwentyBitDuration) resultControl;
                int expectedDuration = expectedControlWithDuration.getDuration();
                int resultDuration = resultControlWithDuration.getDuration();
                if (STRICT) {
                    assertEquals(expectedDuration, resultDuration);
                } else {
                    boolean matches = (resultDuration == expectedDuration - 50)
                            || (resultDuration == expectedDuration + 50) || (resultDuration == expectedDuration);
                    assertTrue(format("Expected first burst to be +-50 of %s but was %s", expectedDuration,
                            resultDuration), matches);
                }
            } else if (expectedControl instanceof CarrierSetup) {
                CarrierSetup expectedCarrierSetup = (CarrierSetup) expectedControl;
                CarrierSetup resultCarrierSetup = (CarrierSetup) resultControl;

                assertEquals(expectedCarrierSetup.getPeriod(), resultCarrierSetup.getPeriod());
                assertEquals(expectedCarrierSetup.getUnknown(), resultCarrierSetup.getUnknown());
            }

        }
    }

}
