package org.github.prontolib.integrationtest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.github.prontolib.irdata.conversion.HexToECF;
import org.github.prontolib.irdata.ecf.model.ProntoECF;
import org.github.prontolib.irdata.hex.model.ProntoHex;
import org.github.prontolib.irdata.hex.parser.HexParser;
import org.github.prontolib.rfx.RFXConfiguration;
import org.github.prontolib.rfx.client.RFXClient;
import org.github.prontolib.rfx.discovery.RFXSearcher;
import org.junit.Test;

import com.google.common.io.BaseEncoding;

public class InfraredTests extends IntegrationTest {

    private static String HEX_SONY_MUTE = "0000 0067 0000 000D 0060 0018 0018 0018 0018 0018 0030 0018 0018 0018 0030 0018 0018 0018 0018 0018 0030 0018 0018 0018 0018 0018 0018 0018 0018 041F";

    @Test
    public void shouldSendInfraredPress() throws Exception {
        assumeTrue(isEnabled());
        RFXSearcher searcher = new RFXSearcher();
        RFXConfiguration rfxConfiguration = searcher.findExtenderWithId(1);
        RFXClient client = new RFXClient("testHarness", rfxConfiguration.getIpAddress());
        byte[] binaryHexData = BaseEncoding.base16().decode(HEX_SONY_MUTE.toUpperCase().replaceAll("\\s", ""));
        ProntoHex prontoHex = new HexParser().deserialise(binaryHexData);
        ProntoECF payload = new HexToECF().convert(prontoHex);

        ExecutorService executor = Executors.newFixedThreadPool(1);
        Future<String> infraredResponseChecker = executor.submit(new InfraredChecker());

        try {
            // client.sendOnce(payload, 0);
            client.startSending(payload, 0);

            Thread.sleep(5000);

            client.stopSending();

            assertEquals("KEY_MUTE\n", infraredResponseChecker.get());
        } finally {
            executor.shutdownNow();
        }
    }

    static class InfraredChecker implements Callable<String> {

        private ServerSocket serverSocket;

        public InfraredChecker() throws IOException {
            serverSocket = new ServerSocket(54321);
            serverSocket.setSoTimeout(2000);
        }

        @Override
        public String call() throws Exception {
            try (Socket socket = serverSocket.accept(); InputStream inputStream = socket.getInputStream()) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] content = new byte[2048];
                int bytesRead = -1;
                while ((bytesRead = inputStream.read(content)) != -1) {
                    baos.write(content, 0, bytesRead);
                }

                return baos.toString();
            } finally {
                serverSocket.close();
            }
        }

    }

}
