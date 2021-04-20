import java.io.*;
import java.util.Base64;

public class Serializer
{
    public static byte[] serializeObject(Object object) throws IOException
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(bos);
        out.writeObject(object);
        byte bytes[] = bos.toByteArray();
        out.close();
        bos.close();
        return bytes;
    }

    public static <T> T deserializeObject(byte[] bytes) throws IOException, ClassNotFoundException
    {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInput in = new ObjectInputStream(bis);
        return (T) in.readObject();
    }

    public static String getStringFromBytes(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static byte[] getBytesFromString(String string) {
        return Base64.getDecoder().decode(string);
    }

    public static int byteArrayToInt(byte[] encodedValue) {
        int value = (encodedValue[3] << (Byte.SIZE * 3));
        value |= (encodedValue[2] & 0xFF) << (Byte.SIZE * 2);
        value |= (encodedValue[1] & 0xFF) << (Byte.SIZE * 1);
        value |= (encodedValue[0] & 0xFF);
        return value;
    }

    public static byte[] intToByteArray(int value) {
        byte[] encodedValue = new byte[Integer.SIZE / Byte.SIZE];
        encodedValue[3] = (byte) (value >> Byte.SIZE * 3);
        encodedValue[2] = (byte) (value >> Byte.SIZE * 2);
        encodedValue[1] = (byte) (value >> Byte.SIZE);
        encodedValue[0] = (byte) value;
        return encodedValue;
    }
}
