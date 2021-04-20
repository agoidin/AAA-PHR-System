import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.*;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.List;

public class ClientConnector implements ClientConnectorInterface
{
    private SecretKey secretKey;
    private String sessionId;
    private ServerInterface server;

    public ClientConnector(ServerInterface server) {
        this.server = server;
    }

    @Override
    public void connectToServer() throws Exception
    {
        EncryptedMessage serverResponse = server.negotiateSession(getEncryptedClientKeyExchangeMessage());
        receiveNegotiationResponse(serverResponse);
    }

    private void receiveNegotiationResponse(EncryptedMessage encryptedMessage) throws Exception
    {
        if (encryptedMessage.isItegrityValid()) {
            sessionId = Serializer.getStringFromBytes(encryptedMessage.decryptWithAES(secretKey));
        }
    }

    private EncryptedMessage getEncryptedClientKeyExchangeMessage() throws Exception
    {
        ClientKeyExchangeMessage message = new ClientKeyExchangeMessage();
        message.secretKey = generateSessionKey();
        secretKey = message.secretKey;

        EncryptedMessage encryptedMessage = new EncryptedMessage();
        encryptedMessage.bytes = encryptWithServerPublicKey(Serializer.serializeObject(message));
        encryptedMessage.digest = Hasher.getMessageHash(encryptedMessage.bytes);

        return encryptedMessage;
    }

    private SecretKey generateSessionKey() throws Exception {
        SecureRandom random = new SecureRandom();

        byte bytes[] = new byte[20];
        random.nextBytes(bytes);

        KeyGenerator keygenerator = KeyGenerator.getInstance("AES");
        keygenerator.init(256, random);
        return keygenerator.generateKey();
    }

    private static byte[] encryptWithServerPublicKey(byte[] data) {
        try {
            PublicKey publicKey = loadServerPublicKey();
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(data);
        }
        catch (Exception ex) {
            System.out.println(ex);
            System.out.println(ex.getMessage());
            return null;
        }
    }

    private static PublicKey loadServerPublicKey() throws Exception {
        FileInputStream keyfis = new FileInputStream("Keys/server_public");
        byte[] encKey = new byte[keyfis.available()];
        keyfis.read(encKey);

        keyfis.close();

        X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(encKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(pubKeySpec);
    }

    @Override
    public boolean loginUser(String email, String password) throws Exception
    {
        LoginUserClientMessage message = new LoginUserClientMessage();
        message.email = email;
        message.password = password;

        EncryptedMessage encryptedMessage = EncryptedMessage.encryptWithAES(secretKey, Serializer.serializeObject(message));

        return server.loginUser(sessionId, encryptedMessage);
    }

    @Override
    public void logout() throws Exception
    {
        connectToServer();
    }

    @Override
    public Role getCurrentUserRole() throws Exception
    {
        EncryptedMessage encryptedMessage = server.getCurrentUserRole(sessionId);

        if (encryptedMessage.isItegrityValid()) {
            Role role = Serializer.deserializeObject(encryptedMessage.decryptWithAES(secretKey));
            return role;
        }
        return null;
    }

    @Override
    public void createUser(String email, String password, Role role) throws Exception
    {
        CreateUserClientMessage message = new CreateUserClientMessage();
        message.email = email;
        message.password = password;
        message.role = role;

        server.createUser(sessionId, EncryptedMessage.encryptWithAES(secretKey, Serializer.serializeObject(message)));
    }

    @Override
    public PatientRecord getPatientRecord() throws Exception
    {
        EncryptedMessage encryptedMessage = server.getPatientRecord(sessionId);
        if (encryptedMessage == null) {
            return null;
        }

        return Serializer.deserializeObject(encryptedMessage.decryptWithAES(secretKey));
    }

    @Override
    public List<PatientRecord> getPatientRecords() throws Exception
    {
        EncryptedMessage encryptedMessage = server.getPatientRecords(sessionId);
        if (encryptedMessage == null) {
            return null;
        }

        return Serializer.deserializeObject(encryptedMessage.decryptWithAES(secretKey));
    }

    @Override
    public List<PatientRecord> getPatientRecords(String searchString) throws Exception
    {
        EncryptedMessage encryptedMessage = server.getPatientRecordsSearch(sessionId,
                EncryptedMessage.encryptWithAES(secretKey,
                    Serializer.serializeObject(new SearchClientMessage(searchString))));

        if (encryptedMessage == null) {
            return null;
        }

        return Serializer.deserializeObject(encryptedMessage.decryptWithAES(secretKey));
    }

    @Override
    public PatientRecord getPatientRecord(int id) throws Exception
    {
        EncryptedMessage encryptedMessage = server.getPatientRecordById(sessionId,
                EncryptedMessage.encryptWithAES(secretKey,
                        Serializer.intToByteArray(id)));

        if (encryptedMessage == null) {
            return null;
        }

        return Serializer.deserializeObject(encryptedMessage.decryptWithAES(secretKey));
    }

    @Override
    public void updatePatientRecord(PatientRecord updatedRecord) throws Exception
    {
        server.updatePatientRecord(sessionId,
                EncryptedMessage.encryptWithAES(secretKey,
                        Serializer.serializeObject(updatedRecord)));
    }

    @Override
    public List<UserRecord> getAllUsers() throws Exception
    {
        EncryptedMessage encryptedMessage = server.getAllUsers(sessionId);

        if (encryptedMessage == null) {
            return null;
        }

        return Serializer.deserializeObject(encryptedMessage.decryptWithAES(secretKey));
    }

    @Override
    public void deleteUser(int id) throws Exception
    {
        DeleteUserClientMessage message =  new DeleteUserClientMessage(id);
        server.deleteUser(sessionId,
                EncryptedMessage.encryptWithAES(secretKey, Serializer.serializeObject(message)));
    }

    @Override
    public void updateCurrentUserPassword(String newPassword) throws Exception
    {
        UpdatePasswordClientMessage message = new UpdatePasswordClientMessage(newPassword);
        server.updateCurrentUserPassword(sessionId,
                EncryptedMessage.encryptWithAES(secretKey, Serializer.serializeObject(message)));
    }

    @Override
    public boolean shouldChangePassword() throws Exception
    {
        return server.shouldCurrentUserChangePassword(sessionId);
    }
}
