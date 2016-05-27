package org.github.prontolib.rfx.client;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import org.github.prontolib.exception.ProntoException;
import org.github.prontolib.rfx.message.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

@RunWith(MockitoJUnitRunner.class)
public class RFXClientTest {

    private RFXClient testObj;

    @Mock
    private DatagramSocket socketMock;

    @Mock
    private InetAddressResolver addressResolverMock;

    @Before
    public void setUp() throws Exception {
        testObj = spy(new RFXClient("lockName", "extenderAddress", addressResolverMock));

        // Mockito.doReturn(socketMock)
    }

    @Test(expected = ProntoException.class)
    public void shouldTimeoutWaitingForValidReplyWhenPacketIdsDoNotMatch() throws Exception {
        mockReply(RFXClient.RESPONSE_TYPE_ACK, 1234);

        testObj.waitForReply(socketMock, 1);
    }

    @Test(expected = ProntoException.class)
    public void shouldTimeoutWaitingForValidReplyWhenIncorrectType() throws Exception {
        mockReply(RFXClient.RESPONSE_TYPE_ACK + 1, 1234);

        testObj.waitForReply(socketMock, 1234);
    }

    @Test
    public void shouldWaitForReply() throws Exception {
        mockReply(RFXClient.RESPONSE_TYPE_ACK, 1234);

        testObj.waitForReply(socketMock, 1234);
    }

    @Test(expected = ProntoException.class)
    public void shouldThrowProntoExceptionOnIOException() throws Exception {
        doThrow(new IOException()).when(socketMock).receive(Matchers.any(DatagramPacket.class));

        testObj.waitForReply(socketMock, 1234);
    }

    private void mockReply(final int type, final int packetId) throws IOException {
        doAnswer(new Answer<Void>() {

            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                Response response = new Response();
                response.setPacketId(packetId);
                response.setType(type);
                DatagramPacket reply = invocation.getArgumentAt(0, DatagramPacket.class);
                reply.setData(response.serialise());
                return null;
            }
        }).when(socketMock).receive(any(DatagramPacket.class));
    }

}