package com.nerdery.smartlocker.util;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

/**
 * Configuration/Utility class
 * Created by ldowell on 12/17/15.
 */
public class Network {

    public static final String SERVER_HOST = "127.0.0.1";
    public static final int SERVER_PORT = 8008;

    /**
     * Packages a command into a byte array
     * @param command
     *      The int value of a command
     * @param args
     *      Optional.
     * @return
     *      A byte array containing all information provided and a checksum
     */
    public static byte[] packageCommand(Command command, int... args) {
        int totalLength = 2 + args.length; //command + arg.length + checksum

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bos.write(totalLength);
        bos.write(command.getFlag());
        for(int arg : args) {
            bos.write(arg);
        }
        // Get the checksum of data entered so far
        byte[] commandBytes = bos.toByteArray();
        byte checksum = getXorChecksum(commandBytes);

        // Then write it
        bos.write(checksum);

        // Spit out the whole thing
        return bos.toByteArray();
    }

    /**
     *
     * Returns the Xor checksum of a given byte array
     * @param data
     *      The byte array to check
     * @return
     *      A byte value representing a checksum
     */
    public static byte getXorChecksum(byte[] data) {
        byte[] bytes = Arrays.copyOf(data, data.length);

        for(int i = 0 ; i < bytes.length; i++) {
            bytes[0] ^= bytes[i];
        }

        return bytes[0];
    }

    private Network() {}
}
