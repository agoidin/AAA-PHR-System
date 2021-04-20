import java.util.List;

public interface ClientConnectorInterface
{
    void connectToServer() throws Exception;
    //If succesful true, if not false
    boolean loginUser(String email, String password) throws Exception;
    void logout() throws Exception;
    Role getCurrentUserRole() throws Exception;
    //This can be called by admin only
    void createUser(String email, String password, Role role) throws Exception;
    //Show their record to the patient
    PatientRecord getPatientRecord() throws Exception;
    //Show all patient records - can be called by staff and regulator
    List<PatientRecord> getPatientRecords() throws Exception;
    //Show all patient records for search expression - can be called by staff and regulator
    List<PatientRecord> getPatientRecords(String searchString) throws Exception;
    //Patient record detail - can be called by staff and regulator
    PatientRecord getPatientRecord(int id) throws Exception;
    //This can be called by staff only
    void updatePatientRecord(PatientRecord updatedRecord) throws Exception;
    //This can be called by admin only
    List<UserRecord> getAllUsers() throws Exception;
    //This can be called by admin only
    void deleteUser(int id) throws Exception;
    //This can be called only after logging in, changes password for that user
    void updateCurrentUserPassword(String newPassword) throws Exception;
    //Call after login to determine whether the user needs to change their password
    boolean shouldChangePassword() throws Exception;
}
