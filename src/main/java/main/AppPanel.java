package main;

import main.lib.User;

import javax.swing.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class AppPanel extends JPanel implements ActionListener, ChangeListener {
    private JPanel mainPanel;
    private JMenuItem userLogout;
    private JTabbedPane tabs;
    private DashboardPanel dashboardPanel;
    private CategoryPanel categoryPanel;

    public AppPanel(JPanel mainPanel, User user) throws SQLException {
        this.mainPanel = mainPanel;

        userLogout = new JMenuItem("Logout");
        userLogout.addActionListener(this);

        dashboardPanel = new DashboardPanel(user);
        categoryPanel = new CategoryPanel(user);

        tabs = new JTabbedPane();
        tabs.addTab("Dashboard", dashboardPanel);
        tabs.addTab("Categories", categoryPanel);
        tabs.addChangeListener(this);

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

    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == tabs) {
            if (tabs.getSelectedComponent() == categoryPanel) {
                categoryPanel.rebuildUI();
            }
        }
    }
}