import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MockPersistence implements PersistenceInterface
{
    @Override
    public boolean userCredentialsValid(String email, String password)
    {
        return true;
    }

    @Override
    public Role getUserRole(String email)
    {
        return Role.STAFF;
    }

    @Override
    public void createUser(String email, String initialPassword, Role role)
    {

    }

    @Override
    public void updateUserPassword(String email, String newPassword) throws SQLException
    {

    }

    @Override
    public PatientRecord getPatientRecord(String email) {
        return new PatientRecord(1, "John", "Wick", "O+", "10/01/1991", "Fish", "None", "Dr Laura Wilson");
    }

    @Override
    public List<PatientRecord> getPatientRecords() {
        List<PatientRecord> response = new ArrayList<>();
        response.add(new PatientRecord(1, "John", "Wick", "O+", "10/01/1991", "Fish", "None", "Dr Laura Wilson"));
        response.add(new PatientRecord(2, "John", "Snow", "A+", "10/01/1491", "None", "None", "Dr Aaron Cosgrove"));
        return response;
    }

    @Override
    public List<PatientRecord> getPatientRecords(String searchString) {
        List<PatientRecord> response = new ArrayList<>();
        response.add(new PatientRecord(1, "John", "Wick", "O+", "10/01/1991", "Fish", "None", "Dr Laura Wilson"));
        response.add(new PatientRecord(2, "John", "Snow", "A+", "10/01/1491", "None", "None", "Dr Aaron Cosgrove"));
        return response;
    }

    @Override
    public List<UserRecord> getAllUsers() throws SQLException
    {
        List<UserRecord> response = new ArrayList<>();
        response.add(new UserRecord(1, "user1@test.com", Role.PATIENT, true));
        response.add(new UserRecord(2, "user2@test.com", Role.STAFF, false));
        response.add(new UserRecord(3, "user3@test.com", Role.REGULATOR, false));
        response.add(new UserRecord(4, "user4@test.com", Role.PATIENT, false));
        return response;
    }

    @Override
    public void deleteUser(int id)
    {

    }

    @Override
    public boolean shouldChangePassword(String email) throws SQLException
    {
        return false;
    }

    @Override
    public PatientRecord getPatientRecord(int id) {
        return new PatientRecord(1, "John", "Wick", "O+", "10/01/1991", "Fish", "None", "Dr Laura Wilson");
    }

    @Override
    public void updatePatientRecord(PatientRecord updatedRecord) {

    }
}
