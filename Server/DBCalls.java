import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DBCalls extends DBAccess implements PersistenceInterface {

    public DBCalls() {
        super(DBConstants.DATABASE_NAME);
    }

    /**
     * Checks if users input credentials exist in DB and are valid
     * 
     * @param email    entered value to check if exists
     * @param password entered value to check if equal to DB record related to email
     * @return True if Credentials exist and equal to user input, else False
     * @throws SQLException
     * @throws NoSuchAlgorithmException
     */
    @Override
    public boolean userCredentialsValid(String email, String password) throws SQLException, NoSuchAlgorithmException {

        if(userExists(email)) {
            PasswordHashing ph = new PasswordHashing(email, password);

            rs = executeQuery("SELECT " + DBConstants.EMAIL_COLUMN + ", " + DBConstants.PASSWORD_COLUMN + " FROM "
                    + DBConstants.USERS_TABLE + " WHERE " + DBConstants.EMAIL_COLUMN + " = \"" + email + "\";");

            String userEmail = rs.getString(DBConstants.EMAIL_COLUMN);
            String userPassword = rs.getString(DBConstants.PASSWORD_COLUMN);

            if(rs == null) { closeAll(); return false; } 
            else { closeAll(); return userEmail.equals(email) && userPassword.equals(ph.getPass()); }
        }
        else { closeAll(); return false; }
    }

    /**
     * Get user Role by email
     * 
     * @param email value to check the related Role
     * @return the user's Role
     * @throws SQLException
     */
    @Override
    public Role getUserRole(String email) throws SQLException {

        rs = executeQuery("SELECT " + DBConstants.ROLE_COLUMN + " FROM " + DBConstants.USERS_TABLE + " WHERE "
                + DBConstants.EMAIL_COLUMN + " = \"" + email + "\";");


        if(rs == null) { closeAll(); return null; }
        else {
            String response = rs.getString(DBConstants.ROLE_COLUMN);
            closeAll();

            switch (response) {
                case "admin":
                    return Role.ADMIN;
                case "staff":
                    return Role.STAFF;
                case "regulator":
                    return Role.REGULATOR;
                case "patient":
                    return Role.PATIENT;
            }
    
            System.out.println("!STH INCORRECT!");
            return null;
        }
    }

    /**
     * Get user Role by id
     * 
     * @param id value to check the related Role
     * @return the user's Role
     * @throws SQLException
     */
    public Role getUserRole(int id) throws SQLException {

        rs = executeQuery("SELECT " + DBConstants.ROLE_COLUMN + " FROM " + DBConstants.USERS_TABLE + " WHERE "
                + DBConstants.ID_COLUMN + " = " + id + ";");

        if(rs == null) { closeAll(); return null; }
        else {
            String response = rs.getString(DBConstants.ROLE_COLUMN);
            closeAll();

            switch (response) {
                case "admin":
                    return Role.ADMIN;
                case "staff":
                    return Role.STAFF;
                case "regulator":
                    return Role.REGULATOR;
                case "patient":
                    return Role.PATIENT;
            }
    
            System.out.println("!STH INCORRECT!");
            return null;
        }
    }

    /**
     * Checks whether password of the user is updated by the user or created by
     * admin
     * 
     * @param email credentials of the user
     * @return True if password was updated, else False
     * @throws SQLException
     */
    public boolean shouldChangePassword(String email) throws SQLException {
        rs = executeQuery("SELECT " + DBConstants.PASSWORD_STATUS_COLUMN + " FROM " + DBConstants.USERS_TABLE
                            + " WHERE " + DBConstants.EMAIL_COLUMN + " = \"" + email + "\";");

        if(rs == null) { closeAll(); return true; }
        else {
            String response = rs.getString(DBConstants.PASSWORD_STATUS_COLUMN);
            closeAll();
            
            return Boolean.parseBoolean(response);
        }
    }

    /**
     * Creates the the new user Record in Users table
     * 
     * @param email           userName to use for login
     * @param initialPassword temporary password for first login
     * @param role            the Role of user
     * @throws SQLException
     * @throws NoSuchAlgorithmException
     */
    @Override
    public void createUser(String email, String initialPassword, Role role)
            throws SQLException, NoSuchAlgorithmException {

        if(!userExists(email)) {

            executeUpdate("INSERT INTO " + DBConstants.USERS_TABLE + " VALUES (NULL, \"" + email +
                          "\", \"nopass\", \"" + role.toString().toLowerCase() + "\", \"true\", \"nosalt\" );");
    
            PasswordHashing ph = new PasswordHashing(email, initialPassword);
    
            //Adding salt and hashed pass
            executeUpdate(
                "UPDATE " + DBConstants.USERS_TABLE +
                " SET " +
                    DBConstants.PASSWORD_COLUMN + " = \"" + ph.getPass() + "\"" +
                " WHERE " + DBConstants.EMAIL_COLUMN + " = \"" + email + "\";"
            );
            
            // If created user is Patient calls for addPatient method to create empty record in Patients Table
            if (role.equals(Role.PATIENT)) {
    
                rs = executeQuery("SELECT id FROM users WHERE " + DBConstants.EMAIL_COLUMN + " = \"" + email + "\";");
    
                int id = rs.getInt(DBConstants.ID_COLUMN);
                // Prints the ID of created user
                System.out.println(id);
                addPatient(id);
                System.out.println("Patient created");
            }
        }
        closeAll();
    }

    /**
     * Creates the PatientRecord based on email input value and fills all the
     * variables with values from related DB entry
     * 
     * @param email value to return related DB entry
     * @return PatientRecord record with filled values
     * @throws SQLException
     */
    @Override
    public PatientRecord getPatientRecord(String email) throws SQLException {

        rs = executeQuery("SELECT " + DBConstants.PATIENTS_TABLE + ".*" + " FROM " + DBConstants.PATIENTS_TABLE
                + " JOIN " + DBConstants.USERS_TABLE + " ON " + DBConstants.USERS_TABLE + "." + DBConstants.ID_COLUMN
                + " = " + DBConstants.PATIENTS_TABLE + "." + DBConstants.USER_ID_COLUMN + " WHERE "
                + DBConstants.USERS_TABLE + "." + DBConstants.EMAIL_COLUMN + " = " + "\"" + email + "\";");

        if(rs == null) { closeAll(); return null; }

        PatientRecord record = new PatientRecord();
        record.id = rs.getInt(DBConstants.USER_ID_COLUMN);
        record.firstname = rs.getString(DBConstants.FIRSTNAME_COLUMN);
        record.surname = rs.getString(DBConstants.SURNAME_COLUMN);
        record.dateOfBirth = rs.getString(DBConstants.DOB_COLUMN);
        record.bloodGroup = rs.getString(DBConstants.BLOOD_GROUP_COLUMN);
        record.allergies = rs.getString(DBConstants.ALLERGIES_COLUMN);
        record.disabilities = rs.getString(DBConstants.DISABILITIES_COLUMN);
        record.gp = rs.getString(DBConstants.GP_COLUMN);

        closeAll();

        return record;
    }

    /**
     * Creates the PatientRecord based on id input value and fills all the variables
     * with values from related DB entry
     * 
     * @param id value to return related DB entry
     * @return PatientRecord record with filled values
     * @throws SQLException
     */
    @Override
    public PatientRecord getPatientRecord(int id) throws SQLException {

        rs = executeQuery("SELECT * FROM " + DBConstants.PATIENTS_TABLE + " WHERE " + DBConstants.USER_ID_COLUMN + " = "
                + id + ";");

        if(rs == null) { closeAll(); return null; }
        
        PatientRecord record = new PatientRecord();
        record.id = rs.getInt(DBConstants.USER_ID_COLUMN);
        record.firstname = rs.getString(DBConstants.FIRSTNAME_COLUMN);
        record.surname = rs.getString(DBConstants.SURNAME_COLUMN);
        record.dateOfBirth = rs.getString(DBConstants.DOB_COLUMN);
        record.bloodGroup = rs.getString(DBConstants.BLOOD_GROUP_COLUMN);
        record.allergies = rs.getString(DBConstants.ALLERGIES_COLUMN);
        record.disabilities = rs.getString(DBConstants.DISABILITIES_COLUMN);
        record.gp = rs.getString(DBConstants.GP_COLUMN);

        closeAll();

        return record;
    }

    /**
     * Takes all entries from Patients table and puts to related PatientRecord
     * variables. Then puts PatientRecord into list
     * 
     * @return list of all PatientRecords with filled values
     * @throws SQLException
     */
    @Override
    public List<PatientRecord> getPatientRecords() throws SQLException {
        List<PatientRecord> records = new ArrayList<>();

        rs = executeQuery("SELECT * FROM " + DBConstants.PATIENTS_TABLE + ";");

        if(rs == null) { closeAll(); return Collections.emptyList(); }

        while (rs.next()) {
            PatientRecord record = new PatientRecord();
            record.id = rs.getInt(DBConstants.USER_ID_COLUMN);
            record.firstname = rs.getString(DBConstants.FIRSTNAME_COLUMN);
            record.surname = rs.getString(DBConstants.SURNAME_COLUMN);
            record.dateOfBirth = rs.getString(DBConstants.DOB_COLUMN);
            record.bloodGroup = rs.getString(DBConstants.BLOOD_GROUP_COLUMN);
            record.allergies = rs.getString(DBConstants.ALLERGIES_COLUMN);
            record.disabilities = rs.getString(DBConstants.DISABILITIES_COLUMN);
            record.gp = rs.getString(DBConstants.GP_COLUMN);

            records.add(record);
        }

        closeAll();

        return records;
    }

    /**
     * Takes all entries from Patients table that has any table containing input
     * String and puts to related PatientRecord variables. Then puts PatientRecord
     * into list
     * 
     * @param searchString string to search for
     * @return list of all PatientRecords with filled values
     * @throws SQLException
     */
    @Override
    public List<PatientRecord> getPatientRecords(String searchString) throws SQLException {
        List<PatientRecord> records = new ArrayList<>();

        String searchIdString;
        try {
            int id = Integer.parseInt(searchString);
            searchIdString = DBConstants.USER_ID_COLUMN + " = " + id + " OR ";
        } catch (NumberFormatException e) {
            searchIdString = "";
        }

        rs = executeQuery("SELECT * FROM " + DBConstants.PATIENTS_TABLE +
                          " WHERE " +
                            searchIdString + 
                            DBConstants.FIRSTNAME_COLUMN + " LIKE \"%" + searchString + "%\" OR " + 
                            DBConstants.SURNAME_COLUMN + " LIKE \"%" + searchString + "%\" OR " + 
                            DBConstants.DOB_COLUMN + " LIKE \"%" + searchString + "%\" OR " + 
                            DBConstants.BLOOD_GROUP_COLUMN + " LIKE \"%" + searchString + "%\" OR " +
                            DBConstants.ALLERGIES_COLUMN + " LIKE \"%" + searchString + "%\" OR " +
                            DBConstants.DISABILITIES_COLUMN + " LIKE \"%" + searchString + "%\" OR " +
                            DBConstants.GP_COLUMN + " LIKE \"%" + searchString + "%\";"
                         );

        if(rs == null) { closeAll(); return Collections.emptyList(); } 

        while (rs.next()) {
            PatientRecord record = new PatientRecord();
            record.id = rs.getInt(DBConstants.USER_ID_COLUMN);
            record.firstname = rs.getString(DBConstants.FIRSTNAME_COLUMN);
            record.surname = rs.getString(DBConstants.SURNAME_COLUMN);
            record.dateOfBirth = rs.getString(DBConstants.DOB_COLUMN);
            record.bloodGroup = rs.getString(DBConstants.BLOOD_GROUP_COLUMN);
            record.allergies = rs.getString(DBConstants.ALLERGIES_COLUMN);
            record.disabilities = rs.getString(DBConstants.DISABILITIES_COLUMN);
            record.gp = rs.getString(DBConstants.GP_COLUMN);

            records.add(record);
        }

        closeAll();
        return records;
    }

    /**
     * Updates the PatientRecord variables' values
     * 
     * @param updatedRecord ID of created user to add record as Reference Key in
     *                      Patients Table
     * @throws SQLException
     */
    @Override
    public void updatePatientRecord(PatientRecord updatedRecord) throws SQLException {

        executeUpdate("UPDATE " + DBConstants.PATIENTS_TABLE + " SET " + DBConstants.FIRSTNAME_COLUMN + " = \""
                + updatedRecord.firstname + "\", " + DBConstants.SURNAME_COLUMN + " = \"" + updatedRecord.surname
                + "\", " + DBConstants.DOB_COLUMN + " = \"" + updatedRecord.dateOfBirth + "\", "
                + DBConstants.BLOOD_GROUP_COLUMN + " = \"" + updatedRecord.bloodGroup + "\", "
                + DBConstants.ALLERGIES_COLUMN + " = \"" + updatedRecord.allergies + "\", "
                + DBConstants.DISABILITIES_COLUMN + " = \"" + updatedRecord.disabilities + "\", "
                + DBConstants.GP_COLUMN + " = \"" + updatedRecord.gp + "\" " + " WHERE " + DBConstants.USER_ID_COLUMN
                + " = " + updatedRecord.id + ";");

        closeAll();
    }

    /**
     * Adds the empty record with ID to Patients table.
     * 
     * @param userID ID of created user to add record as Reference Key in Patients
     *               Table
     * @throws SQLException
     */
    public void addPatient(int userID) throws SQLException {

        executeUpdate("INSERT INTO " + DBConstants.PATIENTS_TABLE + "(" + DBConstants.USER_ID_COLUMN + ") VALUES("
                + userID + ");");
    }

    /**
     * Checks whether the user with given email exists in Users table
     * 
     * @param email users email to check
     * @throws SQLException
     */
    public boolean userExists(String email) throws SQLException {

        rs = executeQuery("SELECT " + DBConstants.ID_COLUMN + " FROM " + DBConstants.USERS_TABLE + " WHERE "
                + DBConstants.EMAIL_COLUMN + " = \"" + email + "\";");
    
        return rs != null;
    }

    /**
     * Updates the password of selected user
     * 
     * @param email       the identificator of required user
     * @param newPassword new password
     * @throws NoSuchAlgorithmException
     * @throws SQLException
     */
    @Override
    public void updateUserPassword(String email, String newPassword) throws SQLException, NoSuchAlgorithmException {
        if(userExists(email)){
            if(new PasswordChecker().checkPassword(newPassword)) {
                PasswordHashing ph = new PasswordHashing(email, newPassword);
        
                executeUpdate(
                    "UPDATE " + DBConstants.USERS_TABLE +
                    " SET " +
                        DBConstants.PASSWORD_COLUMN + " = \"" + ph.getPass() + "\"," +
                        DBConstants.PASSWORD_STATUS_COLUMN + " = \"false\"" +
                    " WHERE " + DBConstants.EMAIL_COLUMN + " = \"" + email + "\";"
                );
            }
        }
        closeAll();
    }

    /**
     * Takes all entries from Users table and puts
     * to related UserRecord variables. Then puts UserRecord into list
     * 
     * @return list of all UserRecords with filled values
     * @throws SQLException
     */
    @Override
    public List<UserRecord> getAllUsers() throws SQLException {
        List<UserRecord> records = new ArrayList<>();

        rs = executeQuery(
            "SELECT " + 
                DBConstants.ID_COLUMN + ", " +
                DBConstants.EMAIL_COLUMN + ", " +
                DBConstants.ROLE_COLUMN +
            " FROM " + DBConstants.USERS_TABLE + ";"
        );
        
        if(rs == null) {closeAll(); return Collections.emptyList(); } 

        while(rs.next()) {
            UserRecord record = new UserRecord();
            record.id = rs.getInt(DBConstants.ID_COLUMN);
            record.email = rs.getString(DBConstants.EMAIL_COLUMN);

            switch (rs.getString(DBConstants.ROLE_COLUMN)) {
                case "admin":
                    record.role = Role.ADMIN;
                    break;
                case "staff":
                    record.role = Role.STAFF;
                    break;
                case "patient":
                    record.role = Role.PATIENT;
                    break;
                case "regulator":
                    record.role = Role.REGULATOR;
                    break;
            }

            records.add(record);
        }
        
        closeAll();

        return records;
    }

    /**
     * Delete entry in Users Table.
     * If user's Role is Patient, delete the entry from Patient record too  
     * 
     * @param id the identificator of required entry to delete
     * @throws SQLException
     */
    @Override
    public void deleteUser(int id) throws SQLException {
        deletePatientRecord(id);

        executeUpdate(
            "DELETE FROM " + DBConstants.USERS_TABLE +
            " WHERE " + DBConstants.ID_COLUMN + " = " + id + ";"
        );

        closeAll();
    }

    /**
     * Delete entry in Patients Table 
     * 
     * @param id the identificator of required entry
     * @throws SQLException
     */
    private void deletePatientRecord(int id) throws SQLException {
        executeUpdate(
            "DELETE FROM " + DBConstants.PATIENTS_TABLE +
            " WHERE " + DBConstants.USER_ID_COLUMN + " = " + id + ";"
        );
    }

    /**
     * Stores the salt in Users table
     * 
     * @param salt  value to store
     * @param email user's email
     * @throws SQLException
     */
    public void storeSalt(String salt, String email) throws SQLException {
        // String salt = "123";
        executeUpdate(
            "UPDATE " + DBConstants.USERS_TABLE +
            " SET " +
                DBConstants.SALT_COLUMN + " = \"" + salt + "\"" +
            " WHERE " + DBConstants.EMAIL_COLUMN + " = \"" + email + "\";"
        );

        closeAll();
    }

    /**
     * Get the salt of the user from DB
     * 
     * @param email user's email
     * @return salt as String
     * @throws SQLException
     */
    public String getSalt(String email) throws SQLException {
        rs = executeQuery(
            "SELECT " + DBConstants.SALT_COLUMN +
            " FROM " + DBConstants.USERS_TABLE +
            " WHERE " + DBConstants.EMAIL_COLUMN + " = \"" + email + "\";"
        );

        if (rs == null) return null;
        return rs.getString(DBConstants.SALT_COLUMN);
    }

    //Debug method to set salt to empty
    public void emptySalt(String email) throws SQLException {
        // String salt = "123";
        executeUpdate(
            "UPDATE " + DBConstants.USERS_TABLE +
            " SET " +
                DBConstants.SALT_COLUMN + " = NULL" +
            " WHERE " + DBConstants.EMAIL_COLUMN + " = \"" + email + "\";"
        );

        closeAll();
    }

    // Debugging method to check whether ResultSet returns sth or not
    public ResultSet check(String table) throws SQLException {

        return executeQuery("SELECT * FROM " + table + ";");
    }
}
