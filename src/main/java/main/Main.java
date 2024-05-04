package main;

import main.lib.DatabaseManager;
import javax.swing.*;
//import main.gui.*;
import java.sql.SQLException;

public class Main {
    public static JFrame mainFrame;
    private static JPanel mainPanel;

    public static void main(String[] args) {
        try {
            DatabaseManager.connect();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }


        SwingUtilities.invokeLater( () -> {
            mainFrame = new JFrame();
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

            JLabel label = new JLabel("Hello world");
            mainPanel.add(label);

            // adding panels

            mainFrame.setContentPane(mainPanel);
            mainFrame.pack();
            mainFrame.setVisible(true);
        });


    }

    public static void a() {
    }
}
