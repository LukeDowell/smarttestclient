package com.nerdery.smartlocker.test;

import com.nerdery.smartlocker.util.Command;
import com.nerdery.smartlocker.util.Network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Created by ldowell on 12/17/15.
 */
public class TestClient extends Observable {

    private DatagramChannel channel;
    private Thread listenerThread;
    private InetSocketAddress broadcastAddress = new InetSocketAddress("255.255.255.255", Network.SERVER_PORT);
    private List<SocketAddress> boardAddresses;

    public TestClient() throws IOException {
        boardAddresses = new ArrayList<>();
        channel = DatagramChannel.open();
        channel.socket().setBroadcast(true);

        listenerThread = createListenerThead();
        listenerThread.start();
    }

    /**
     * Sends out the given byte array to whatever destination is configured
     * in the Network class
     * @param messageArray
     *      The byte array to send
     * @return
     *      The number of bytes sent
     * @throws IOException
     *      Something fucked up
     */
    public int send(byte[] messageArray) throws IOException {
        ByteBuffer messageBuffer = ByteBuffer.wrap(messageArray);
//        return channel.send(messageBuffer, serverAddress);
        return 1;
    }


    public int broadcast(byte[] messageArray) throws IOException {
        ByteBuffer messageBuffer = ByteBuffer.wrap(messageArray);
        return channel.send(messageBuffer, broadcastAddress);
    }

    private Thread createListenerThead() {

        return new Thread(() -> {
            while(!Thread.currentThread().isInterrupted()) {
                try {
                    ByteBuffer buffer = ByteBuffer.allocate(64);
                    buffer.clear();

                    channel.receive(buffer);
                    setChanged();
                    notifyObservers(buffer);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
