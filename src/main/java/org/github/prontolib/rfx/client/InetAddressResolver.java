package org.github.prontolib.rfx.client;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class InetAddressResolver {

    public InetAddress getByName(String hostName) throws UnknownHostException {
        return InetAddress.getByName(hostName);
    }

}
