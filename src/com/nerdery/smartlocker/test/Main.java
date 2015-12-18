package com.nerdery.smartlocker.test;


import com.nerdery.smartlocker.util.Command;
import com.nerdery.smartlocker.util.Network;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) throws IOException {

        TestClient client = new TestClient();
        client.addObserver((o, arg) -> {
            ByteBuffer buffer = (ByteBuffer) arg;
            byte[] message = buffer.array();
            System.out.println(new String(message));
        });

        client.broadcast(Network.packageCommand(Command.BROADCAST));
    }
}
