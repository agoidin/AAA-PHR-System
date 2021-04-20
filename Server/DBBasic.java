import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

abstract class DBBasic {

    protected Connection connection = null;

    private final String dbName;                                            //Filesystem path to database

    private final boolean debug = false;                                    //Set to true to enable debug messages

    /**
     * DBBasic Constructor.
     * Opens the DB
     * 
     * @param dbName name of the DB
     */
    public DBBasic(String dbName){
        this.dbName = dbName;

        if (debug)
            System.out.println("Db.constructor [" + dbName + "]");

        try {
            open();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Connects to DB
     * 
     * @throws SQLException
     */
    private void getConnection( ) throws SQLException {
        connection = DriverManager.getConnection(JDBCConstants.DATABASE_LOCATION + dbName);

        /*
         * Turn off AutoCommit:
         * delay updates until commit( ) called
         */
        connection.setAutoCommit(false);
    }

     /**
      * Opens DB.
     `* Confirms DB file exists and if so,
     `* loads JDBC driver and establishes JDBC connection to DB

      * @throws ClassNotFoundException
      * @throws SQLException
      */
    private void open() throws ClassNotFoundException, SQLException {
        File dbf = new File(dbName);

        if (!dbf.exists()) {
            System.out.println("SQLite database file [" + dbName + "] does not exist!" +
                                "\nCheck spelling or location!");
            System.exit( 0 );
        }

        Class.forName(JDBCConstants.JDBC_DRIVER);
        getConnection();

        if (debug)
            System.out.println( "Db.Open : leaving" );
    }

    /**
     * Close DB.
     * Commits any remaining updates to DB and closes connection
     * 
     * @throws SQLException
     */
    protected final void close() throws SQLException {
            //con.commit( ); // Commit any updates
            connection.close();
    }
}
