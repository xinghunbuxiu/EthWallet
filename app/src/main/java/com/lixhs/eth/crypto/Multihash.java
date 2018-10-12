package com.lixhs.eth.crypto;

import org.bitcoinj.core.Base58;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

public class Multihash {
    private static final char[] hexArray = "0123456789ABCDEF".toCharArray();
    private final byte[] hash;
    public final Type type;

    public enum Type {
        md5(213, 16),
        sha1(17, 20),
        sha2_256(18, 32),
        sha2_512(19, 64),
        sha3_512(20, 64),
        blake2b(64, 64),
        blake2s(65, 32);

        private static Map<Integer, Type> lookup;
        public int index;
        public int length;

        static {
            lookup = new TreeMap();
            Type[] values = values();
            int length = values.length;
            int i = 0;
            while (i < length) {
                Type type = values[i];
                lookup.put(Integer.valueOf(type.index), type);
                i++;
            }
        }

        private Type(int index, int length) {
            this.index = index;
            this.length = length;
        }

        public static Type lookup(int i) {
            if (lookup.containsKey(Integer.valueOf(i))) {
                return (Type) lookup.get(Integer.valueOf(i));
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unknown Multihash type: ");
            stringBuilder.append(i);
            throw new IllegalStateException(stringBuilder.toString());
        }
    }

    public Multihash(Type type, byte[] bArr) {
        if (bArr.length > 127) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unsupported hash size: ");
            stringBuilder.append(bArr.length);
            throw new IllegalStateException(stringBuilder.toString());
        } else if (bArr.length != type.length) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Incorrect hash length: ");
            stringBuilder2.append(bArr.length);
            stringBuilder2.append(" != ");
            stringBuilder2.append(type.length);
            throw new IllegalStateException(stringBuilder2.toString());
        } else {
            this.type = type;
            this.hash = bArr;
        }
    }

    public Multihash(Multihash multihash) {
        this(multihash.type, multihash.hash);
    }

    private Multihash(byte[] bArr) {
        this(Type.lookup(bArr[0] & 255), Arrays.copyOfRange(bArr, 2, bArr.length));
    }

    private byte[] toBytes() {
        byte obj[] = new byte[(this.hash.length + 2)];
        obj[0] = (byte) this.type.index;
        obj[1] = (byte) this.hash.length;
        System.arraycopy(this.hash, 0, obj, 2, this.hash.length);
        return obj;
    }

    public void serialize(DataOutput dataOutput) throws IOException {
        dataOutput.write(toBytes());
    }

    public static Multihash deserialize(DataInput dataInput) throws IOException {
        int readUnsignedByte = dataInput.readUnsignedByte();
        int readUnsignedByte2 = dataInput.readUnsignedByte();
        Type lookup = Type.lookup(readUnsignedByte);
        byte[] bArr = new byte[readUnsignedByte2];
        dataInput.readFully(bArr);
        return new Multihash(lookup, bArr);
    }

    public String toString() {
        return toBase58();
    }

    public boolean equals(Object obj) {
        boolean z = false;
        if (!(obj instanceof Multihash)) {
            return false;
        }
        Multihash multihash = (Multihash) obj;
        if (this.type == multihash.type && Arrays.equals(this.hash, multihash.hash)) {
            z = true;
        }
        return z;
    }

    public int hashCode() {
        return Arrays.hashCode(this.hash) ^ this.type.hashCode();
    }

    public String toHex() {
        byte[] toBytes = toBytes();
        char[] cArr = new char[(toBytes.length * 2)];
        for (int i = 0; i < toBytes.length; i++) {
            int i2 = toBytes[i] & 255;
            int i3 = i * 2;
            cArr[i3] = hexArray[i2 >>> 4];
            cArr[i3 + 1] = hexArray[i2 & 15];
        }
        return new String(cArr);
    }

    public String toBase58() {
        return Base58.encode(toBytes());
    }

    public static Multihash fromHex(String str) {
        if (str.length() % 2 != 0) {
            throw new IllegalStateException("Uneven number of hex digits!");
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int i = 0;
        while (i < str.length() - 1) {
            int i2 = i + 2;
            byteArrayOutputStream.write(Integer.valueOf(str.substring(i, i2), 16).intValue());
            i = i2;
        }
        return new Multihash(byteArrayOutputStream.toByteArray());
    }

    public static Multihash fromBase58(String str) {
        return new Multihash(Base58.decode(str));
    }
}