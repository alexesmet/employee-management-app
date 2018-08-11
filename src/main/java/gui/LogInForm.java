package gui;

import entity.User;
import storage.UserStorage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LogInForm extends JFrame {
    private JPanel rootPanel;
    private JButton logInButton;
    private JPasswordField passwordField;
    private JTextField loginField;

    public LogInForm() {

        setContentPane(rootPanel);
        setTitle("Log In");
        pack();
        Dimension dimemsion = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dimemsion.width / 2 - this.getSize().width / 2, dimemsion.height / 2 - this.getSize().height / 2);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getRootPane().setDefaultButton(logInButton);

        logInButton.addActionListener(new LogInButtonActionListener());
    }

    private class LogInButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            String login = loginField.getText();
            String password = new String(passwordField.getPassword());
            User user = UserStorage.getUser(login);

            if (user == null) {
                JOptionPane.showMessageDialog(null, "Incorrect username.");
            } else if (!user.getPassword().equals(password)) {
                JOptionPane.showMessageDialog(null, "Incorrect password.");
            } else {

                new AccountPage(user).setVisible(true);
                dispose();
            }


        }
    }
}