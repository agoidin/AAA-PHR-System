import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server
        extends java.rmi.server.UnicastRemoteObject
        implements ServerInterface
{
    //SessionId is the Key
    private Map<String, Session> sessions = new HashMap<String, Session>();
    //private PersistenceInterface persistence;
    private LogWriter logWriter;
    private Object databaseLock = new Object();

    public Server(PersistenceInterface persistence, LogWriter logWriter) throws java.rmi.RemoteException {
        super();
        //this.persistence = persistence;
        this.logWriter = logWriter;
    }

    public EncryptedMessage negotiateSession(EncryptedMessage encryptedClientKeyExchangeMessage) throws Exception
    {
        if (!encryptedClientKeyExchangeMessage.isItegrityValid()) {
            logWriter.WriteWarningToLog("Invalid message integrity for session negotiation");
            return null;
        }

        PrivateKey priv = loadPrivateKey();

        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, priv);
        byte[] decryptedMessage = cipher.doFinal(encryptedClientKeyExchangeMessage.bytes);

        ClientKeyExchangeMessage clientKeyExchangeMessage = Serializer.deserializeObject(decryptedMessage);
        //Save session id with this secret key + return encrypted sessionId (using sessionKey)
        String sessionId = generateSessionId();
        Session session = new Session();
        session.secretKey = clientKeyExchangeMessage.secretKey;

        sessions.put(sessionId, session);

        logWriter.WriteEventToLog("SessionSucessfullyNegotiated", "SessionId: " + sessionId);
        return EncryptedMessage.encryptWithAES(session.secretKey, Serializer.getBytesFromString(sessionId));
    }

    @Override
    public boolean loginUser(String sessionId, EncryptedMessage encryptedLoginUserClientMessage) throws Exception
    {
        PersistenceInterface persistence = new DBCalls();

        if (sessions.containsKey(sessionId)) {
            byte[] decryptedMessage = encryptedLoginUserClientMessage.decryptWithAES(sessions.get(sessionId).secretKey);
            LoginUserClientMessage loginUserMessage = Serializer.deserializeObject(decryptedMessage);
            synchronized (databaseLock) {
                if (persistence.userCredentialsValid(loginUserMessage.email, loginUserMessage.password)) {
                    Session session = sessions.get(sessionId);
                    session.userEmail = loginUserMessage.email;
                    logWriter.WriteEventToLog("SucessfulLogin", "Email: " + session.userEmail + " SessionId " + sessionId);
                    return true;
                }
                else {
                    logWriter.WriteWarningToLog("Unsucessful login attempt for email " + loginUserMessage.email + " using SessionId: " + sessionId);
                }
            }
        }
        else {
            logWriter.WriteWarningToLog("Login attempt for not existing SessionId: " + sessionId);
        }
        return false;
    }

    @Override
    public EncryptedMessage getCurrentUserRole(String sessionId) throws Exception
    {
        PersistenceInterface persistence = new DBCalls();

        if (sessions.containsKey(sessionId)) {
            Session session = sessions.get(sessionId);
            if (session.userEmail != null) {
                synchronized (databaseLock)
                {
                    return EncryptedMessage.encryptWithAES(session.secretKey,
                            Serializer.serializeObject(persistence.getUserRole(session.userEmail)));
                }
            }
        }
        return null;
    }

    @Override
    public void createUser(String sessionId, EncryptedMessage encryptedMessage) throws Exception
    {
        PersistenceInterface persistence = new DBCalls();

        if (sessions.containsKey(sessionId)) {
            Session session = sessions.get(sessionId);
            if (session.userEmail != null) {
                if (persistence.getUserRole(session.userEmail) == Role.ADMIN) {
                    byte[] decryptedMessage = encryptedMessage.decryptWithAES(session.secretKey);
                    CreateUserClientMessage createUserMessage = Serializer.deserializeObject(decryptedMessage);
                    persistence = new DBCalls();
                    persistence.createUser(createUserMessage.email, createUserMessage.password, createUserMessage.role);
                    logWriter.WriteEventToLog("UserCreated", "CreatedEmail: " + createUserMessage.email + " CreatorEmail: " + session.userEmail);
                }
            }
        }
    }

    @Override
    public EncryptedMessage getPatientRecord(String sessionId) throws Exception
    {
        PersistenceInterface persistence = new DBCalls();

        if (sessions.containsKey(sessionId)) {
            Session session = sessions.get(sessionId);
            if (session.userEmail != null)
            {
                synchronized (databaseLock)
                {
                    PatientRecord patientRecord = persistence.getPatientRecord(session.userEmail);
                    return EncryptedMessage.encryptWithAES(session.secretKey, Serializer.serializeObject(patientRecord));
                }
            }
        }
        return null;
    }

    @Override
    public EncryptedMessage getPatientRecords(String sessionId) throws Exception
    {
        PersistenceInterface persistence = new DBCalls();
        if (sessions.containsKey(sessionId)) {
            Session session = sessions.get(sessionId);
            synchronized (databaseLock)
            {
                Role userRole = persistence.getUserRole(session.userEmail);
                if (session.userEmail != null && (userRole == Role.STAFF || userRole == Role.REGULATOR))
                {
                    persistence = new DBCalls();
                    List<PatientRecord> patientRecords = persistence.getPatientRecords();
                    return EncryptedMessage.encryptWithAES(session.secretKey, Serializer.serializeObject(patientRecords));
                }
            }
        }
        return null;
    }

    @Override
    public EncryptedMessage getPatientRecordsSearch(String sessionId, EncryptedMessage encryptedMessage) throws Exception
    {
        PersistenceInterface persistence = new DBCalls();

        if (sessions.containsKey(sessionId)) {
            Session session = sessions.get(sessionId);
            synchronized (databaseLock)
            {
                Role userRole = persistence.getUserRole(session.userEmail);
                if (session.userEmail != null && (userRole == Role.STAFF || userRole == Role.REGULATOR))
                {
                    persistence = new DBCalls();
                    SearchClientMessage searchClientMessage = Serializer.deserializeObject(encryptedMessage.decryptWithAES(session.secretKey));
                    List<PatientRecord> patientRecords = persistence.getPatientRecords(searchClientMessage.search);
                    return EncryptedMessage.encryptWithAES(session.secretKey, Serializer.serializeObject(patientRecords));
                }
            }
        }
        return null;
    }

    @Override
    public EncryptedMessage getPatientRecordById(String sessionId, EncryptedMessage encryptedMessage) throws Exception
    {
        PersistenceInterface persistence = new DBCalls();

        if (sessions.containsKey(sessionId)) {
            Session session = sessions.get(sessionId);
            synchronized (databaseLock)
            {
                Role userRole = persistence.getUserRole(session.userEmail);
                if (session.userEmail != null && (userRole == Role.STAFF || userRole == Role.REGULATOR))
                {
                    persistence = new DBCalls();
                    PatientRecord patientRecord = persistence.getPatientRecord(Serializer.byteArrayToInt(encryptedMessage.decryptWithAES(session.secretKey)));
                    return EncryptedMessage.encryptWithAES(session.secretKey, Serializer.serializeObject(patientRecord));
                }
            }
        }
        return null;
    }

    @Override
    public void updatePatientRecord(String sessionId, EncryptedMessage encryptedMessage) throws Exception
    {
        PersistenceInterface persistence = new DBCalls();

        if (sessions.containsKey(sessionId)) {
            Session session = sessions.get(sessionId);
            synchronized (databaseLock)
            {
                Role userRole = persistence.getUserRole(session.userEmail);
                if (session.userEmail != null && (userRole == Role.STAFF))
                {
                    persistence = new DBCalls();
                    PatientRecord patientRecord = Serializer.deserializeObject(encryptedMessage.decryptWithAES(session.secretKey));
                    persistence.updatePatientRecord(patientRecord);
                    logWriter.WriteEventToLog("PatientRecordUpdated", "RecordId: " + patientRecord.id + " UpdatorEmail: " + session.userEmail);
                }
            }
        }
    }

    @Override
    public boolean shouldCurrentUserChangePassword(String sessionId) throws Exception
    {
        PersistenceInterface persistence = new DBCalls();

        if (sessions.containsKey(sessionId)) {
            Session session = sessions.get(sessionId);
            if (session.userEmail != null) {
                synchronized (databaseLock)
                {
                    return persistence.shouldChangePassword(session.userEmail);
                }
            }
        }
        return false;
    }

    @Override
    public void deleteUser(String sessionId, EncryptedMessage encryptedMessage) throws Exception
    {
        PersistenceInterface persistence = new DBCalls();

        if (sessions.containsKey(sessionId)) {
            Session session = sessions.get(sessionId);
            synchronized (databaseLock)
            {
                Role userRole = persistence.getUserRole(session.userEmail);
                if (session.userEmail != null && (userRole == Role.ADMIN))
                {
                    DeleteUserClientMessage message = Serializer.deserializeObject(encryptedMessage.decryptWithAES(session.secretKey));
                    persistence = new DBCalls();
                    persistence.deleteUser(message.id);
                    logWriter.WriteEventToLog("UserDeleted", "UserId: " + message.id + " DeletorEmail: " + session.userEmail);
                }
            }
        }
    }

    @Override
    public EncryptedMessage getAllUsers(String sessionId) throws Exception
    {
        PersistenceInterface persistence = new DBCalls();

        if (sessions.containsKey(sessionId)) {
            Session session = sessions.get(sessionId);
            synchronized (databaseLock)
            {
                Role userRole = persistence.getUserRole(session.userEmail);
                if (session.userEmail != null && (userRole == Role.ADMIN))
                {
                    persistence = new DBCalls();
                    return EncryptedMessage.encryptWithAES(session.secretKey,
                            Serializer.serializeObject(persistence.getAllUsers()));
                }
            }
        }
        return null;
    }

    @Override
    public void updateCurrentUserPassword(String sessionId, EncryptedMessage encryptedMessage) throws Exception
    {
        PersistenceInterface persistence = new DBCalls();

        if (sessions.containsKey(sessionId)) {
            Session session = sessions.get(sessionId);
            if (session.userEmail != null) {
                UpdatePasswordClientMessage message = Serializer.deserializeObject(encryptedMessage.decryptWithAES(session.secretKey));
                synchronized (databaseLock)
                {
                    persistence = new DBCalls();
                    persistence.updateUserPassword(session.userEmail, message.newPassword);
                }
            }
        }
    }

    private String generateSessionId() throws IOException, ClassNotFoundException
    {
        SecureRandom random = new SecureRandom();

        byte bytes[] = new byte[10];
        random.nextBytes(bytes);

        String sessionId;
        do {
            sessionId = Serializer.getStringFromBytes(bytes);
        }
        while (sessions.containsKey(sessionId));

        return sessionId;
    }

    private PrivateKey loadPrivateKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException
    {
        File filePrivateKey = new File("Keys/server_private.key");
        FileInputStream fis = new FileInputStream("Keys/server_private.key");
        byte[] encodedPrivateKey = new byte[(int) filePrivateKey.length()];
        fis.read(encodedPrivateKey);
        fis.close();

        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(
                encodedPrivateKey);

        KeyFactory kf = KeyFactory.getInstance("RSA");

        return kf.generatePrivate(privateKeySpec);
    }
}
