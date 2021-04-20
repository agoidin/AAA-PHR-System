public class DBConstants {
    /* DATABASE NAME */
    public static final String DATABASE_NAME = "PHR.db"; //ServerApp/src/
    
    /* TABLE NAMES */
    public static final String USERS_TABLE = "Users";
    public static final String PATIENTS_TABLE = "Patients";

    /* USERS TABLE COLUMNS */
    public static final String ID_COLUMN = "id";
    public static final String EMAIL_COLUMN = "email";
    public static final String PASSWORD_COLUMN = "password";
    public static final String ROLE_COLUMN = "role";
    public static final String PASSWORD_STATUS_COLUMN = "should_update_password";
    public static final String SALT_COLUMN = "salt";

    /* PATIENT TABLE COLUMNS */
    public static final String USER_ID_COLUMN = "user_id";
    public static final String FIRSTNAME_COLUMN = "firstname";
    public static final String SURNAME_COLUMN = "surname";
    public static final String DOB_COLUMN = "dob";
    public static final String BLOOD_GROUP_COLUMN = "blood_group";
    public static final String ALLERGIES_COLUMN = "allergies";
    public static final String DISABILITIES_COLUMN = "disabilities";
    public static final String GP_COLUMN = "gp";
}