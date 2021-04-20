import javax.swing.*;
import java.awt.*;
import java.awt.Color;
import java.awt.event.*;
import java.util.Arrays;

public class ChangePassword
{
    private JPanel pwPane;
    private final Color backgroundColour;
    private App app;
    //JFrame frame;

    public ChangePassword(App a){
        app = a;
        pwPane = new JPanel();
        backgroundColour = Color.decode("#69d6d2");
    }

    public void buildPanel() throws Exception
    {
        // frame = new JFrame("Patients Health Record");
        // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        pwPane.removeAll();
        pwPane.setLayout(new GridLayout(1,1));
        pwPane.setBackground(backgroundColour);

        JLabel loginText = new JLabel("<html>Patients<br/>Health<br/>Record</html>", SwingConstants.CENTER);
        loginText.setFont(new Font("Monospace", Font.BOLD, 46));


        JLabel newPassword = new JLabel("Enter New Password");
        newPassword.setFont(new Font("Monospace", Font.PLAIN, 20));
        JPasswordField pwField = new JPasswordField();
        pwField.setMaximumSize(new Dimension(400,20));
        pwField.setEchoChar('*');

        JLabel newPassword2 = new JLabel("Re-enter Password");
        newPassword2.setFont(new Font("Monospace", Font.PLAIN, 20));
        JPasswordField pwField2 = new JPasswordField();
        pwField2.setMaximumSize(new Dimension(400,20));
        pwField2.setEchoChar('*');
        
        JButton changePassword = new JButton("Change Password");
        //changePassword.addActionListener(e -> app.login(usernameField.getText(), passwordField.getText()));
        pwPane.add(loginText);

        JPanel pwForm = new JPanel();
        pwForm.setBackground(backgroundColour);
        pwForm.setLayout(new BoxLayout(pwForm, BoxLayout.PAGE_AXIS));
      
        
        pwForm.add(Box.createRigidArea(new Dimension(0,150)));
        pwForm.add(newPassword);
        pwForm.add(pwField);
        pwForm.add(newPassword2);
        pwForm.add(pwField2);

        pwForm.add(Box.createRigidArea(new Dimension(0,20)));
        pwForm.add(changePassword);
    
        pwPane.add(pwForm);

        // frame.add(pwPane);

        // frame.setVisible(false);

        changePassword.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                try {
                    // boolean formatCheck = checkPassword(pwField);
                    boolean formatCheck = new PasswordChecker().checkPassword(String.valueOf(pwField.getPassword()));
                    boolean matchCheck = passwordMatchCheck(pwField, pwField2);

                    if(formatCheck && matchCheck)
                    {
                        char[] p = pwField.getPassword();
                        String newPassword = new String(p);
                        System.out.println("Password Changed");
                        app.updateCurrentUserPassword(newPassword);
                        app.logout();
                    } else if(!matchCheck){
                        JOptionPane.showMessageDialog(null, "Passwords must match", "Passwords Don't Match", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Password must have at least: \n" +
                                                            "- 8 characters\n" +
                                                            "- 1 lower-case letter\n" +
                                                            "- 1 upper-case letter\n" +
                                                            "- 1 digit\n" +
                                                            "- 1 special character: !@#$%&*()'+,-./:;<=>?[]^_`{|}",
                                                      "Password Not Secure", JOptionPane.ERROR_MESSAGE);
                    }
                }
                catch (Exception ex) {
                    System.out.println(ex);
                }
            }
        });
    }

    public JPanel getPanel(){
        return pwPane;
    }

    //Changed with PasswordChecker class used for Back-end too, so we have same checker 
    /* public boolean checkPassword(JPasswordField password)
    {
        boolean check = false;
        boolean isEmpty = true;
        boolean lengthCheck = false;
        boolean containsUp = false;
        boolean containsNum = false;
        boolean containsSpecial = false;

        char[] p = password.getPassword();
        String s = new String(p);
        String specialChars = "/*!@#$%^&*()\"{}_[]|\\?/<>,.";

        if(s == null){
            isEmpty = false;
        }

        for(int i = 0; i < p.length; i++)
        {
            if(p.length >= 8)
            {
                lengthCheck = true;
            }
            if(Character.isUpperCase(p[i]))
            {
                containsUp = true;
            }
            if(Character.isDigit(p[i]))
            {
                containsNum = true;
            }
            if(specialChars.contains(Character.toString(p[i])))
            {
                containsSpecial = true;
            }
        }

        if(lengthCheck == true && containsUp == true && containsNum == true && containsSpecial == true)
        {
            check = true;
            System.out.println("Valid password");
        }
        else
        {
            System.out.println("Invalid password");

        }

        return check;

    } */

    public boolean passwordMatchCheck(JPasswordField password, JPasswordField password2)
    {
        boolean matchCheck = false;
        char[] p = password.getPassword();
        char[] p2 = password2.getPassword();
        System.out.println(p);        
        System.out.println(p2);

        if(Arrays.equals(p, p2))
        {
            matchCheck = true;
            System.out.println("the passwords match");
        }
        else
        {
            System.out.println("the passwords do not match");
        }

        return matchCheck;
    }
}