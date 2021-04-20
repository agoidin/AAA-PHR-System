import javax.swing.*;
import java.awt.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The login page is the first page the user sees, allowing them to log in to their accounts.
 */
public class LoginPage {

    private JPanel loginPane;
    private final Color backgroundColour;
    private App app;

     /**
     * @param a A call back to the app class to allow access to the methods connecting to the server (eg login, logout).
     * Creates the panel and declares the background colour for the page.
     */
    public LoginPage(App a){
        app = a;
        loginPane = new JPanel();
        backgroundColour = Color.decode("#69d6d2");
    }

    /**
     * Adds all the components to the panel, ready to be drawn to the frame.
     */
    public void buildPanel() throws Exception
    {
        loginPane.removeAll();
        loginPane.setLayout(new GridLayout(1,1));
        loginPane.setBackground(backgroundColour);

        JLabel loginText = new JLabel("<html>Patients<br/>Health<br/>Record</html>", SwingConstants.CENTER);
        loginText.setFont(new Font("Monospace", Font.BOLD, 46));


        JLabel usernameLabel = new JLabel("Email");
        usernameLabel.setFont(new Font("Monospace", Font.PLAIN, 20));
        JTextField usernameField = new JTextField();
        usernameField.setMaximumSize(new Dimension(400,20));

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Monospace", Font.PLAIN, 20));
        JPasswordField passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(400,20));
        passwordField.setEchoChar('*');
        
        JButton loginButton = new JButton("Login");
        //loginButton.addActionListener(e -> app.login(usernameField.getText(), passwordField.getText()));
        loginButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                try {
                    app.login(usernameField.getText(), String.valueOf(passwordField.getPassword()));
                }
                catch(Exception ex)
                {
                    JOptionPane.showMessageDialog(null, "Wrong email or password", "Wrong credentials", JOptionPane.ERROR_MESSAGE);
                    System.out.println(ex);
                }
            }
        });
        loginPane.add(loginText);

        JPanel loginForm = new JPanel();
        loginForm.setBackground(backgroundColour);
        loginForm.setLayout(new BoxLayout(loginForm, BoxLayout.PAGE_AXIS));
      
        
        loginForm.add(Box.createRigidArea(new Dimension(0,150)));
        loginForm.add(usernameLabel);
        loginForm.add(usernameField);
        loginForm.add(passwordLabel);
        loginForm.add(passwordField);

        loginForm.add(Box.createRigidArea(new Dimension(0,20)));
        loginForm.add(loginButton);
   
    
        loginPane.add(loginForm);
    }

    /**
     * @return returns the panel that all the components have been drawn to
     */
    public JPanel getPanel(){
        return loginPane;
    }
}
