package org.github.prontolib.irdata.hex.parser;

import org.junit.Before;
import org.junit.Test;

import com.google.common.io.BaseEncoding;

public class HexParserTest {

    private String HEX_SONY_MUTE = "0000 0067 0000 000D 0060 0018 0018 0018 0018 0018 0030 0018 0018 0018 0030 0018 0018 0018 0018 0018 0030 0018 0018 0018 0018 0018 0018 0018 0018 041F";

    private HexParser testObj;

    @Before
    public void setUp() throws Exception {
        testObj = new HexParser();
    }

    @Test
    public void testDeserialise() throws Exception {
        byte[] binaryData = BaseEncoding.base16().decode(HEX_SONY_MUTE.replaceAll("\\s+", "").toUpperCase());
        testObj.deserialise(binaryData);
    }

}
