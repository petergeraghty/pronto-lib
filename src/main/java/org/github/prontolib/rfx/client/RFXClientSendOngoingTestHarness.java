package org.github.prontolib.rfx.client;

import org.github.prontolib.irdata.conversion.HexToECF;
import org.github.prontolib.irdata.ecf.model.ProntoECF;
import org.github.prontolib.irdata.hex.model.ProntoHex;
import org.github.prontolib.irdata.hex.parser.HexParser;
import org.github.prontolib.rfx.RFXConfiguration;
import org.github.prontolib.rfx.discovery.RFXSearcher;

import com.google.common.io.BaseEncoding;

public class RFXClientSendOngoingTestHarness {

    private static String HEX_SONY_VOL_UP = "0000 0067 0000 000d 0060 0018 0018 0018 0030 0018 0018 0018 0018 0018 0030 0018 0018 0018 0018 0018 0030 0018 0018 0018 0018 0018 0018 0018 0018 0420";

    public static void main(String[] args) throws Exception {
        RFXSearcher searcher = new RFXSearcher();
        RFXConfiguration rfxConfiguration = searcher.findExtenderWithId(0);
        RFXClient client = new RFXClient(rfxConfiguration.getIpAddress());
        byte[] binaryHexData = BaseEncoding.base16().decode(HEX_SONY_VOL_UP.toUpperCase().replaceAll("\\s", ""));
        ProntoHex prontoHex = new HexParser().deserialise(binaryHexData);
        ProntoECF payload = new HexToECF().convert(prontoHex);

        client.send(payload, 0);
    }

}
