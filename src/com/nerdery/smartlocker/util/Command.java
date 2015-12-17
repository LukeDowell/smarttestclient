package com.nerdery.smartlocker.util;

public enum Command {

    /** Asks for the status of every door */
    ALL_DOOR_STATUS(0x01),

    /** Asks for the status of a single door, with a given column and box name */
    SINGLE_DOOR_STATUS(0x02),

    /** Asks for the status of items in every box */
    ALL_ITEM_STATUS(0x03),

    /** Sent from the server when a given column number is incorrect or nonexistant */
    WRONG_COLUMN_NUMBER(0xFE),

    /** Sent from the server when the command's checksum fails */
    CHECKSUM_FAILED(0x81),

    /** A CRC32 checksum will always be 8 bytes long */
    CHECKSUM_LENGTH(8);

    private int flag;

    Command(int flag) {
        this.flag = flag;
    }

    public int getFlag() {
        return flag;
    }
}