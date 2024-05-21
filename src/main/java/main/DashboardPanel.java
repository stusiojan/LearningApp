package main;

import main.lib.User;

import javax.swing.*;
//import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DashboardPanel extends JPanel implements ActionListener {

    public DashboardPanel(User user) {
        add(new JLabel("Dashboard panel " + user.getLogin() + " " + user.getId()));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
    }
}
