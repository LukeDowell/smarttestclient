package com.nerdery.smartlocker.test;

import com.nerdery.smartlocker.util.Command;
import com.nerdery.smartlocker.util.Network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Observable;

/**
 * Created by ldowell on 12/17/15.
 */
public class TestClient extends Observable {

    private DatagramChannel channel;

    private InetSocketAddress serverAddress;

    private Thread listenerThread;

    public TestClient() throws IOException {
        channel = DatagramChannel.open();
        serverAddress = new InetSocketAddress(Network.SERVER_HOST, Network.SERVER_PORT);

        initializeListener();
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
        return channel.send(messageBuffer, serverAddress);
    }

    private void initializeListener() {

        if(listenerThread != null) {
            listenerThread.interrupt();
        }

        listenerThread = new Thread(() -> {
            while(!Thread.currentThread().isInterrupted()) {
                try {
                    ByteBuffer buffer = ByteBuffer.allocate(12);
                    buffer.clear();

                    channel.receive(buffer);
                    System.out.println("ASDFASDF");
                    setChanged();
                    notifyObservers(buffer);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
