package main;

import javax.swing.*;

import main.lib.DatabaseManager;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Arrays;
import java.security.spec.*;
import javax.crypto.spec.*;
import javax.crypto.*;

public class LoginPanel extends JPanel implements ActionListener {

    private JPanel mainPanel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    public LoginPanel(JPanel mainPanel) {
        this.mainPanel = mainPanel;

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);
        setLayout(new GridBagLayout());

        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Login");
        registerButton = new JButton("Register");
        JLabel titleLabel = new JLabel("LOGIN");
        JLabel usernameLabel = new JLabel("Username");
        JLabel passwordLabel = new JLabel("Password");

        titleLabel.setFont(new Font(titleLabel.getFont().getName(), Font.BOLD, 24));
        titleLabel.setForeground(new Color(0,0,128));

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
        constraints.gridwidth = 2;
        add(loginButton, constraints);

        constraints.gridx = 1;
        add(registerButton, constraints);

        loginButton.addActionListener(this);
        registerButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        CardLayout cl = (CardLayout)mainPanel.getLayout();
        
        if (e.getSource() == loginButton) {
            try {
                boolean loginSucceeded = false;
                String userLogin = usernameField.getText();

                if (DatabaseManager.hasUser(userLogin)) {
                    char password[] = passwordField.getPassword();
                    String salt = DatabaseManager.getSalt(userLogin);
                    String expectedHash = DatabaseManager.getHash(userLogin);

                    KeySpec spec = new PBEKeySpec(password, salt.getBytes("US-ASCII"), 65536, 256);
                    SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
                    byte[] hash = skf.generateSecret(spec).getEncoded();

                    StringBuilder providedHash = new StringBuilder(2 * hash.length);
                    for (int i = 0; i < hash.length; i++) {
                        String hex = Integer.toHexString(0xff & hash[i]);
                        if (hex.length() == 1)
                            providedHash.append('0');
                        providedHash.append(hex);
                    }

                    Arrays.fill(password, '\0');

                    if (expectedHash.equals(providedHash.toString())) {
                        loginSucceeded = true;
                        mainPanel.add("app_panel", new AppPanel(mainPanel, userLogin));
                        mainPanel.revalidate();
                        cl.show(mainPanel, "app_panel");
                    }
                }

                if (!loginSucceeded) {
                    JOptionPane.showMessageDialog(null, "Incorrec username or password.");
                }
            } catch (Exception exc) {
                JOptionPane.showMessageDialog(null, "An error occurred while authenticating the user.", "", JOptionPane.ERROR_MESSAGE);
            }
        }
        else if (e.getSource() == registerButton) {
            cl.show(mainPanel, "register_panel");
        }
    }
}
