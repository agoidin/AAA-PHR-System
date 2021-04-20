import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.io.IOException;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

public class EncryptedMessage implements Serializable
{
    public byte[] bytes;
    public byte[] digest;
    public byte[] iv;

    public boolean isItegrityValid() throws IOException, NoSuchAlgorithmException
    {
        if (Arrays.equals(Hasher.getMessageHash(bytes), digest)) {
            return true;
        }
        return false;
    }

    public static EncryptedMessage encryptWithAES(SecretKey secretKey, byte[] bytes) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");

        byte[] iv = createInitializationVector();
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        cipher.init(Cipher.ENCRYPT_MODE,
                secretKey,
                ivParameterSpec);

        EncryptedMessage encryptedMessage = new EncryptedMessage();
        encryptedMessage.bytes = cipher.doFinal(bytes);
        encryptedMessage.digest = Hasher.getMessageHash(encryptedMessage.bytes);
        encryptedMessage.iv = iv;

        return encryptedMessage;
    }

    public byte[] decryptWithAES(SecretKey secretKey) throws Exception {
        if (this.bytes == null) {
            return null;
        }
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");

        IvParameterSpec ivParameterSpec = new IvParameterSpec(this.iv);

        cipher.init(
                Cipher.DECRYPT_MODE,
                secretKey,
                ivParameterSpec);

        byte[] result = cipher.doFinal(this.bytes);

        return result;
    }

    private static byte[] createInitializationVector()
    {
        byte[] initializationVector = new byte[16];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(initializationVector);
        return initializationVector;
    }
}