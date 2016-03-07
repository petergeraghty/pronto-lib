package org.github.prontolib.rfx.test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

// Can't use Mockito.doAnswer on receive method as it is void, but want to be able to manipulate
// the DatagramPacket passed in, so created this with a method I can use
public class FakeDatagramSocket extends DatagramSocket {

    public FakeDatagramSocket() throws SocketException {
        super();
    }

    @Override
    public synchronized void receive(DatagramPacket datagramPacket) throws IOException {
        populatePacket(datagramPacket);
    }

    public DatagramPacket populatePacket(DatagramPacket datagramPacket) {
        return datagramPacket;
    }

}
