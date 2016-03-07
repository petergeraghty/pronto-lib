package org.github.prontolib.irdata.ecf.parser;

import static com.google.common.collect.Maps.newHashMap;
import static com.igormaznitsa.jbbp.io.JBBPByteOrder.BIG_ENDIAN;
import static com.igormaznitsa.jbbp.io.JBBPOut.BeginBin;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.github.prontolib.irdata.ecf.model.CarrierSetup;
import org.github.prontolib.irdata.ecf.model.Control;
import org.github.prontolib.irdata.ecf.model.EndBurst;
import org.github.prontolib.irdata.ecf.model.LEDModulate;
import org.github.prontolib.irdata.ecf.model.LEDOff;
import org.github.prontolib.irdata.ecf.model.LEDOn;
import org.github.prontolib.irdata.ecf.model.ProntoECF;
import org.github.prontolib.irdata.ecf.model.Stop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.igormaznitsa.jbbp.io.JBBPBitInputStream;
import com.igormaznitsa.jbbp.io.JBBPBitNumber;
import com.igormaznitsa.jbbp.io.JBBPBitOrder;
import com.igormaznitsa.jbbp.io.JBBPOut;

public class ECFParser {

    private static final Logger logger = LoggerFactory.getLogger(ECFParser.class);

    private static Map<Integer, Class<? extends Control>> controlTypeMap;

    static {
        controlTypeMap = newHashMap();
        controlTypeMap.put(CarrierSetup.TYPE, CarrierSetup.class);
        controlTypeMap.put(LEDOff.TYPE, LEDOff.class);
        controlTypeMap.put(EndBurst.TYPE, EndBurst.class);
        controlTypeMap.put(LEDModulate.TYPE, LEDModulate.class);
        controlTypeMap.put(LEDOn.TYPE, LEDOn.class);
        controlTypeMap.put(Stop.TYPE, Stop.class);
    }

    public ProntoECF deserialise(byte[] binaryData) throws IOException, InstantiationException, IllegalAccessException {
        ProntoECF result = new ProntoECF();
        JBBPBitInputStream bitInputStream = new JBBPBitInputStream(new ByteArrayInputStream(binaryData),
                JBBPBitOrder.LSB0);
        result.setType(bitInputStream.readUnsignedShort(BIG_ENDIAN));
        result.setNumberOfBytes(bitInputStream.readUnsignedShort(BIG_ENDIAN));
        result.setVersion(bitInputStream.readByte());
        result.setUnknown1(bitInputStream.readUnsignedShort(BIG_ENDIAN));
        result.setUnknown2(bitInputStream.readByte());

        result.setControlStream(new ArrayList<Control>());
        while (bitInputStream.getCounter() < result.getNumberOfBytes() - 1) {

            int durationPrefix = bitInputStream.readBits(JBBPBitNumber.BITS_4);
            int controlType = bitInputStream.readBits(JBBPBitNumber.BITS_4);
            Control control = controlTypeMap.get(controlType).newInstance();
            control.parse(bitInputStream, durationPrefix);
            result.getControlStream().add(control);

            logger.debug("Added control " + control.toString());
        }

        return result;
    }

    public byte[] serialise(ProntoECF prontoECF) throws IOException {
        JBBPOut jbbpOut = BeginBin().Short(prontoECF.getType()).Short(prontoECF.getNumberOfBytes())
                .Byte(prontoECF.getVersion()).Short(prontoECF.getUnknown1()).Byte(prontoECF.getUnknown2());

        for (Control control : prontoECF.getControlStream()) {
            jbbpOut = control.deserialise(jbbpOut);
        }

        return jbbpOut.Byte(prontoECF.getRepeatOffset()).End().toByteArray();

    }

}
