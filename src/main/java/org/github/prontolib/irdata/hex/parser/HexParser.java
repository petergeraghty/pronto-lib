package org.github.prontolib.irdata.hex.parser;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static com.igormaznitsa.jbbp.io.JBBPByteOrder.BIG_ENDIAN;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.github.prontolib.irdata.ecf.model.CarrierSetup;
import org.github.prontolib.irdata.ecf.model.Control;
import org.github.prontolib.irdata.ecf.model.EndBurst;
import org.github.prontolib.irdata.ecf.model.LEDModulate;
import org.github.prontolib.irdata.ecf.model.LEDOff;
import org.github.prontolib.irdata.ecf.model.LEDOn;
import org.github.prontolib.irdata.ecf.model.Stop;
import org.github.prontolib.irdata.hex.model.BurstPair;
import org.github.prontolib.irdata.hex.model.ProntoHex;

import com.igormaznitsa.jbbp.io.JBBPBitInputStream;
import com.igormaznitsa.jbbp.io.JBBPBitOrder;

public class HexParser {

    private static Map<Integer, Class<? extends Control>> controlTypeMap;

    static {
        controlTypeMap = newHashMap();
        controlTypeMap.put(0x0, CarrierSetup.class);
        controlTypeMap.put(0x4, LEDOff.class);
        controlTypeMap.put(0x5, EndBurst.class);
        controlTypeMap.put(0x6, LEDModulate.class);
        controlTypeMap.put(0x8, LEDOn.class);
        controlTypeMap.put(0xc, Stop.class);
    }

    public ProntoHex deserialise(byte[] binaryData) throws IOException, InstantiationException, IllegalAccessException {
        ProntoHex result = new ProntoHex();
        JBBPBitInputStream bitInputStream = new JBBPBitInputStream(new ByteArrayInputStream(binaryData),
                JBBPBitOrder.LSB0);
        result.setType(bitInputStream.readUnsignedShort(BIG_ENDIAN));
        result.setPeriod(bitInputStream.readUnsignedShort(BIG_ENDIAN));
        result.setBurstLength(bitInputStream.readUnsignedShort(BIG_ENDIAN));
        result.setRepeatBurstLength(bitInputStream.readUnsignedShort(BIG_ENDIAN));

        result.setBurst(parseBurstPairs(bitInputStream, result.getBurstLength()));
        result.setRepeatBurst(parseBurstPairs(bitInputStream, result.getRepeatBurstLength()));

        return result;
    }

    private List<BurstPair> parseBurstPairs(JBBPBitInputStream bitInputStream, int length) throws IOException {
        List<BurstPair> burstPairs = newArrayList();

        for (int i = 0; i < length; i++) {
            BurstPair burstPair = new BurstPair();
            burstPair.setFirst(bitInputStream.readUnsignedShort(BIG_ENDIAN));
            burstPair.setSecond(bitInputStream.readUnsignedShort(BIG_ENDIAN));
            burstPairs.add(burstPair);
        }

        return burstPairs;
    }

}
