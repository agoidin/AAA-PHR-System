import javax.swing.*;

//import jdk.javadoc.internal.doclets.toolkit.util.ClassUseMapper;

import java.awt.*;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * The App class handles all client side actions like changing pages
 * and connects to the server.
 */

public class App extends JFrame {
    /**
     * Declares all the pages
     */
    private ClientConnectorInterface clientConnector;
    private LoginPage loginPage = new LoginPage(this);
    private PatientInfoPage patientInfoPage = new PatientInfoPage(this);
    private PatientEditPage patientEditPage = new PatientEditPage(this);
    private CreateUserPage createUserPage = new CreateUserPage(this);
    private UserListPage userListPage = new UserListPage(this);
    private ChangePassword passwordChangePage = new ChangePassword(this);
    //private patientsTable patientsTablePage = new patientsTable(this);
    
    private final CardLayout cLayout;
    private final JPanel mainPane;
    /**
     * All the constants to allow the mainPane to switch between each page using cardLayout
     */
    public final String LOGIN_PAGE = "login page";
    public final String PATIENT_INFO = "patient info";
    public final String PATIENT_EDIT = "patient edit";
    public final String CREATE_USER = "create user";
    public final String USER_LIST = "user list";
    public final String PASSWORD_CHANGE = "password change";
    public final String BUFFER = "buffer";
    //public final String PATIENTS_TABLE = "patients table";
    

    private PatientRecord patient = new PatientRecord();

    /**
     * The temporary username and password to simulate a login system without access to the server
     */
    public App(ClientConnectorInterface cc) throws Exception
    {
        clientConnector = cc;
        setTitle("Patient Health Records");
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setLocationRelativeTo(null);

            mainPane = new JPanel();
            mainPane.setPreferredSize(new Dimension(600,400));
            cLayout = new CardLayout();
            mainPane.setLayout(cLayout);

            mainPane.add(LOGIN_PAGE, loginPage.getPanel());
            mainPane.add(PATIENT_INFO, patientInfoPage.getPanel());
            mainPane.add(PATIENT_EDIT, patientEditPage.getPanel());
            mainPane.add(CREATE_USER, createUserPage.getPanel());
            mainPane.add(USER_LIST, userListPage.getPanel());
            mainPane.add(PASSWORD_CHANGE, passwordChangePage.getPanel());
            mainPane.add(BUFFER, new JPanel());
            //mainPane.add(PATIENTS_TABLE, patientsTablePage.getPanel());

        try{
            clientConnector.connectToServer();
            loginPage.buildPanel();

            setLayout(new BorderLayout());
            add(mainPane,BorderLayout.CENTER);

            pack();
            setVisible(true);
        }catch(Exception e){
           e.printStackTrace();
           System.out.println("Failed to connect");
        }
    }
    /**
     * Logs the user into their account, connecting to the server to do so.
     * @param username The username of the user
     * @param password The user's password
     */
    void login(String username, String password) throws Exception
    {
        boolean canLogIn = false;
        try{
            canLogIn = clientConnector.loginUser(username, password);
        }catch (Exception e) {
            e.printStackTrace();
        }
        Role currentRole = null;
        boolean passwordChange = false;
        if(canLogIn){
            try{
                currentRole = clientConnector.getCurrentUserRole();
                passwordChange = clientConnector.shouldChangePassword();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

        if(passwordChange){
            showPasswordChangePane();
        }else{
            switch(currentRole) {
                case PATIENT:
                    System.out.println("Patient logged in...");
                    showPatientInfoPane();
                    break;
                case STAFF:
                    System.out.println("Staff logged in...");
                    showPatientsTablePane();
                    break;
                case REGULATOR:
                    System.out.println("Regulator logged in...");
                    showPatientsTablePane();
                    break;
                case ADMIN:
                    System.out.println("Admin logged in...");
                    showUserListPane();
                    break;
                default:
                  // code block
              }
        }
        
    }

    /**
     * Saves the changed information to the server, changing the patien's information
     * @param id The patient's account Id
     * @param name The patient's first name
     * @param surname The patient's surname
     * @param dob The patient's Date of Birth
     * @param bloodGroup The patient's bloodGroup
     * @param allergies The patient's allergies
     * @param disabilities The patient's disabilities
     * @param gp The patient's GP
     */
    void saveInfo(String id, String name, String surname, String dob, String bloodGroup, String allergies, String disabilities, String gp){
        PatientRecord updated = new PatientRecord(Integer.parseInt(id), name, surname, bloodGroup, dob, allergies, disabilities, gp);
        try{
            clientConnector.updatePatientRecord(updated);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Logs the user out of the app
     */
    void logout()throws Exception {
        try{
            clientConnector.logout();
        }catch (Exception e) {
            e.printStackTrace();
        }
        //patientsTablePage.getFrame().setVisible(false);
        showLoginPane();
    }

    /**
     * Creates a new user and adds them to the server
     * @param user The username of the new user
     * @param password The password of the new user
     * @param role The role the account has: admin, patient, etc...
     */
    void createUser(String user, String password, String r){
        Role role = null;
        switch(r){
            case "patient":
                role = Role.PATIENT;
                break;
            case "staff":
                role = Role.STAFF;
                break;
            case "regulator":
                role = Role.REGULATOR;
                break;
            case "admin":
                role = Role.ADMIN;
                break;
        }

        try{
            clientConnector.createUser(user, password, role);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    List<UserRecord> getUsers(){
       try{
           return clientConnector.getAllUsers();
       }catch(Exception e){
           return null;
       }
    }

    void deleteUser(int id){
        try{
        clientConnector.deleteUser(id);
        System.out.println("Deleted user of id " + id);
        cLayout.show(mainPane, BUFFER);
        showUserListPane();
        }catch(Exception e){
            System.out.println("Failes to delete user");
        }
    }

    void updateCurrentUserPassword(String newPassword) throws Exception
    {
        clientConnector.updateCurrentUserPassword(newPassword);
    }
    /**
     * Swaps to the login page
    */
    void showLoginPane() throws Exception {
        loginPage.buildPanel();
        setVisible(true);
        cLayout.show(mainPane, LOGIN_PAGE);
    }

    Role getCurrentUserRole() throws Exception
    {
        try{
            return clientConnector.getCurrentUserRole();
        }catch(Exception e){
            return null;
        }
        
    }

    /**
     * Swaps to the patient info page
    */
    void showPatientInfoPane() throws Exception {
        patient = clientConnector.getPatientRecord();
        patientInfoPage.buildPanel(patient);
        setVisible(true);
        cLayout.show(mainPane, PATIENT_INFO);
    }

    /**
     * Swaps to the edit info page
    */
    void showEditInfoPane(PatientRecord patient){
        patientEditPage.buildPanel(patient);
        setVisible(true);
        cLayout.show(mainPane, PATIENT_EDIT);
    }

    /**
     * Swaps to the create user page
    */
    void showCreateUserPane(){
        createUserPage.buildPanel();
        setVisible(true);
        cLayout.show(mainPane, CREATE_USER);
    }
    
    void showPatientsTablePane(){
        patientsTable pt = new patientsTable(this, clientConnector);
        setVisible(false);
        cLayout.show(mainPane, BUFFER);
    }

    void showUserListPane(){
        userListPage.buildPanel(getUsers());
        setVisible(true);
        cLayout.show(mainPane, USER_LIST);
    }

    void showPasswordChangePane() throws Exception
    {
        passwordChangePage.buildPanel();
        setVisible(true);
        cLayout.show(mainPane, PASSWORD_CHANGE);
    }
}
