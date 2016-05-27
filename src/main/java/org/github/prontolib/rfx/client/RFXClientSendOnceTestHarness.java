package org.github.prontolib.rfx.client;

import org.github.prontolib.irdata.conversion.HexToECF;
import org.github.prontolib.irdata.ecf.model.ProntoECF;
import org.github.prontolib.irdata.hex.model.ProntoHex;
import org.github.prontolib.irdata.hex.parser.HexParser;
import org.github.prontolib.rfx.RFXConfiguration;
import org.github.prontolib.rfx.discovery.RFXSearcher;

import com.google.common.io.BaseEncoding;

public class RFXClientSendOnceTestHarness {

    private static String HEX_SONY_MUTE = "0000 0067 0000 000D 0060 0018 0018 0018 0018 0018 0030 0018 0018 0018 0030 0018 0018 0018 0018 0018 0030 0018 0018 0018 0018 0018 0018 0018 0018 041F";

    public static void main(String[] args) throws Exception {
        RFXSearcher searcher = new RFXSearcher();
        RFXConfiguration rfxConfiguration = searcher.findExtenderWithId(0);
        RFXClient client = new RFXClient("testHarness", rfxConfiguration.getIpAddress());
        byte[] binaryHexData = BaseEncoding.base16().decode(HEX_SONY_MUTE.toUpperCase().replaceAll("\\s", ""));
        ProntoHex prontoHex = new HexParser().deserialise(binaryHexData);
        ProntoECF payload = new HexToECF().convert(prontoHex);

        client.sendOnce(payload, 3);
    }

}
