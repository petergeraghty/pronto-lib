package org.github.prontolib.rfx.message;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.google.common.io.BaseEncoding;

public class SerialTest {

    private static final String MESSAGE = "0000 0100 0000 0000 0000 0000 5002 001D 16C0 0000 0000 0000 0000 0001 A55B 0203 0100 0100 0000 0000 F900 0000";

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void test() throws Exception {
        byte[] payload = BaseEncoding.base16().decode("A55B02030100010000000000F9");
        Serial serial = new Serial();
        serial.setPacketId(1);
        serial.setPayload(payload);
        serial.setLength(payload.length + 16);
        serial.setPort(1);
        serial.setRate(BaudRate.RATE_19200);
        serial.setStopBits(StopBits.ONE);
        serial.setParity(Parity.NONE);
        serial.setDataBits(DataBits.EIGHT);
        byte[] serialised = serial.serialise();

        String message = BaseEncoding.base16().withSeparator(" ", 4).encode(serialised);
        System.out.println(message);

        assertEquals(MESSAGE, message);
    }

}
