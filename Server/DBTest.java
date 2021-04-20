import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;

/**
 * Class for testing the MySQL Calls
 */
public class DBTest {
    DBTest() throws SQLException, NoSuchAlgorithmException {
        DBCalls calls = new DBCalls();

        // System.out.println(calls.userExists("admin@phr.co.uk"));

        // calls.createUser("staff1@phr.co.uk", "staff", Role.STAFF);

        // calls.addPatient(2);
/* 
        PatientRecord pr =  new PatientRecord();
        pr.id = 23;
        pr.firstname = "TEST";
        pr.surname = "test";
        pr.dateOfBirth = "05-06-1975";
        pr.bloodGroup = "B-";
        pr.allergies = "diary products";
        pr.gp = "Dr. Mila Parker";
        calls.updatePatientRecord(pr); */

        // System.out.println(calls.getPatientRecord(20));

        /* List<PatientRecord> list = calls.getPatientRecords();
        for (PatientRecord patientRecord : list) {
            System.out.println(patientRecord.toString());;
        } */

        /* List<PatientRecord> list = calls.getPatientRecords("73452");
        for (PatientRecord patientRecord : list) {
            System.out.println(patientRecord.toString());
        } */

        // System.out.println(calls.passwordIsUpdated("admin@phr.co.uk"));

        
        // System.out.println(calls.getUserRole("admin@phr.co.uk"));
        
        
        
        
        // calls.storeSalt("admin@phr.co.uk");
        
        // System.out.println(calls.getSalt("staff@phr.co.uk"));
        
        // calls.updateUserPassword("staff4@phr.co.uk", "Staff123!");
        // System.out.println(calls.userCredentialsValid("staff4@phr.co.uk", "Staff123!"));
        // calls.emptySalt("patient4@email.com");
        // calls.updateUserPassword("patient@email.com", "Qwerty123!");
        // System.out.println(calls.userExists("admin@phr.co.uk"));
        // calls.createUser("patient8@email.com", "123", Role.PATIENT);
        // calls.createUser("staff11@phr.co.uk", "staff", Role.STAFF);
        // calls.deleteUser(27);
        // calls.closeAll();
        // System.out.println(calls.shouldChangePassword("staff2@phr.co.uk"));

        if (calls.userCredentialsValid("admin@phr.co.uk", "Admin123!")) {

            DBCalls calls2 = new DBCalls();
            if(calls2.getUserRole("admin@phr.co.uk") == Role.ADMIN) {

                DBCalls calls3 = new DBCalls();
                List<UserRecord> list = calls3.getAllUsers();
                for (UserRecord userRecord : list) {
                    System.out.println(userRecord.toString());
                }

            } else { System.out.println("not admin"); }

        } else { System.out.println("wrong username or pass"); }
    }

    public static void main(String[] args) {
        try {
            new DBTest();
        } catch (SQLException | NoSuchAlgorithmException sqlException) {
            sqlException.printStackTrace();
        }
    }
}
