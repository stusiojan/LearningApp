package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.plaf.basic.*;

import main.lib.DatabaseManager;

import java.util.Arrays;
import java.security.SecureRandom;
import java.security.spec.*;
import javax.crypto.spec.*;
import javax.crypto.*;

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

    private String generateSalt() {
        // FIXME: should probably be in a different class.
        final int SALT_LENGTH = 16;
        final String CHARSET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!\"#$%&\'()*+,-./:;<=>?@[\\]^_`{|}~";

        SecureRandom sr = new SecureRandom();

        String salt = "";
        for (int i = 0; i < SALT_LENGTH; i++)
        {   
            int index = sr.nextInt(CHARSET.length());
            salt += CHARSET.charAt(index);
        }
        return salt;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        CardLayout cl = (CardLayout)mainPanel.getLayout();
        
        if (e.getSource() == backButton) {
            cl.show(mainPanel, "login_panel");
        }
        else if (e.getSource() == registerButton) {
            String userLogin = usernameField.getText();
            char[] password = passwordField.getPassword();
            char[] passwordConfirm = passwordConfirmField.getPassword();

            if (userLogin.length() == 0) {
                JOptionPane.showMessageDialog(null, "The username cannot be empty.");
            }
            else if (!Arrays.equals(password, passwordConfirm)) {
                JOptionPane.showMessageDialog(null, "The passwords do not match.");
            }
            else if (DatabaseManager.hasUser(userLogin)) {
                JOptionPane.showMessageDialog(null, "The specified username already exists.");
            }
            else {
                try {
                    String salt = generateSalt();
                    KeySpec spec = new PBEKeySpec(password, salt.getBytes("US-ASCII"), 65536, 256);
                    SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
                    byte[] hash = skf.generateSecret(spec).getEncoded();
                    
                    Arrays.fill(password, '\0');
                    Arrays.fill(passwordConfirm, '\0');

                    StringBuilder userHash = new StringBuilder(2 * hash.length);
                    for (int i = 0; i < hash.length; i++) {
                        String hex = Integer.toHexString(0xff & hash[i]);
                        if (hex.length() == 1)
                            userHash.append('0');
                        userHash.append(hex);
                    }

                    DatabaseManager.addUser(userLogin, userHash.toString(), salt);
                    
                    JOptionPane.showMessageDialog(null, "The user was created.");
                    cl.show(mainPanel, "login_panel");
                } catch (Exception exc) {
                    JOptionPane.showMessageDialog(null, "Failed to create a user.", "", JOptionPane.ERROR_MESSAGE);
                }
            }            
        }
    }
}
