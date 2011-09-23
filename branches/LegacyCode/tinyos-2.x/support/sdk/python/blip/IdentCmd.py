#
# This class is automatically generated by mig. DO NOT EDIT THIS FILE.
# This class implements a Python interface to the 'IdentCmd'
# message type.
#

import tinyos.message.Message

# The default size of this message type in bytes.
DEFAULT_MESSAGE_SIZE = 52

# The Active Message type associated with this message.
AM_TYPE = -1

class IdentCmd(tinyos.message.Message.Message):
    # Create a new IdentCmd of size 52.
    def __init__(self, data="", addr=None, gid=None, base_offset=0, data_length=52):
        tinyos.message.Message.Message.__init__(self, data, addr, gid, base_offset, data_length)
        self.amTypeSet(AM_TYPE)
    
    # Get AM_TYPE
    def get_amType(cls):
        return AM_TYPE
    
    get_amType = classmethod(get_amType)
    
    #
    # Return a String representation of this message. Includes the
    # message type name and the non-indexed field values.
    #
    def __str__(self):
        s = "Message <IdentCmd> \n"
        try:
            s += "  [appname=";
            for i in range(0, 16):
                s += "0x%x " % (self.getElement_appname(i) & 0xff)
            s += "]\n";
        except:
            pass
        try:
            s += "  [username=";
            for i in range(0, 16):
                s += "0x%x " % (self.getElement_username(i) & 0xff)
            s += "]\n";
        except:
            pass
        try:
            s += "  [hostname=";
            for i in range(0, 16):
                s += "0x%x " % (self.getElement_hostname(i) & 0xff)
            s += "]\n";
        except:
            pass
        try:
            s += "  [timestamp=0x%x]\n" % (self.get_timestamp())
        except:
            pass
        return s

    # Message-type-specific access methods appear below.

    #
    # Accessor methods for field: appname
    #   Field type: short[]
    #   Offset (bits): 0
    #   Size of each element (bits): 8
    #

    #
    # Return whether the field 'appname' is signed (False).
    #
    def isSigned_appname(self):
        return False
    
    #
    # Return whether the field 'appname' is an array (True).
    #
    def isArray_appname(self):
        return True
    
    #
    # Return the offset (in bytes) of the field 'appname'
    #
    def offset_appname(self, index1):
        offset = 0
        if index1 < 0 or index1 >= 16:
            raise IndexError
        offset += 0 + index1 * 8
        return (offset / 8)
    
    #
    # Return the offset (in bits) of the field 'appname'
    #
    def offsetBits_appname(self, index1):
        offset = 0
        if index1 < 0 or index1 >= 16:
            raise IndexError
        offset += 0 + index1 * 8
        return offset
    
    #
    # Return the entire array 'appname' as a short[]
    #
    def get_appname(self):
        tmp = [None]*16
        for index0 in range (0, self.numElements_appname(0)):
                tmp[index0] = self.getElement_appname(index0)
        return tmp
    
    #
    # Set the contents of the array 'appname' from the given short[]
    #
    def set_appname(self, value):
        for index0 in range(0, len(value)):
            self.setElement_appname(index0, value[index0])

    #
    # Return an element (as a short) of the array 'appname'
    #
    def getElement_appname(self, index1):
        return self.getUIntElement(self.offsetBits_appname(index1), 8, 1)
    
    #
    # Set an element of the array 'appname'
    #
    def setElement_appname(self, index1, value):
        self.setUIntElement(self.offsetBits_appname(index1), 8, value, 1)
    
    #
    # Return the total size, in bytes, of the array 'appname'
    #
    def totalSize_appname(self):
        return (128 / 8)
    
    #
    # Return the total size, in bits, of the array 'appname'
    #
    def totalSizeBits_appname(self):
        return 128
    
    #
    # Return the size, in bytes, of each element of the array 'appname'
    #
    def elementSize_appname(self):
        return (8 / 8)
    
    #
    # Return the size, in bits, of each element of the array 'appname'
    #
    def elementSizeBits_appname(self):
        return 8
    
    #
    # Return the number of dimensions in the array 'appname'
    #
    def numDimensions_appname(self):
        return 1
    
    #
    # Return the number of elements in the array 'appname'
    #
    def numElements_appname():
        return 16
    
    #
    # Return the number of elements in the array 'appname'
    # for the given dimension.
    #
    def numElements_appname(self, dimension):
        array_dims = [ 16,  ]
        if dimension < 0 or dimension >= 1:
            raise IndexException
        if array_dims[dimension] == 0:
            raise IndexError
        return array_dims[dimension]
    
    #
    # Fill in the array 'appname' with a String
    #
    def setString_appname(self, s):
         l = len(s)
         for i in range(0, l):
             self.setElement_appname(i, ord(s[i]));
         self.setElement_appname(l, 0) #null terminate
    
    #
    # Read the array 'appname' as a String
    #
    def getString_appname(self):
        carr = "";
        for i in range(0, 4000):
            if self.getElement_appname(i) == chr(0):
                break
            carr += self.getElement_appname(i)
        return carr
    
    #
    # Accessor methods for field: username
    #   Field type: short[]
    #   Offset (bits): 128
    #   Size of each element (bits): 8
    #

    #
    # Return whether the field 'username' is signed (False).
    #
    def isSigned_username(self):
        return False
    
    #
    # Return whether the field 'username' is an array (True).
    #
    def isArray_username(self):
        return True
    
    #
    # Return the offset (in bytes) of the field 'username'
    #
    def offset_username(self, index1):
        offset = 128
        if index1 < 0 or index1 >= 16:
            raise IndexError
        offset += 0 + index1 * 8
        return (offset / 8)
    
    #
    # Return the offset (in bits) of the field 'username'
    #
    def offsetBits_username(self, index1):
        offset = 128
        if index1 < 0 or index1 >= 16:
            raise IndexError
        offset += 0 + index1 * 8
        return offset
    
    #
    # Return the entire array 'username' as a short[]
    #
    def get_username(self):
        tmp = [None]*16
        for index0 in range (0, self.numElements_username(0)):
                tmp[index0] = self.getElement_username(index0)
        return tmp
    
    #
    # Set the contents of the array 'username' from the given short[]
    #
    def set_username(self, value):
        for index0 in range(0, len(value)):
            self.setElement_username(index0, value[index0])

    #
    # Return an element (as a short) of the array 'username'
    #
    def getElement_username(self, index1):
        return self.getUIntElement(self.offsetBits_username(index1), 8, 1)
    
    #
    # Set an element of the array 'username'
    #
    def setElement_username(self, index1, value):
        self.setUIntElement(self.offsetBits_username(index1), 8, value, 1)
    
    #
    # Return the total size, in bytes, of the array 'username'
    #
    def totalSize_username(self):
        return (128 / 8)
    
    #
    # Return the total size, in bits, of the array 'username'
    #
    def totalSizeBits_username(self):
        return 128
    
    #
    # Return the size, in bytes, of each element of the array 'username'
    #
    def elementSize_username(self):
        return (8 / 8)
    
    #
    # Return the size, in bits, of each element of the array 'username'
    #
    def elementSizeBits_username(self):
        return 8
    
    #
    # Return the number of dimensions in the array 'username'
    #
    def numDimensions_username(self):
        return 1
    
    #
    # Return the number of elements in the array 'username'
    #
    def numElements_username():
        return 16
    
    #
    # Return the number of elements in the array 'username'
    # for the given dimension.
    #
    def numElements_username(self, dimension):
        array_dims = [ 16,  ]
        if dimension < 0 or dimension >= 1:
            raise IndexException
        if array_dims[dimension] == 0:
            raise IndexError
        return array_dims[dimension]
    
    #
    # Fill in the array 'username' with a String
    #
    def setString_username(self, s):
         l = len(s)
         for i in range(0, l):
             self.setElement_username(i, ord(s[i]));
         self.setElement_username(l, 0) #null terminate
    
    #
    # Read the array 'username' as a String
    #
    def getString_username(self):
        carr = "";
        for i in range(0, 4000):
            if self.getElement_username(i) == chr(0):
                break
            carr += self.getElement_username(i)
        return carr
    
    #
    # Accessor methods for field: hostname
    #   Field type: short[]
    #   Offset (bits): 256
    #   Size of each element (bits): 8
    #

    #
    # Return whether the field 'hostname' is signed (False).
    #
    def isSigned_hostname(self):
        return False
    
    #
    # Return whether the field 'hostname' is an array (True).
    #
    def isArray_hostname(self):
        return True
    
    #
    # Return the offset (in bytes) of the field 'hostname'
    #
    def offset_hostname(self, index1):
        offset = 256
        if index1 < 0 or index1 >= 16:
            raise IndexError
        offset += 0 + index1 * 8
        return (offset / 8)
    
    #
    # Return the offset (in bits) of the field 'hostname'
    #
    def offsetBits_hostname(self, index1):
        offset = 256
        if index1 < 0 or index1 >= 16:
            raise IndexError
        offset += 0 + index1 * 8
        return offset
    
    #
    # Return the entire array 'hostname' as a short[]
    #
    def get_hostname(self):
        tmp = [None]*16
        for index0 in range (0, self.numElements_hostname(0)):
                tmp[index0] = self.getElement_hostname(index0)
        return tmp
    
    #
    # Set the contents of the array 'hostname' from the given short[]
    #
    def set_hostname(self, value):
        for index0 in range(0, len(value)):
            self.setElement_hostname(index0, value[index0])

    #
    # Return an element (as a short) of the array 'hostname'
    #
    def getElement_hostname(self, index1):
        return self.getUIntElement(self.offsetBits_hostname(index1), 8, 1)
    
    #
    # Set an element of the array 'hostname'
    #
    def setElement_hostname(self, index1, value):
        self.setUIntElement(self.offsetBits_hostname(index1), 8, value, 1)
    
    #
    # Return the total size, in bytes, of the array 'hostname'
    #
    def totalSize_hostname(self):
        return (128 / 8)
    
    #
    # Return the total size, in bits, of the array 'hostname'
    #
    def totalSizeBits_hostname(self):
        return 128
    
    #
    # Return the size, in bytes, of each element of the array 'hostname'
    #
    def elementSize_hostname(self):
        return (8 / 8)
    
    #
    # Return the size, in bits, of each element of the array 'hostname'
    #
    def elementSizeBits_hostname(self):
        return 8
    
    #
    # Return the number of dimensions in the array 'hostname'
    #
    def numDimensions_hostname(self):
        return 1
    
    #
    # Return the number of elements in the array 'hostname'
    #
    def numElements_hostname():
        return 16
    
    #
    # Return the number of elements in the array 'hostname'
    # for the given dimension.
    #
    def numElements_hostname(self, dimension):
        array_dims = [ 16,  ]
        if dimension < 0 or dimension >= 1:
            raise IndexException
        if array_dims[dimension] == 0:
            raise IndexError
        return array_dims[dimension]
    
    #
    # Fill in the array 'hostname' with a String
    #
    def setString_hostname(self, s):
         l = len(s)
         for i in range(0, l):
             self.setElement_hostname(i, ord(s[i]));
         self.setElement_hostname(l, 0) #null terminate
    
    #
    # Read the array 'hostname' as a String
    #
    def getString_hostname(self):
        carr = "";
        for i in range(0, 4000):
            if self.getElement_hostname(i) == chr(0):
                break
            carr += self.getElement_hostname(i)
        return carr
    
    #
    # Accessor methods for field: timestamp
    #   Field type: long
    #   Offset (bits): 384
    #   Size (bits): 32
    #

    #
    # Return whether the field 'timestamp' is signed (False).
    #
    def isSigned_timestamp(self):
        return False
    
    #
    # Return whether the field 'timestamp' is an array (False).
    #
    def isArray_timestamp(self):
        return False
    
    #
    # Return the offset (in bytes) of the field 'timestamp'
    #
    def offset_timestamp(self):
        return (384 / 8)
    
    #
    # Return the offset (in bits) of the field 'timestamp'
    #
    def offsetBits_timestamp(self):
        return 384
    
    #
    # Return the value (as a long) of the field 'timestamp'
    #
    def get_timestamp(self):
        return self.getUIntElement(self.offsetBits_timestamp(), 32, 1)
    
    #
    # Set the value of the field 'timestamp'
    #
    def set_timestamp(self, value):
        self.setUIntElement(self.offsetBits_timestamp(), 32, value, 1)
    
    #
    # Return the size, in bytes, of the field 'timestamp'
    #
    def size_timestamp(self):
        return (32 / 8)
    
    #
    # Return the size, in bits, of the field 'timestamp'
    #
    def sizeBits_timestamp(self):
        return 32
    
