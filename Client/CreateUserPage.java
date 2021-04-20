import javax.swing.*;
import java.awt.*;
import java.awt.Color;


/**
 * The CreateUserPage is where the new username, password and role of a user is declared.
 */
public class CreateUserPage {
    private JPanel createUserPane;
    private final Color backgroundColour;
    private App app;

    /**
     * @param a A call back to the app class to allow access to the methods connecting to the server (eg login, logout).
     * Creates the panel and declares the background colour for the page.
     */
    public CreateUserPage(App a){
        app = a;
        createUserPane = new JPanel();
        backgroundColour = Color.decode("#69d6d2");
    }


    /**
     * Adds all the components to the panel, ready to be drawn to the frame.
     */
    void buildPanel(){
        createUserPane.removeAll();
        createUserPane.setBackground(backgroundColour);

        JPanel titleContainer = new JPanel();
        titleContainer.setBackground(backgroundColour);
        JPanel buttonContainer = new JPanel();
        buttonContainer.setBackground(backgroundColour);

        JLabel title = new JLabel("<html><b>P</b>atient <b>H</b>ealth <b>R</b>ecords<html>", SwingConstants.LEFT);
        title.setFont(new Font("Monospace", Font.PLAIN, 20));

        titleContainer.add(title);

        JPanel inputGrid = new JPanel();
        inputGrid.setBackground(backgroundColour);
        inputGrid.setLayout(new GridLayout(1,1, 200, 20));

        JPanel userInput = new JPanel();
        
        userInput.setLayout(new BoxLayout(userInput, BoxLayout.PAGE_AXIS));
        userInput.setBackground(backgroundColour);
        
        userInput.add(Box.createRigidArea(new Dimension(0,25)));
        userInput.add(new JLabel("Email"));
        JTextField userField = new JTextField(15);
        userInput.add(userField);
        userInput.add(new JLabel("Password"));
        JTextField passField = new JTextField(15);
        userInput.add(passField);
        userInput.add(Box.createRigidArea(new Dimension(0,25)));

        inputGrid.add(userInput);

        JPanel roleInput = new JPanel();

        roleInput.setLayout(new BoxLayout(roleInput, BoxLayout.PAGE_AXIS));
        roleInput.setBackground(backgroundColour);

        ButtonGroup bg = new ButtonGroup();

        JRadioButton patientCheck = new JRadioButton("Patient", true);
        patientCheck.setBackground(backgroundColour);
        patientCheck.setActionCommand("patient");
        
        JRadioButton staffCheck = new JRadioButton("Staff");
        staffCheck.setBackground(backgroundColour);
        staffCheck.setActionCommand("staff");
        JRadioButton regulatorCheck = new JRadioButton("Regulator");
        regulatorCheck.setBackground(backgroundColour);
        regulatorCheck.setActionCommand("regulator");
        JRadioButton adminCheck = new JRadioButton("Admin");
        adminCheck.setBackground(backgroundColour);
        adminCheck.setActionCommand("admin");

        bg.add(patientCheck);
        bg.add(staffCheck);
        bg.add(regulatorCheck);
        bg.add(adminCheck);

     
        roleInput.add(new JLabel("Role: "));
        roleInput.add(patientCheck);
        roleInput.add(staffCheck);
        roleInput.add(regulatorCheck);
        roleInput.add(adminCheck);

        inputGrid.add(roleInput);
        JPanel container = new JPanel();
        container.setBackground(backgroundColour);
        container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));


        JButton createUserButton = new JButton("Create User");
        createUserButton.addActionListener(e -> {
            if(new PasswordChecker().checkPassword(passField.getText())) {
                app.createUser(userField.getText(), passField.getText(), bg.getSelection().getActionCommand());
                app.showUserListPane();
            } else {
                JOptionPane.showMessageDialog(null, "Password must have at least: \n" +
                                                            "- 8 characters\n" +
                                                            "- 1 lower-case letter\n" + 
                                                            "- 1 upper-case letter\n" + 
                                                            "- 1 digit\n" + 
                                                            "- 1 special character: !@#$%&*()'+,-./:;<=>?[]^_`{|}",
                                                      "Password Not Secure", JOptionPane.ERROR_MESSAGE);
            }
        });
        JButton logoutButton = new JButton("Back");
        logoutButton.addActionListener(e -> app.showUserListPane());
     
        buttonContainer.add(createUserButton);
        buttonContainer.add(logoutButton);
        //container.add(title);
        container.add(titleContainer);
        container.add(Box.createRigidArea(new Dimension(0,25)));
    
        container.add(inputGrid);

        container.add(buttonContainer);
        //container.add(logoutButton);
     
        createUserPane.add(container);
    }

    /**
     * @return returns the panel that all the components have been drawn to
     */
    public JPanel getPanel(){
        return createUserPane;
    }
}
