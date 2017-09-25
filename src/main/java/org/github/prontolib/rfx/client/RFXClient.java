package org.github.prontolib.rfx.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.github.prontolib.exception.ProntoException;
import org.github.prontolib.irdata.ecf.model.ProntoECF;
import org.github.prontolib.irdata.ecf.parser.ECFParser;
import org.github.prontolib.rfx.message.BaudRate;
import org.github.prontolib.rfx.message.Continue;
import org.github.prontolib.rfx.message.DataBits;
import org.github.prontolib.rfx.message.Lock;
import org.github.prontolib.rfx.message.Message;
import org.github.prontolib.rfx.message.Parity;
import org.github.prontolib.rfx.message.Response;
import org.github.prontolib.rfx.message.Serial;
import org.github.prontolib.rfx.message.Start;
import org.github.prontolib.rfx.message.Stop;
import org.github.prontolib.rfx.message.StopBits;
import org.github.prontolib.rfx.message.Unlock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.BaseEncoding;

public class RFXClient {

    private static final int RESPONSE_TIMEOUT = 2000;
    static final int RESPONSE_TYPE_ACK = 32;
    private static final int LOCK_PERIOD_MILLIS = 30000;
    private static final int SEND_REPEAT = 1500;
    private static final int SEND_ONCE = 0;
    private static final int RFX_UDP_PORT = 65442;

    private static final Logger logger = LoggerFactory.getLogger(RFXClient.class);

    private InetAddress host;
    private int packetId = 200; // TODO, initialise to random number
    private final ScheduledExecutorService continuationService = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> continuationHandler;
    private int startPacketId;
    private final String lockName;

    public RFXClient(String lockName, String hostName) {
        this(lockName, hostName, new InetAddressResolver());
    }

    public RFXClient(String lockName, String hostName, InetAddressResolver addressResolver) {
        this.lockName = lockName;
        try {
            host = addressResolver.getByName(hostName);
        } catch (UnknownHostException e) {
            throw new ProntoException(String.format("Could not find Pronto Extender %s", hostName), e);
        }
    }

    public void sendSerial(byte[] payload, int serialPort) {
        Serial serial = new Serial();
        serial.setPacketId(1);
        serial.setPayload(payload);
        serial.setLength(payload.length + 16);
        serial.setPort(serialPort);
        serial.setRate(BaudRate.RATE_19200);
        serial.setStopBits(StopBits.ONE);
        serial.setParity(Parity.NONE);
        serial.setDataBits(DataBits.EIGHT);

        sendAndWaitForReply(serial);
    }

    public void sendOnce(ProntoECF payload, int irPort) {
        logger.debug("Sending one-off code");

        lock();

        start(payload, irPort, SEND_ONCE);

        unlock();
    }

    public void startSending(ProntoECF payload, int irPort) {
        logger.debug("Sending ECF payload "
                + BaseEncoding.base16().withSeparator(" ", 4).encode(new ECFParser().serialise(payload)));

        lock();

        startPacketId = packetId;
        start(payload, irPort, SEND_REPEAT);

        final Runnable sendContinueMessage = new Runnable() {
            @Override
            public void run() {
                Continue continueMessage = new Continue();
                continueMessage.setTimeout(SEND_REPEAT);
                continueMessage.setResumeId(startPacketId);

                sendAndWaitForReply(continueMessage);
            }
        };

        continuationHandler = continuationService.scheduleAtFixedRate(sendContinueMessage, 1, 1, TimeUnit.SECONDS);
    }

    public void stopSending() {
        continuationService.shutdown();
        try {
            Thread.sleep(1000); // TODO, shouldn't be waiting before sending stop message

            Stop stopMessage = new Stop();
            stopMessage.setStartId(startPacketId);

            sendAndWaitForReply(stopMessage);

            Thread.sleep(1000);

            unlock();
        } catch (InterruptedException e) {
            throw new ProntoException("Could not send stop message to extender", e);
        }
    }

    protected void start(ProntoECF payload, int irPort, int timeout) {
        Start startMessage = new Start();
        startMessage.setPort(irPort);
        startMessage.setPayload(payload);
        startMessage.setLength(payload.getNumberOfBytes() + 16);
        startMessage.setTimeout(timeout);

        logger.debug("Start sending payload: "
                + BaseEncoding.base16().withSeparator(" ", 4).encode(new ECFParser().serialise(payload)));

        sendAndWaitForReply(startMessage);
    }

    protected void lock() {
        Lock lockMessage = new Lock();
        lockMessage.setTimeout(LOCK_PERIOD_MILLIS);
        lockMessage.setOwner(lockName);
        lockMessage.setLength(13);

        sendAndWaitForReply(lockMessage);
    }

    protected void unlock() {
        Unlock unlockMessage = new Unlock();
        unlockMessage.setOwner(lockName);
        unlockMessage.setLength(9);

        sendAndWaitForReply(unlockMessage);
    }

    protected void sendAndWaitForReply(Message message) {
        DatagramSocket socket;
        try {
            socket = datagramSocket();

            message.setPacketId(packetId);

            byte[] binaryData = message.serialise();

            logger.debug("Sending message " + BaseEncoding.base16().withSeparator(" ", 4).encode(binaryData));

            DatagramPacket dp = new DatagramPacket(binaryData, binaryData.length, host, RFX_UDP_PORT);
            socket.send(dp);

            waitForReply(socket, packetId++);

            socket.close();
        } catch (IOException e) {
            throw new ProntoException("Could not send message to extender", e);
        }
    }

    protected void waitForReply(DatagramSocket sock, int packetId) {
        try {
            long start = System.currentTimeMillis();
            while (System.currentTimeMillis() - start < RESPONSE_TIMEOUT) {
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

                if (idMatch && response.getType() == RESPONSE_TYPE_ACK) {
                    return;
                }
            }
        } catch (IOException e) {
            throw new ProntoException("Failed to receive reply from extender", e);
        }

        throw new ProntoException("Did not get response from extender after waiting for %s milliseconds");
    }

    protected DatagramSocket datagramSocket() throws SocketException {
        DatagramSocket sock = new DatagramSocket();
        sock.setSoTimeout(1000);
        return sock;
    }

}
