public interface ServerInterface extends java.rmi.Remote
{
    EncryptedMessage negotiateSession(EncryptedMessage encryptedClientKeyExchangeMessage) throws Exception;
    boolean loginUser(String sessionId, EncryptedMessage encryptedLoginUserClientMessage) throws Exception;
    EncryptedMessage getCurrentUserRole(String sessionId) throws Exception;
    void createUser(String sessionId, EncryptedMessage encryptedMessage) throws Exception;
    EncryptedMessage getPatientRecord(String sessionId) throws Exception;
    EncryptedMessage getPatientRecords(String sessionId) throws Exception;
    EncryptedMessage getPatientRecordsSearch(String sessionId, EncryptedMessage encryptedMessage) throws Exception;
    EncryptedMessage getPatientRecordById(String sessionId, EncryptedMessage encryptedMessage) throws Exception;
    void updatePatientRecord(String sessionId, EncryptedMessage encryptedMessage) throws Exception;
    boolean shouldCurrentUserChangePassword(String sessionId) throws Exception;
    void deleteUser(String sessionId, EncryptedMessage encryptedMessage) throws Exception;
    EncryptedMessage getAllUsers(String sessionId) throws Exception;
    void updateCurrentUserPassword(String sessionId, EncryptedMessage encryptedMessage) throws Exception;
}