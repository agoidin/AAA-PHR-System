import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        //Uncomment 2 lines bellow when running just frontend - use the 'connector' variable in frontend
        //MockClientConnector connector = new MockClientConnector();
        //App app =  new App(connector);

        //Comment everything bellow when runnig just frontend
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 6600);
            //ServerInterface server = (ServerInterface) Naming.lookup("rmi://localhost/phr");
            ServerInterface server = (ServerInterface) registry.lookup("phr");
            ClientConnectorInterface connector = new ClientConnector(server);
            App app =  new App(connector);
            /*
            connector.connectToServer();
            boolean loggedIn = connector.loginUser("admin@phr.co.uk", "123");
            Role userRole = connector.getCurrentUserRole();
            connector.createUser("staff@phr.co.uk", "123", Role.STAFF);
            PatientRecord patientRecord = connector.getPatientRecord();
            List<PatientRecord> patientRecords = connector.getPatientRecords();
            List<PatientRecord> patientRecords2 = connector.getPatientRecords("John");
            PatientRecord patientRecord2 = connector.getPatientRecord(1);
            connector.updatePatientRecord(new PatientRecord(1, "John", "Wick", "O+", "10/01/1991", "Fish", "None", "Dr Laura Wilson"));
            List<UserRecord> users = connector.getAllUsers();
            connector.updateCurrentUserPassword("123456");
            boolean shouldChangePassword = connector.shouldChangePassword();
            connector.deleteUser(1); */
        }
        catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
