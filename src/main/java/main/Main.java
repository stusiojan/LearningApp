package main;

import main.lib.DatabaseManager;
import java.awt.*;
import javax.swing.*;
//import main.gui.*;

public class Main {
    public static JFrame mainFrame;
    private static JPanel mainPanel;

    public static void main(String[] args) {
        DatabaseManager.connect();

        SwingUtilities.invokeLater( () -> {
            mainFrame = new JFrame();
            mainFrame.setTitle("LearningApp");
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            mainPanel = new JPanel(new CardLayout());
            mainPanel.add("login_panel", new LoginPanel(mainPanel));
            mainPanel.add("register_panel", new RegisterPanel(mainPanel));

            mainFrame.getContentPane().add(mainPanel);
            mainFrame.pack(); 
            mainFrame.setVisible(true);
        });
    }
}
