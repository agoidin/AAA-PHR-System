import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.util.List;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.*;
import java.util.*; 
import javax.swing.table.*;

public class patientsTable
{
    private App app;
    //private ServerInterface server;
    ClientConnectorInterface c;

    List<PatientRecord> patientList = new ArrayList<PatientRecord>();
    List<PatientRecord> searchList = new ArrayList<PatientRecord>();

    //Dummy data
    /* PatientRecord one = new PatientRecord(1, "John", "Smith", "1996.01.12", "A", null, null, "Doctor");
    PatientRecord two = new PatientRecord(2, "Lucy", "Davis", "1992.05.19", "B", null, null, "Doctor");
    PatientRecord three = new PatientRecord(3, "Harry", "Something", "1987.08.08", "O", "fish", null, "Doctor");
    PatientRecord four = new PatientRecord(4, "Charlotte", "Mei", "1946.12.29", "A", null, "blind", "Doctor");
    PatientRecord five = new PatientRecord(5, "Bob", "B", "1956.12.29", "B", "nuts", "deaf", "Doctor");
    PatientRecord six = new PatientRecord(6, "John", "Li", "1996.01.12", "A", null, null, "Doctor"); */

    PatientRecord searchedRecord;

    String column[]={"ID","Name","Surname", "Date Of Birth", "Blood Group", "Allergies", "Disabilities", "GP"};

    JFrame frame;    
    JTable table;
    JPanel panel = new JPanel(new GridBagLayout()); 
    JLabel label = new JLabel("Patients Health Record    ");
    JButton add = new JButton("Add New");
    JButton search = new JButton("Search User");
    JTextField textField = new JTextField(20);
    JButton reset = new JButton("Clear Search");

    patientsTable(App a, ClientConnectorInterface connector)
    {   
        app = a;
        c = connector;
        /*
         * if(user = admin, regulator, staff)
         */
        try {
            patientList = c.getPatientRecords();
            System.out.println("works");
        } catch (Exception e) {
            System.out.println("Error");
        }

        /* patientList.add(one);
        patientList.add(two);
        patientList.add(three);
        patientList.add(four);
        patientList.add(five);
        patientList.add(six); */

        frame = new JFrame("Patients Health Record");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 

        Object[][] tableData = new Object[patientList.size()][8];
        
        tableData = createTable(patientList, tableData);

        DefaultTableModel model = new DefaultTableModel(tableData, column);
        table = new JTable(model);   
        int rows = table.getRowCount();

        panel.add(label);
        //panel.add(add);
        panel.add(textField);
        panel.add(search);
        panel.add(reset);

        JButton logOut = new JButton("Log Out ");

        /*
        logOut.addActionListener(e -> {
            frame.dispose();
            app.logout();  
        }); */

        logOut.addActionListener((new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                try {
                    frame.dispose();
                    app.logout();
                }
                catch(Exception ex)
                {
                    System.out.println(ex);
                }
            }
        }));

        frame.add(BorderLayout.NORTH, new JScrollPane(table));
        frame.add(BorderLayout.CENTER, panel);
        frame.add(BorderLayout.SOUTH, logOut);
        
        frame.setVisible(true);

        search.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                try{
                    String s = textField.getText();
                    searchList = connector.getPatientRecords(s);
                    
                    Object[][] tableResults = new Object[searchList.size()][8];
                    tableResults = createTable(searchList, tableResults);
                    DefaultTableModel modelResults = new DefaultTableModel(tableResults,column);
                    table.setModel(modelResults);
                    
                }catch(Exception exc){

                } 

            
                
            }
        });

        reset.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                table.setModel(model);
            }
        });

        table.addMouseListener(
            new MouseInputAdapter(){
                public void mousePressed(MouseEvent e)      // or mouseClicked          
                {
                    if (e.getClickCount() == 2)
                    {
                        Role currentRole;
                        try{
                            currentRole = app.getCurrentUserRole();
                        }catch(Exception exc){
                            currentRole = null;
                        }
                            if(currentRole.equals(Role.STAFF)){
                            JTable target = (JTable)e.getSource();
                            int row = target.getSelectedRow();
                            int numCols = target.getColumnCount();

                            String data [] = new String[numCols];
                            for(int i = 1; i < numCols; i ++){
                                data[i - 1] = (String) target.getValueAt(row, i);
                                System.out.println(data[i]);
                            }

                            frame.dispose();
                            app.showEditInfoPane(new PatientRecord((int) target.getValueAt(row, 0), data[0], data[1], data[3], data[2], data[4], data[5], data[6]));
                        }
                    }
                }
            }
        );
    }

    public Object[][] createTable(List<PatientRecord> patientList, Object[][] tableData)
    {
        int index = 0;
        for (PatientRecord p : patientList)
        {
            tableData[index][0] = p.id;
            tableData[index][1] = p.firstname;
            tableData[index][2] = p.surname;
            tableData[index][3] = p.dateOfBirth;
            tableData[index][4] = p.bloodGroup;
            tableData[index][5] = p.allergies;
            tableData[index][6] = p.disabilities;
            tableData[index][7] = p.gp;
            index++;
        } 
        return tableData;
    }

    public JPanel getPanel(){
        return panel;
    }

    public JFrame getFrame(){
        return frame;
    }
}     

// table.getSelectionModel().addListSelectionListener(new ListSelectionListener()
// {
//     public void valueChanged(ListSelectionEvent event) 
//     {
//         System.out.println("To do...");
//     }
// });