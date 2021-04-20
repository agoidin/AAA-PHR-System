import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MockClientConnector implements  ClientConnectorInterface
{
    @Override
    public void connectToServer() throws Exception
    {
    }

    @Override
    public boolean loginUser(String email, String password) throws Exception
    {
        return true;
    }

    @Override
    public void logout() throws Exception
    {
        
    }

    @Override
    public Role getCurrentUserRole() throws Exception
    {
        return Role.ADMIN;
    }

    @Override
    public void createUser(String email, String password, Role role) throws Exception
    {

    }

    @Override
    public PatientRecord getPatientRecord() throws Exception
    {
        return new PatientRecord(1, "John", "Wick", "O+", "10/01/1991", "Fish", "None", "Dr Laura Wilson");
    }

    @Override
    public List<PatientRecord> getPatientRecords() throws Exception
    {
        List<PatientRecord> response = new ArrayList<>();
        response.add(new PatientRecord(1, "John", "Wick", "O+", "10/01/1991", "Fish", "None", "Dr Laura Wilson"));
        response.add(new PatientRecord(2, "John", "Snow", "A+", "10/01/1491", "None", "None", "Dr Aaron Cosgrove"));
        return response;
    }

    @Override
    public List<PatientRecord> getPatientRecords(String searchString) throws Exception
    {
        List<PatientRecord> response = new ArrayList<>();
        response.add(new PatientRecord(1, "John", "Wick", "O+", "10/01/1991", "Fish", "None", "Dr Laura Wilson"));
        response.add(new PatientRecord(2, "John", "Snow", "A+", "10/01/1491", "None", "None", "Dr Aaron Cosgrove"));
        return response;
    }

    @Override
    public PatientRecord getPatientRecord(int id) throws Exception
    {
        return new PatientRecord(1, "John", "Wick", "O+", "10/01/1991", "Fish", "None", "Dr Laura Wilson");
    }

    @Override
    public void updatePatientRecord(PatientRecord updatedRecord) throws Exception
    {

    }

    @Override
    public List<UserRecord> getAllUsers() throws Exception
    {
        List<UserRecord> response = new ArrayList<>();
        response.add(new UserRecord(1, "user1@test.com", Role.PATIENT, true));
        response.add(new UserRecord(2, "user2@test.com", Role.STAFF, false));
        response.add(new UserRecord(3, "user3@test.com", Role.REGULATOR, false));
        response.add(new UserRecord(4, "user4@test.com", Role.PATIENT, false));
        return response;
    }

    @Override
    public void deleteUser(int id) throws Exception
    {

    }

    @Override
    public void updateCurrentUserPassword(String newPassword)
    {

    }

    @Override
    public boolean shouldChangePassword()
    {
        return true;
    }
}
