import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBAccess extends DBBasic {

    private Statement stmt;
    protected ResultSet rs;

    protected DBAccess(String dbName) {
        super(dbName);
    }

    protected ResultSet executeQuery(String stub) throws SQLException {
        stmt = connection.createStatement();
        rs = stmt.executeQuery(stub);

        System.out.println("Query executed!");
        connection.commit();

        return (rs.isClosed()) ? null : rs;
    }

    protected void executeUpdate(String stub) throws SQLException {
        stmt = connection.createStatement();
        stmt.executeUpdate(stub);

        System.out.println("Update executed!");
        connection.commit();
    }

    public void closeAll() throws SQLException {
        if (rs != null) rs.close();
        if (stmt != null) stmt.close();
        if (connection != null) super.close();
    }
}
