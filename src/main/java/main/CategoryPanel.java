package main;

import javax.swing.*;
//import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CategoryPanel extends JPanel implements ActionListener {
    
    public CategoryPanel(String userLogin) {
        add(new JLabel("Category panel " + userLogin));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }
}
