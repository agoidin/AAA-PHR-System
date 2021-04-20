import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;

public interface PersistenceInterface {
    boolean userCredentialsValid(String email, String password) throws SQLException, NoSuchAlgorithmException;
    Role getUserRole(String email) throws SQLException;
    void createUser(String email, String initialPassword, Role role) throws SQLException, NoSuchAlgorithmException;
    //NEW
    void updateUserPassword(String email, String newPassword) throws SQLException, NoSuchAlgorithmException;
    void updatePatientRecord(PatientRecord updatedRecord) throws SQLException;
    PatientRecord getPatientRecord(String email) throws SQLException;
    PatientRecord getPatientRecord(int id) throws SQLException;
    List<PatientRecord> getPatientRecords() throws SQLException;
    List<PatientRecord> getPatientRecords(String searchString) throws SQLException;
    //NEW
    List<UserRecord> getAllUsers() throws SQLException;
    //NEW
    void deleteUser(int id) throws SQLException;
    //NEW
    boolean shouldChangePassword(String email) throws SQLException;
}
