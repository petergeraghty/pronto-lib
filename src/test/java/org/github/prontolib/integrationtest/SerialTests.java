package org.github.prontolib.integrationtest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.github.prontolib.rfx.RFXConfiguration;
import org.github.prontolib.rfx.client.RFXClient;
import org.github.prontolib.rfx.discovery.RFXSearcher;
import org.junit.Test;

import com.google.common.io.BaseEncoding;

public class SerialTests extends IntegrationTest {

    private static final String SERIAL_PAYLOAD = "A55B02030100010000000000F9";

    @Test
    public void shouldSendSerialData() throws Exception {
        assumeTrue(isEnabled());
        RFXSearcher searcher = new RFXSearcher();
        RFXConfiguration rfxConfiguration = searcher.findExtenderWithId(1);
        RFXClient client = new RFXClient("testHarness", rfxConfiguration.getIpAddress());

        byte[] payload = BaseEncoding.base16().decode(SERIAL_PAYLOAD);

        ExecutorService executor = Executors.newFixedThreadPool(1);
        Future<String> serialResponseChecker = executor.submit(new SerialResponseChecker(SERIAL_PAYLOAD));

        try {
            client.sendSerial(payload, 0);

            assertEquals(SERIAL_PAYLOAD, serialResponseChecker.get());
        } finally {
            executor.shutdownNow();
        }
    }

    static class SerialResponseChecker implements Callable<String> {

        private Socket socket;
        private InputStream inputStream;
        private String expected;

        public SerialResponseChecker(String expected) throws IOException {
            this.expected = expected;
            socket = new Socket("192.168.0.41", 54321);
            socket.setSoTimeout(2000);
            inputStream = socket.getInputStream();
        }

        @Override
        public String call() throws Exception {
            try {
                String received = null;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] content = new byte[2048];
                int bytesRead = -1;
                while ((bytesRead = inputStream.read(content)) != -1) {
                    baos.write(content, 0, bytesRead);
                    received = BaseEncoding.base16().encode(baos.toByteArray());
                    if (expected.equals(received)) {
                        break;
                    }
                }

                return received;
            } finally {
                inputStream.close();
                socket.close();
            }
        }

    }

}
