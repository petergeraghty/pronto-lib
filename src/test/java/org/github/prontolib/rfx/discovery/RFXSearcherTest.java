package org.github.prontolib.rfx.discovery;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

import org.github.prontolib.rfx.RFXConfiguration;
import org.github.prontolib.rfx.RFXType;
import org.github.prontolib.rfx.test.FakeDatagramSocket;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

@RunWith(MockitoJUnitRunner.class)
public class RFXSearcherTest {

    @Spy
    private RFXSearcher testObj;

    @Mock
    private InetAddress addressMock;
    @Spy
    FakeDatagramSocket datagramSocketMock;

    @Before
    public void setUp() throws Exception {
        doReturn(datagramSocketMock).when(testObj).openSocket();
        when(addressMock.getHostAddress()).thenReturn("192.168.0.28");

        doAnswer(new Answer<DatagramPacket>() {
            @Override
            public DatagramPacket answer(InvocationOnMock invocation) throws Throwable {
                DatagramPacket datagramPacket = invocation.getArgumentAt(0, DatagramPacket.class);
                datagramPacket.setData(("HTTP/1.1 200 OK\r\n" + "ST: philips:maestro-extender\r\n"
                        + "S: uuid:c7020000-9b07-0000-3305-0000a3020000\r\n" + "Cache-Control: max-age=1800\r\n"
                        + "USN: uuid:86c8f273-f803-4d01-9886-bdfad9a3dc31\r\n" + "X-Maestro-Extender-ID: 0\r\n"
                        + "X-Maestro-Extender-Firmware: 1.1.36\r\n" + "X-Maestro-Extender-MAC: 00:1A:6B:95:56:88\r\n"
                        + "X-Maestro-Extender-Type: RFX9400\r\n" + "X-Maestro-Extender-Mode: usemode").getBytes());
                datagramPacket.setAddress(addressMock);
                return datagramPacket;
            }
        }).when(datagramSocketMock).populatePacket(any(DatagramPacket.class));

    }

    @Test
    public void shouldSucceed() throws Throwable {
        RFXConfiguration rfxConfiguration = testObj.findExtenderWithId(0);

        assertNotNull(rfxConfiguration);
        assertEquals(0, rfxConfiguration.getId());
        assertEquals("192.168.0.28", rfxConfiguration.getIpAddress());
        assertEquals("1.1.36", rfxConfiguration.getFirmwareVersion());
        assertEquals("00:1A:6B:95:56:88", rfxConfiguration.getMacAddress());
        assertEquals(RFXType.RFX9400, rfxConfiguration.getType());
        Assert.assertTrue(rfxConfiguration.isUseMode());
    }

    @Test
    public void shouldReturnNullOnSocketTimeout() throws Exception {
        doThrow(new SocketTimeoutException()).when(datagramSocketMock).receive(any(DatagramPacket.class));

        RFXConfiguration rfxConfiguration = testObj.findExtenderWithId(99);

        assertNull(rfxConfiguration);
    }

    @Test
    public void shouldReturnNullWhenIdDoesNotMatch() throws Exception {
        RFXConfiguration rfxConfiguration = testObj.findExtenderWithId(99);

        assertNull(rfxConfiguration);
    }

}
