package org.github.prontolib.rfx.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

import org.github.prontolib.irdata.ecf.model.ProntoECF;
import org.github.prontolib.irdata.ecf.parser.ECFParser;
import org.github.prontolib.rfx.message.Continue;
import org.github.prontolib.rfx.message.Lock;
import org.github.prontolib.rfx.message.Message;
import org.github.prontolib.rfx.message.Response;
import org.github.prontolib.rfx.message.Start;
import org.github.prontolib.rfx.message.Stop;
import org.github.prontolib.rfx.message.Unlock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.BaseEncoding;
import com.google.common.io.BaseEncoding.DecodingException;

public class RFXClient {

    private static final int LOCK_PERIOD_MILLIS = 30000;
    private static final int SEND_REPEAT = 1500;
    private static final int SEND_ONCE = 0;
    private static final int RFX_UDP_PORT = 65442;

    private static final Logger logger = LoggerFactory.getLogger(RFXClient.class);

    private InetAddress host;
    DatagramSocket sock = null;
    int packetId = 200;

    public RFXClient(String hostName) {
        try {
            host = InetAddress.getByName(hostName);
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void send(ProntoECF payload, int irPort) throws InterruptedException, IOException {
        logger.debug("Sending ECF payload "
                + BaseEncoding.base16().withSeparator(" ", 4).encode(new ECFParser().serialise(payload)));

        try {
            // TODO - break up into startSend() and stopSend()
            sock = new DatagramSocket();

            sock.setSoTimeout(1000);

            Lock lockMessage = new Lock();
            lockMessage.setTimeout(LOCK_PERIOD_MILLIS);
            lockMessage.setOwner("pete");
            lockMessage.setLength(13);
            lockMessage.setPacketId(packetId);

            sendAndWaitForReply(lockMessage);

            int startPacketId = packetId;
            Start startMessage = new Start();
            startMessage.setPort(irPort);
            startMessage.setPayload(payload);
            startMessage.setLength(payload.getNumberOfBytes() + 16);
            // startMessage.setPacketId(packetId);
            startMessage.setTimeout(SEND_REPEAT); // set zero for no repeat

            sendAndWaitForReply(startMessage);

            for (int i = 0; i < 5; i++) {
                Thread.sleep(1000);
                Continue continueMessage = new Continue();
                continueMessage.setTimeout(SEND_REPEAT);
                continueMessage.setResumeId(startPacketId);

                sendAndWaitForReply(continueMessage);
            }

            Thread.sleep(1000);

            Stop stopMessage = new Stop();
            stopMessage.setStartId(startPacketId);
            stopMessage.setPacketId(packetId);

            sendAndWaitForReply(stopMessage);

            Unlock unlockMessage = new Unlock();
            unlockMessage.setOwner("pete");
            unlockMessage.setLength(9);
            unlockMessage.setPacketId(packetId);

            sendAndWaitForReply(unlockMessage);

            sock.close();

        }

        catch (IOException e) {
            System.err.println("IOException " + e);
        }
    }

    public void sendOnce(ProntoECF payload, int irPort) throws DecodingException, InterruptedException {
        try {
            logger.debug("Sending one-off code");
            sock = new DatagramSocket();

            sock.setSoTimeout(1000);

            Lock lockMessage = new Lock();
            lockMessage.setTimeout(LOCK_PERIOD_MILLIS);
            lockMessage.setOwner("pete");
            lockMessage.setLength(13);

            sendAndWaitForReply(lockMessage);

            Start startMessage = new Start();
            startMessage.setPort(irPort);
            startMessage.setPayload(payload);
            startMessage.setLength(payload.getNumberOfBytes() + 16);
            startMessage.setTimeout(SEND_ONCE);

            System.out.println(BaseEncoding.base16().withSeparator(" ", 4).encode(new ECFParser().serialise(payload)));

            sendAndWaitForReply(startMessage);

            // Thread.sleep(1000);

            Unlock unlockMessage = new Unlock();
            unlockMessage.setOwner("pete");
            unlockMessage.setLength(9);

            sendAndWaitForReply(unlockMessage);

            sock.close();

        }

        catch (IOException e) {
            System.err.println("IOException " + e);
        }
    }

    private void sendAndWaitForReply(Message message) throws IOException, DecodingException {
        message.setPacketId(packetId);

        byte[] binaryData = message.serialise();

        logger.debug("Sending message " + BaseEncoding.base16().withSeparator(" ", 4).encode(binaryData));

        DatagramPacket dp = new DatagramPacket(binaryData, binaryData.length, host, RFX_UDP_PORT);
        sock.send(dp);

        waitForReply(sock, packetId++);

    }

    private static void waitForReply(DatagramSocket sock, int packetId) throws IOException, DecodingException {
        long start = System.currentTimeMillis();
        while (true) {
            // TODO throw exception on timeout
            byte[] buffer = new byte[20]; // TODO, what is maximum size reply?
            DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
            sock.receive(reply);

            Response response = Response.deserialise(reply.getData());

            logger.debug("Got response "
                    + BaseEncoding.base16().withSeparator(" ", 4).encode(Arrays.copyOf(reply.getData(), 20)));

            logger.debug("Response type " + response.getType());
            boolean idMatch = packetId == response.getPacketId();

            if (!idMatch) {
                logger.debug(String.format("Did not match packet ids, wanted %s but was %s", packetId,
                        response.getPacketId()));
            }

            if ((idMatch && response.getType() == 32) || (System.currentTimeMillis() - start) > 2000) {
                break;
            }
        }
    }

}
