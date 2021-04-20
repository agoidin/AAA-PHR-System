import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class PasswordHashing {

    private final String HASH_ALGORITHM = "SHA-256";
    
    private String securePassword;

    /**
     * PasswordHashing constructor.
     * Calls the PasswordSalt class to generate the salt.
     * Then calls generates the hash and asiigns to securePassword variable
     * 
     * @param email           email of the user
     * @param initialPassword user input password as plain text 
     * @throws NoSuchAlgorithmException
     * @throws SQLException
     */
    public PasswordHashing(String email, String initialPassword)
            throws NoSuchAlgorithmException, SQLException {
        PasswordSalt ps = new PasswordSalt(email);

        byte[] salt = ps.getSalt();

        securePassword = generateHash(initialPassword, HASH_ALGORITHM, salt);
    }

    /**
     * Generates the hash from initial password + user saved salt
     * 
     * @param initialPassword plain text password to hash 
     * @param algorithm       algorithm used for hashing
     * @param salt            user salt to add to hash
     * @return hash in String HEX format
     * @throws NoSuchAlgorithmException
     */
    private String generateHash(String initialPassword, String algorithm, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest ms = MessageDigest.getInstance(algorithm);
        ms.reset();
        ms.update(salt);

        byte[] hash = ms.digest(initialPassword.getBytes());

        return bytesToHex(hash);
    } 

    private static final byte[] HEX_ARRAY = "0123456789ABCDEF".getBytes(StandardCharsets.US_ASCII);

    /**
     * Converts bytes array to HEX String
     * 
     * @param bytes array to convet
     * @return HEX String
     */
    private static String bytesToHex(byte[] bytes) {
        byte[] hexChars = new byte[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars, StandardCharsets.UTF_8);
    }

    /**
     * Get hashed password
     * 
     * @return hashed password
     */
    public String getPass() {
        return securePassword;
    }
}
