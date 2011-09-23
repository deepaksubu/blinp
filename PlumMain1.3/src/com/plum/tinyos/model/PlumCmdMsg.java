package com.plum.tinyos.model;
/**
 * This class is automatically generated by mig. DO NOT EDIT THIS FILE.
 * This class implements a Java interface to the 'PlumCmdMsg'
 * message type.
 */

public class PlumCmdMsg extends net.tinyos.message.Message {

    /** The default size of this message type in bytes. */
    public static final int DEFAULT_MESSAGE_SIZE = 16;

    /** The Active Message type associated with this message. */
    public static final int AM_TYPE = 50;

    /** Create a new PlumCmdMsg of size 16. */
    public PlumCmdMsg() {
        super(DEFAULT_MESSAGE_SIZE);
        amTypeSet(AM_TYPE);
    }

    /** Create a new PlumCmdMsg of the given data_length. */
    public PlumCmdMsg(int data_length) {
        super(data_length);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new PlumCmdMsg with the given data_length
     * and base offset.
     */
    public PlumCmdMsg(int data_length, int base_offset) {
        super(data_length, base_offset);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new PlumCmdMsg using the given byte array
     * as backing store.
     */
    public PlumCmdMsg(byte[] data) {
        super(data);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new PlumCmdMsg using the given byte array
     * as backing store, with the given base offset.
     */
    public PlumCmdMsg(byte[] data, int base_offset) {
        super(data, base_offset);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new PlumCmdMsg using the given byte array
     * as backing store, with the given base offset and data length.
     */
    public PlumCmdMsg(byte[] data, int base_offset, int data_length) {
        super(data, base_offset, data_length);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new PlumCmdMsg embedded in the given message
     * at the given base offset.
     */
    public PlumCmdMsg(net.tinyos.message.Message msg, int base_offset) {
        super(msg, base_offset, DEFAULT_MESSAGE_SIZE);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new PlumCmdMsg embedded in the given message
     * at the given base offset and length.
     */
    public PlumCmdMsg(net.tinyos.message.Message msg, int base_offset, int data_length) {
        super(msg, base_offset, data_length);
        amTypeSet(AM_TYPE);
    }

    /**
    /* Return a String representation of this message. Includes the
     * message type name and the non-indexed field values.
     */
    public String toString() {
      String s = "Message <PlumCmdMsg> \n";
      try {
        s += "  [cmdID=0x"+Long.toHexString(get_cmdID())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [addr=0x"+Long.toHexString(get_addr())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [sampleRate=0x"+Long.toHexString(get_sampleRate())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [statusRate=0x"+Long.toHexString(get_statusRate())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [blockStart=0x"+Long.toHexString(get_blockStart())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [blockEnd=0x"+Long.toHexString(get_blockEnd())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [unixTime=0x"+Long.toHexString(get_unixTime())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      return s;
    }

    // Message-type-specific access methods appear below.

    /////////////////////////////////////////////////////////
    // Accessor methods for field: cmdID
    //   Field type: int, unsigned
    //   Offset (bits): 0
    //   Size (bits): 16
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'cmdID' is signed (false).
     */
    public static boolean isSigned_cmdID() {
        return false;
    }

    /**
     * Return whether the field 'cmdID' is an array (false).
     */
    public static boolean isArray_cmdID() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'cmdID'
     */
    public static int offset_cmdID() {
        return (0 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'cmdID'
     */
    public static int offsetBits_cmdID() {
        return 0;
    }

    /**
     * Return the value (as a int) of the field 'cmdID'
     */
    public int get_cmdID() {
        return (int)getUIntBEElement(offsetBits_cmdID(), 16);
    }

    /**
     * Set the value of the field 'cmdID'
     */
    public void set_cmdID(int value) {
        setUIntBEElement(offsetBits_cmdID(), 16, value);
    }

    /**
     * Return the size, in bytes, of the field 'cmdID'
     */
    public static int size_cmdID() {
        return (16 / 8);
    }

    /**
     * Return the size, in bits, of the field 'cmdID'
     */
    public static int sizeBits_cmdID() {
        return 16;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: addr
    //   Field type: int, unsigned
    //   Offset (bits): 16
    //   Size (bits): 16
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'addr' is signed (false).
     */
    public static boolean isSigned_addr() {
        return false;
    }

    /**
     * Return whether the field 'addr' is an array (false).
     */
    public static boolean isArray_addr() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'addr'
     */
    public static int offset_addr() {
        return (16 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'addr'
     */
    public static int offsetBits_addr() {
        return 16;
    }

    /**
     * Return the value (as a int) of the field 'addr'
     */
    public int get_addr() {
        return (int)getUIntBEElement(offsetBits_addr(), 16);
    }

    /**
     * Set the value of the field 'addr'
     */
    public void set_addr(int value) {
        setUIntBEElement(offsetBits_addr(), 16, value);
    }

    /**
     * Return the size, in bytes, of the field 'addr'
     */
    public static int size_addr() {
        return (16 / 8);
    }

    /**
     * Return the size, in bits, of the field 'addr'
     */
    public static int sizeBits_addr() {
        return 16;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: sampleRate
    //   Field type: int, unsigned
    //   Offset (bits): 32
    //   Size (bits): 16
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'sampleRate' is signed (false).
     */
    public static boolean isSigned_sampleRate() {
        return false;
    }

    /**
     * Return whether the field 'sampleRate' is an array (false).
     */
    public static boolean isArray_sampleRate() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'sampleRate'
     */
    public static int offset_sampleRate() {
        return (32 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'sampleRate'
     */
    public static int offsetBits_sampleRate() {
        return 32;
    }

    /**
     * Return the value (as a int) of the field 'sampleRate'
     */
    public int get_sampleRate() {
        return (int)getUIntBEElement(offsetBits_sampleRate(), 16);
    }

    /**
     * Set the value of the field 'sampleRate'
     */
    public void set_sampleRate(int value) {
        setUIntBEElement(offsetBits_sampleRate(), 16, value);
    }

    /**
     * Return the size, in bytes, of the field 'sampleRate'
     */
    public static int size_sampleRate() {
        return (16 / 8);
    }

    /**
     * Return the size, in bits, of the field 'sampleRate'
     */
    public static int sizeBits_sampleRate() {
        return 16;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: statusRate
    //   Field type: int, unsigned
    //   Offset (bits): 48
    //   Size (bits): 16
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'statusRate' is signed (false).
     */
    public static boolean isSigned_statusRate() {
        return false;
    }

    /**
     * Return whether the field 'statusRate' is an array (false).
     */
    public static boolean isArray_statusRate() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'statusRate'
     */
    public static int offset_statusRate() {
        return (48 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'statusRate'
     */
    public static int offsetBits_statusRate() {
        return 48;
    }

    /**
     * Return the value (as a int) of the field 'statusRate'
     */
    public int get_statusRate() {
        return (int)getUIntBEElement(offsetBits_statusRate(), 16);
    }

    /**
     * Set the value of the field 'statusRate'
     */
    public void set_statusRate(int value) {
        setUIntBEElement(offsetBits_statusRate(), 16, value);
    }

    /**
     * Return the size, in bytes, of the field 'statusRate'
     */
    public static int size_statusRate() {
        return (16 / 8);
    }

    /**
     * Return the size, in bits, of the field 'statusRate'
     */
    public static int sizeBits_statusRate() {
        return 16;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: blockStart
    //   Field type: int, unsigned
    //   Offset (bits): 64
    //   Size (bits): 16
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'blockStart' is signed (false).
     */
    public static boolean isSigned_blockStart() {
        return false;
    }

    /**
     * Return whether the field 'blockStart' is an array (false).
     */
    public static boolean isArray_blockStart() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'blockStart'
     */
    public static int offset_blockStart() {
        return (64 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'blockStart'
     */
    public static int offsetBits_blockStart() {
        return 64;
    }

    /**
     * Return the value (as a int) of the field 'blockStart'
     */
    public int get_blockStart() {
        return (int)getUIntBEElement(offsetBits_blockStart(), 16);
    }

    /**
     * Set the value of the field 'blockStart'
     */
    public void set_blockStart(int value) {
        setUIntBEElement(offsetBits_blockStart(), 16, value);
    }

    /**
     * Return the size, in bytes, of the field 'blockStart'
     */
    public static int size_blockStart() {
        return (16 / 8);
    }

    /**
     * Return the size, in bits, of the field 'blockStart'
     */
    public static int sizeBits_blockStart() {
        return 16;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: blockEnd
    //   Field type: int, unsigned
    //   Offset (bits): 80
    //   Size (bits): 16
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'blockEnd' is signed (false).
     */
    public static boolean isSigned_blockEnd() {
        return false;
    }

    /**
     * Return whether the field 'blockEnd' is an array (false).
     */
    public static boolean isArray_blockEnd() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'blockEnd'
     */
    public static int offset_blockEnd() {
        return (80 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'blockEnd'
     */
    public static int offsetBits_blockEnd() {
        return 80;
    }

    /**
     * Return the value (as a int) of the field 'blockEnd'
     */
    public int get_blockEnd() {
        return (int)getUIntBEElement(offsetBits_blockEnd(), 16);
    }

    /**
     * Set the value of the field 'blockEnd'
     */
    public void set_blockEnd(int value) {
        setUIntBEElement(offsetBits_blockEnd(), 16, value);
    }

    /**
     * Return the size, in bytes, of the field 'blockEnd'
     */
    public static int size_blockEnd() {
        return (16 / 8);
    }

    /**
     * Return the size, in bits, of the field 'blockEnd'
     */
    public static int sizeBits_blockEnd() {
        return 16;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: unixTime
    //   Field type: long, unsigned
    //   Offset (bits): 96
    //   Size (bits): 32
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'unixTime' is signed (false).
     */
    public static boolean isSigned_unixTime() {
        return false;
    }

    /**
     * Return whether the field 'unixTime' is an array (false).
     */
    public static boolean isArray_unixTime() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'unixTime'
     */
    public static int offset_unixTime() {
        return (96 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'unixTime'
     */
    public static int offsetBits_unixTime() {
        return 96;
    }

    /**
     * Return the value (as a long) of the field 'unixTime'
     */
    public long get_unixTime() {
        return (long)getUIntBEElement(offsetBits_unixTime(), 32);
    }

    /**
     * Set the value of the field 'unixTime'
     */
    public void set_unixTime(long value) {
        setUIntBEElement(offsetBits_unixTime(), 32, value);
    }

    /**
     * Return the size, in bytes, of the field 'unixTime'
     */
    public static int size_unixTime() {
        return (32 / 8);
    }

    /**
     * Return the size, in bits, of the field 'unixTime'
     */
    public static int sizeBits_unixTime() {
        return 32;
    }

}
