package org.github.prontolib.rfx.discovery;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import org.github.prontolib.rfx.RFXConfiguration;
import org.github.prontolib.rfx.RFXType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Not using jupnp or cling as RFX seems to be not quite on spec
public class RFXSearcher {

    private static final Logger logger = LoggerFactory.getLogger(RFXSearcher.class);

    private static final int TIMEOUT_MILLIS = 1000;

    public RFXConfiguration findExtenderWithId(int searchId) throws IOException {
        byte[] sendData = new byte[1024];
        byte[] receiveData = new byte[1024];

        String MSEARCH = "M-SEARCH * HTTP/1.1\nHost: 239.255.255.250:1900\nMan: \"ssdp:discover\"\nST: philips:maestro-extender\n";
        sendData = MSEARCH.getBytes();

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
                InetAddress.getByName("239.255.255.250"), 1900);

        DatagramSocket clientSocket = openSocket();
        clientSocket.setSoTimeout(TIMEOUT_MILLIS);
        clientSocket.send(sendPacket);

        RFXConfiguration foundConfig = null;

        long startTime = System.currentTimeMillis();
        try {
            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                clientSocket.receive(receivePacket);

                String response = new String(receivePacket.getData());

                logger.debug("Got SSDP response:\n" + response);

                String[] lines = response.split("\r\n");

                RFXConfiguration config = new RFXConfiguration();
                for (String line : lines) {
                    if (line.contains(": ")) {
                        String[] parts = line.split(": ");
                        String header = parts[0];
                        String value = parts[1].trim();
                        switch (header) {
                            case RFXConfiguration.HEADER_ID:
                                config.setId(Integer.parseInt(value));
                                break;
                            case RFXConfiguration.HEADER_FIRMWARE_VERSION:
                                config.setFirmwareVersion(value);
                                break;
                            case RFXConfiguration.HEADER_MAC_ADDRESS:
                                config.setMacAddress(value);
                                break;
                            case RFXConfiguration.HEADER_RFX_TYPE:
                                config.setType(RFXType.valueOf(value));
                                break;
                            case RFXConfiguration.HEADER_MODE:
                                config.setUseMode(RFXConfiguration.MODE_USE.equals(value));
                                break;
                            default:
                                break;
                        }
                    }
                }
                config.setIpAddress(receivePacket.getAddress().getHostAddress());
                if (config.getId() == searchId) {
                    foundConfig = config;
                    break;
                }
                if ((System.currentTimeMillis() - startTime) > TIMEOUT_MILLIS) {
                    logger.warn("Timed out waiting for extender with id " + searchId);
                    break;
                }
            }
        } catch (SocketTimeoutException ste) {
            logger.warn("Timed out waiting for SSDP response");
        }
        clientSocket.close();

        return foundConfig;
    }

    protected DatagramSocket openSocket() throws SocketException {
        return new DatagramSocket();
    }

}
