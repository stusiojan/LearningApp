package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.plaf.basic.*;

public class RegisterPanel extends JPanel implements ActionListener {

    private JPanel mainPanel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField passwordConfirmField;
    private JButton registerButton;
    private BasicArrowButton backButton;

    public RegisterPanel(JPanel mainPanel) {
        this.mainPanel = mainPanel;

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);
        setLayout(new GridBagLayout());

        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        passwordConfirmField = new JPasswordField(20);
        registerButton = new JButton("Register");
        backButton = new BasicArrowButton(BasicArrowButton.WEST);
        backButton.setBounds(5, 5, 50, 50);

        JLabel titleLabel = new JLabel("REGISTER");
        JLabel usernameLabel = new JLabel("Username");
        JLabel passwordLabel = new JLabel("Password");
        JLabel passwordConfirmLabel = new JLabel("Confirm Password");

        titleLabel.setFont(new Font(titleLabel.getFont().getName(), Font.BOLD, 24));
        titleLabel.setForeground(new Color(0,0,128));
        
        add(backButton);

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        add(titleLabel, constraints);

        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridx = 0;
        constraints.gridy += 1;     
        add(usernameLabel, constraints);
 
        constraints.gridx = 1;
        add(usernameField, constraints);
         
        constraints.gridx = 0;
        constraints.gridy += 1;     
        add(passwordLabel, constraints);
         
        constraints.gridx = 1;
        add(passwordField, constraints);

        constraints.gridx = 0;
        constraints.gridy += 1;     
        add(passwordConfirmLabel, constraints);
         
        constraints.gridx = 1;
        add(passwordConfirmField, constraints);
         
        constraints.gridx = 0;
        constraints.gridy += 1;
        constraints.gridwidth = 2;
        add(registerButton, constraints);

        backButton.addActionListener(this);
        registerButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        CardLayout cl = (CardLayout)mainPanel.getLayout();
        
        if (e.getSource() == backButton) {
            //mainPanel.removeAll();
            //mainPanel.add("login_panel", new LoginPanel(mainPanel));
            //mainPanel.revalidate();
            cl.show(mainPanel, "login_panel");
        }
    }
}
