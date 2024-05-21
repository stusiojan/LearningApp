package main;

import main.lib.User;

import javax.swing.*;
//import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CategoryPanel extends JPanel implements ActionListener {
    
    public CategoryPanel(User user) {
        add(new JLabel("Category panel " + user.getLogin() + " " + user.getId()));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }
}
