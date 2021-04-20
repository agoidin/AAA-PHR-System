import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;

public class Main {

    public static void main(String [ ] args)
    {
        LogWriter logWriter = new LogWriter();
        try {
            PersistenceInterface persistence = new DBCalls();
            //PersistenceInterface persistence = new MockPersistence();
            ServerInterface server = new Server(persistence, logWriter);
            Registry registry = LocateRegistry.createRegistry(6600);
            registry.rebind("phr", server);
        } catch (Exception e) {
            logWriter.WriteExceptionToLog(e);
            System.out.println("Server Error: " + e);
        }
    }
}
