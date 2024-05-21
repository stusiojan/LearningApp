package main;

import main.lib.DatabaseManager;
import main.lib.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;

import java.util.Arrays;
import java.util.Optional;

public class LoginPanel extends JPanel implements ActionListener, ComponentListener {

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
        addComponentListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        CardLayout cl = (CardLayout)mainPanel.getLayout();
        
        if (e.getSource() == loginButton) {
            try {
                boolean loginSucceeded = false;
                String userLogin = usernameField.getText();
                Optional<User> user = DatabaseManager.getUser(userLogin);

                if (user.isPresent()) {
                    char password[] = passwordField.getPassword();
                    String providedHash = User.computeHash(password, user.get().getSalt());
                    String expectedHash = user.get().getHash();
                    Arrays.fill(password, '\0');

                    if (expectedHash.equals(providedHash)) {
                        loginSucceeded = true;
                        usernameField.setText("");
                        passwordField.setText("");
                        mainPanel.add("app_panel", new AppPanel(mainPanel, user.get()));
                        mainPanel.revalidate();
                        cl.show(mainPanel, "app_panel");
                    }
                }
                if (!loginSucceeded) {
                    JOptionPane.showMessageDialog(null, "Incorrect username or password.");
                }
            } catch (Exception exc) {
                JOptionPane.showMessageDialog(null, "An error occurred while authenticating the user.", "", JOptionPane.ERROR_MESSAGE);
            }
        }
        else if (e.getSource() == registerButton) {
            cl.show(mainPanel, "register_panel");
        }
    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }
    @Override
    public void componentMoved(ComponentEvent e) {

    }
    @Override
    public void componentResized(ComponentEvent e) {

    }
    @Override
    public void componentShown(ComponentEvent e) {
        usernameField.setText("");
        passwordField.setText("");
    }
}
