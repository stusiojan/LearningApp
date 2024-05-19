package main;

import javax.swing.*;
//import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DashboardPanel extends JPanel implements ActionListener {

    public DashboardPanel(String userLogin) {
        add(new JLabel("Dashboard panel " + userLogin));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }
}
