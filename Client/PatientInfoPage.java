import javax.swing.*;
import java.awt.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * The PatientInfoPage handes the page that contains all the information for the patient
 * but doesn't allow the user to edit the information.
 */
public class PatientInfoPage {
    private JPanel patientInfoPane;
    private final Color backgroundColour;
    private App app;

     /**
     * @param a A call back to the app class to allow access to the methods connecting to the server (eg login, logout).
     * Creates the panel and declares the background colour for the page.
     */
    public PatientInfoPage(App a){
        app = a;
        patientInfoPane = new JPanel();
        backgroundColour = Color.decode("#69d6d2");
    }

    /**
     * Adds all the components to the panel, ready to be drawn to the frame.
     */
    public void buildPanel(PatientRecord patient) throws Exception {
        patientInfoPane.removeAll();
        patientInfoPane.setBackground(backgroundColour);

        JPanel titleContainer = new JPanel();
        titleContainer.setBackground(backgroundColour);
        JPanel logoutContainer = new JPanel();
        logoutContainer.setBackground(backgroundColour);

        JLabel title = new JLabel("<html><b>P</b>atient <b>H</b>ealth <b>R</b>ecords<html>", SwingConstants.LEFT);
        title.setFont(new Font("Monospace", Font.PLAIN, 20));
        JButton logoutButton = new JButton("Logout");
        //logoutButton.addActionListener(e -> app.logout());
        logoutButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                try {
                    app.logout();
                }
                catch(Exception ex)
                {
                    System.out.println(ex);
                }
            }
        });


        titleContainer.add(title);
        logoutContainer.add(logoutButton);

        JPanel informationGrid = new JPanel();
        informationGrid.setBackground(backgroundColour);
        informationGrid.setLayout(new GridLayout(4,2, 200, 20));
        informationGrid.add(new JLabel("ID: " + patient.id));
        informationGrid.add(new JLabel("Name: " + (patient.firstname != null ? patient.firstname : "-")));
        informationGrid.add(new JLabel("Surname: " + (patient.surname != null ? patient.surname : "-")));
        informationGrid.add(new JLabel("DOB: " + (patient.dateOfBirth != null ? patient.dateOfBirth : "-")));
        informationGrid.add(new JLabel("Blood Group: " + (patient.bloodGroup != null ? patient.bloodGroup : "-")));
        informationGrid.add(new JLabel("Allergies: " + (patient.allergies != null ? patient.allergies : "-")));
        informationGrid.add(new JLabel("Disabilities: " + (patient.disabilities != null ? patient.disabilities : "-")));
        informationGrid.add(new JLabel("GP: " + (patient.gp != null ? patient.gp : "-")));

        JPanel container = new JPanel();
        container.setBackground(backgroundColour);
        container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));

        //container.add(title);
        container.add(titleContainer);
        container.add(Box.createRigidArea(new Dimension(0,25)));
        container.add(informationGrid);
        container.add(logoutContainer);
        //container.add(logoutButton);
     
        patientInfoPane.add(container);
    }

    /**
     * @return returns the panel that all the components have been drawn to
     */
    public JPanel getPanel(){
        return patientInfoPane;
    }
}
