import javax.swing.*;
import java.awt.*;
import java.awt.Color;

/**
 * The PatientEditPage is the page that allows the user to view the information of the user and edit it.
 */

public class PatientEditPage {
    private JPanel patientEditPane;
    private final Color backgroundColour;
    private App app;

     /**
     * @param a A call back to the app class to allow access to the methods connecting to the server (eg login, logout).
     * Creates the panel and declares the background colour for the page.
     */
    public PatientEditPage(App a){
        app = a;
        patientEditPane = new JPanel();
        backgroundColour = Color.decode("#69d6d2");
    }

    /**
     * Adds all the components to the panel, ready to be drawn to the frame.
     */
    public void buildPanel(PatientRecord patient){
        patientEditPane.removeAll();
        patientEditPane.setBackground(backgroundColour);

        JPanel titleContainer = new JPanel();
        titleContainer.setBackground(backgroundColour);
        JPanel buttonContainer = new JPanel();
        buttonContainer.setBackground(backgroundColour);


        JLabel title = new JLabel("<html><b>P</b>atient <b>H</b>ealth <b>R</b>ecords<html>", SwingConstants.LEFT);
        title.setFont(new Font("Monospace", Font.PLAIN, 20));

        titleContainer.add(title);
        

        JPanel cell1 = new JPanel();
        cell1.setBackground(backgroundColour);
        JPanel cell2 = new JPanel();
        cell2.setBackground(backgroundColour);
        JPanel cell3 = new JPanel();
        cell3.setBackground(backgroundColour);
        JPanel cell4 = new JPanel();
        cell4.setBackground(backgroundColour);
        JPanel cell5 = new JPanel();
        cell5.setBackground(backgroundColour);
        JPanel cell6 = new JPanel();
        cell6.setBackground(backgroundColour);
        JPanel cell7 = new JPanel();
        cell7.setBackground(backgroundColour);
        JPanel cell8 = new JPanel();
        cell8.setBackground(backgroundColour);

        cell1.add(new JLabel("ID: "));
        cell2.add(new JLabel("Name: "));
        cell3.add(new JLabel("Surname: "));
        cell4.add(new JLabel("DOB: "));
        cell5.add(new JLabel("Blood Group: "));
        cell6.add(new JLabel("Allergies: "));
        cell7.add(new JLabel("Disabilities: "));
        cell8.add(new JLabel("GP: "));

        JLabel id = new JLabel("" + patient.id);
        JTextField name = new JTextField(patient.firstname, 10);
        JTextField surname = new JTextField(patient.surname, 10);
        JTextField dob = new JTextField(patient.dateOfBirth, 10);
        JTextField bloodGroup = new JTextField(patient.bloodGroup, 10);
        JTextField allergies = new JTextField(patient.allergies, 10);
        JTextField disabilities = new JTextField(patient.disabilities, 10);
        JTextField gp = new JTextField(patient.gp, 10);

        cell1.add(id);
        cell2.add(name);
        cell3.add(surname);
        cell4.add(dob);
        cell5.add(bloodGroup);
        cell6.add(allergies);
        cell7.add(disabilities);
        cell8.add(gp);
    
        JPanel informationGrid = new JPanel();
        informationGrid.setBackground(backgroundColour);
        informationGrid.setLayout(new GridLayout(4,2, 200, 20));
        informationGrid.add(cell1);
        informationGrid.add(cell2);
        informationGrid.add(cell3);
        informationGrid.add(cell4);
        informationGrid.add(cell5);
        informationGrid.add(cell6);
        informationGrid.add(cell7);
        informationGrid.add(cell8);

        JPanel container = new JPanel();
        container.setBackground(backgroundColour);
        container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));


        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> app.saveInfo(id.getText(), name.getText(), surname.getText(), dob.getText(), bloodGroup.getText(), allergies.getText(), disabilities.getText(), gp.getText()));
        JButton logoutButton = new JButton("Back");
        logoutButton.addActionListener(e -> app.showPatientsTablePane());
        
        buttonContainer.add(saveButton);
        buttonContainer.add(logoutButton);
        //container.add(title);
        container.add(titleContainer);
        container.add(Box.createRigidArea(new Dimension(0,25)));
        container.add(informationGrid);
        container.add(buttonContainer);
        //container.add(logoutButton);
        
        patientEditPane.add(container);
    }

    /**
     * @return returns the panel that all the components have been drawn to
     */
    public JPanel getPanel(){
        return patientEditPane;
    }
    
}
