package org.github.prontolib.irdata.conversion;

import java.io.IOException;
import java.util.List;

import org.github.prontolib.irdata.ecf.model.CarrierSetup;
import org.github.prontolib.irdata.ecf.model.Control;
import org.github.prontolib.irdata.ecf.model.EndBurst;
import org.github.prontolib.irdata.ecf.model.LEDModulate;
import org.github.prontolib.irdata.ecf.model.LEDOff;
import org.github.prontolib.irdata.ecf.model.ProntoECF;
import org.github.prontolib.irdata.ecf.model.Stop;
import org.github.prontolib.irdata.hex.model.BurstPair;
import org.github.prontolib.irdata.hex.model.ProntoHex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.io.BaseEncoding.DecodingException;

public class HexToECF {

    private static final Logger logger = LoggerFactory.getLogger(HexToECF.class);

    public ProntoECF convert(ProntoHex prontoHex) throws DecodingException, IOException {
        logger.debug(String.format("Converting HEX with period %s, initial burst length %s, repeat burst length %s",
                prontoHex.getPeriod(), prontoHex.getBurstLength(), prontoHex.getRepeatBurstLength()));

        ProntoECF prontoECF = new ProntoECF();

        int ecfPeriod = (int) Math.round(prontoHex.getPeriod() * 12.058);

        List<Control> controlStream = Lists.newArrayList();

        CarrierSetup carrierSetup = new CarrierSetup();
        carrierSetup.setUnknown(0x0a);
        carrierSetup.setPeriod(ecfPeriod);
        controlStream.add(carrierSetup);

        List<BurstPair> allBurstPairs = Lists.newArrayList(prontoHex.getBurst());
        allBurstPairs.addAll(prontoHex.getRepeatBurst());

        int duration = 0;
        int count = 0;
        int cumulativeDrift = 0;
        for (BurstPair burstPair : allBurstPairs) {
            count++;
            int first = burstPair.getFirst();
            int second = burstPair.getSecond();
            first = first * ecfPeriod;
            second = second * ecfPeriod;
            int drift = (first + second) % 50;
            cumulativeDrift = (cumulativeDrift + drift) % 50;
            logger.debug("Cumulative drift " + cumulativeDrift);
            if (cumulativeDrift > 25 && !(count == allBurstPairs.size() - 1)) {
                second = second + 50 - drift;
                logger.debug("Rounded up to " + second);
            } else {
                second = second - drift;

                logger.debug("Rounded down to " + second);
            }
            duration += first;

            if (count == allBurstPairs.size()) {
                duration += 2000 * 50;
                duration = (duration / 50);
                second = duration;
            } else {
                duration += second;
            }

            LEDModulate modulate = new LEDModulate();
            modulate.setDuration(first);
            controlStream.add(modulate);

            if (count == allBurstPairs.size()) {
                EndBurst end = new EndBurst();
                end.setDuration(second);
                controlStream.add(end);
            } else {
                LEDOff off = new LEDOff();
                off.setDuration(second);
                controlStream.add(off);
            }
        }

        // TODO, check if this is really needed
        LEDOff finalOff = new LEDOff();
        finalOff.setDuration(0x16);
        controlStream.add(finalOff);

        Stop stop = new Stop();
        stop.setDuration(0x30000);
        controlStream.add(stop);

        prontoECF.setControlStream(controlStream);
        int burstLength = prontoHex.getBurst().size() + prontoHex.getRepeatBurst().size();
        prontoECF.setNumberOfBytes(burstLength * 6 + 18);

        return prontoECF;
    }

}
