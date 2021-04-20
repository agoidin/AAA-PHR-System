import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.Base64;

public class PasswordSalt {
    private String saltString;

    private byte[] salt;

    private DBCalls calls;

    /**
     * PasswordSalt Constructor.
     * Checks whether user has already generated salt stored in DB.
     * If not then generates salt, encode to String representation and stores in DB.
     * If yes then gets the saltString from DB, decode back to salt byte array and assign with variables. 
     * 
     * @param email email of the user
     * @throws SQLException
     */
    public PasswordSalt(String email) throws SQLException{

        calls = new DBCalls();

        if(calls.getSalt(email) == null || calls.getSalt(email).equals("null") || calls.getSalt(email).equals("nosalt")) {
            System.out.println("no salt");
            salt = generateSalt();
            saltString = Base64.getEncoder().encodeToString(salt);

            calls.storeSalt(saltString, email);
        } else {
            saltString = calls.getSalt(email);
            salt = Base64.getDecoder().decode(saltString);
            calls.closeAll();
        }
    }

    /**
     * Generated the salt
     * 
     * @return salt as byte array
     */
    private byte[] generateSalt(){
        SecureRandom sr = new SecureRandom();
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }
    
    /**
     * Calls the DBCalls class to store the salt in DB
     * 
     * @param email email of the user
     * @throws SQLException
     */
    public void storeSalt(String email) throws SQLException {
        calls.storeSalt(saltString, email);
    }

    /**
     * Get salt as String
     * 
     * @return salt as String
     */
    public String getSaltString() {
        return saltString;
    }

    /**
     * Get salt as byte array
     * 
     * @return salt as byte array
     */
    public byte[] getSalt() {
        return salt;
    }
}
