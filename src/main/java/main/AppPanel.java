package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AppPanel extends JPanel implements ActionListener {

    private JPanel mainPanel;
    private JMenuItem userLogout;

    public AppPanel(JPanel mainPanel, String userLogin)
    {
        this.mainPanel = mainPanel;

        userLogout = new JMenuItem("Logout");
        userLogout.addActionListener(this);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Dashboard", new DashboardPanel(userLogin));
        tabs.addTab("Categories", new CategoryPanel(userLogin));

        JMenu userMenu = new JMenu("User");
        userMenu.add(userLogout);

        JMenuBar menubar = new JMenuBar();
        menubar.add(userMenu);

        setLayout(new BorderLayout());
        add(menubar, BorderLayout.NORTH);
        add(tabs);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        CardLayout cl = (CardLayout)mainPanel.getLayout();

        if (e.getSource() == userLogout) {
            cl.show(mainPanel, "login_panel");
        }
    }
}