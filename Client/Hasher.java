import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hasher
{
    public static byte[] getMessageHash(Object object) throws IOException, NoSuchAlgorithmException
    {
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        return md.digest(Serializer.serializeObject(object));
    }
}