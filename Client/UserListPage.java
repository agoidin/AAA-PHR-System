import javax.swing.*;
import java.awt.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class UserListPage {
    private JPanel userListPane;
    private final Color backgroundColour = Color.decode("#69d6d2");
    private App app;

    public UserListPage(App a){
        app = a;
        userListPane = new JPanel();
    }

    public void buildPanel(List<UserRecord> users){
        

        userListPane.removeAll();
        userListPane.setBackground(backgroundColour);
        userListPane.setLayout(new BorderLayout());

        // DefaultListModel<JPanel> model = new DefaultListModel<>();
        // JList<JPanel> list = new JList<>( model );
        JPanel userList = new JPanel();
        userList.setLayout(new BoxLayout(userList, BoxLayout.PAGE_AXIS));

        JPanel listElement [] = new JPanel[users.size()];
    
        for(int i = 0; i < listElement.length; i++){
            listElement[i] = new JPanel();
            listElement[i].setLayout(new GridLayout(1, 2));
            listElement[i].setBackground(backgroundColour);
            
            
            JPanel textContainer = new JPanel();
            JPanel buttonContainer = new JPanel();
            
            JButton deleteButton = new JButton("Delete");

            textContainer.add(new JLabel(users.get(i).email));
            textContainer.setBackground(backgroundColour);
            buttonContainer.add(deleteButton);
            buttonContainer.setBackground(backgroundColour);

            final int userId = users.get(i).id;
            deleteButton.addActionListener(e -> app.deleteUser(userId));

            listElement[i].add(textContainer);
            listElement[i].add(buttonContainer);
            userList.add(listElement[i]);
        }
        JScrollPane scrollPane = new JScrollPane(userList,
        JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        userListPane.add(scrollPane, BorderLayout.CENTER);

        JPanel buttons = new JPanel();

        JButton logoutButton = new JButton("Logout");
        JButton createUserButton = new JButton("Create User");

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
        createUserButton.addActionListener(e -> app.showCreateUserPane());
     
        buttons.add(createUserButton);
        buttons.add(logoutButton);

        userListPane.add(buttons, BorderLayout.SOUTH);
    }

    public JPanel getPanel(){
        return userListPane;
    }
}
