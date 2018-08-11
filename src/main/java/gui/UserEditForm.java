package gui;

import entity.Department;
import entity.Role;
import entity.User;
import storage.UserStorage;
import util.Container;
import util.HibernateUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class UserEditForm extends JFrame {
    private JPanel rootPanel;
    private JPanel necessaryPanel;
    private JTextField usernameField;
    private JPasswordField passwordField1;
    private JPasswordField passwordField2;
    private JComboBox roleComboBox;
    private JPanel additionalPanel;
    private JComboBox depComboBox;
    private JTextField nameField;
    private JButton submitButton;
    private JLabel statusLabel;
    private JTextField wageField;
    private JTextField sceduleField;
    private User user;

    public UserEditForm(User user) {
        this.user = user;
        setContentPane(rootPanel);
        setTitle("Create/Edit user");
        pack();

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dimension.width / 2 - this.getSize().width / 2, dimension.height / 2 - this.getSize().height / 2);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getRootPane().setDefaultButton(submitButton);

        submitButton.addActionListener(new SubmitButtonActionListener());
        statusLabel.setText("   ");

        Container uc = new Container();
        HibernateUtil.transact(session -> uc.set(session.createQuery("from Department").list()));
        List<Department> departments = (List<Department>) uc.get();
        for (int i = 0; i < departments.size(); i++) {
            depComboBox.addItem(departments.get(i));
        }

        if (user != null) {
            usernameField.setText(user.getLogin());
            usernameField.setEnabled(false);
            switch (user.getRole()) {
                case MANAGER:
                    roleComboBox.setSelectedIndex(1);
                    break;
                case HEAD:
                    roleComboBox.setSelectedIndex(2);
                    break;
                default:
                    roleComboBox.setSelectedIndex(0);
            }
            if (user.getDepartment() != null) {
                depComboBox.setSelectedItem(user.getDepartment());
            }
            wageField.setText("" + user.getWage());
            nameField.setText(user.getName());
            sceduleField.setText(user.getSchedule());


        }
    }

    private class SubmitButtonActionListener implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            if (usernameField.getText().equals("")) {
                statusLabel.setText("Username cannot be empty.");
                statusLabel.setForeground(new Color(200, 50, 50));
            } else if (usernameField.getText().contains(" ")) {
                statusLabel.setText("Username cannot contain spaces.");
                statusLabel.setForeground(new Color(200, 50, 50));
            } else if (UserStorage.getUser(usernameField.getText()) != null && user == null) {
                statusLabel.setText("This username already exists.");
                statusLabel.setForeground(new Color(200, 50, 50));
            } else if (!passwordField1.getText().equals(passwordField2.getText())) {
                statusLabel.setText("Passwords do not match.");
                statusLabel.setForeground(new Color(200, 50, 50));
            } else if (passwordField1.getText().equals("") && user == null) {
                statusLabel.setText("Password cannot be empty.");
                statusLabel.setForeground(new Color(200, 50, 50));
            } else {
                if (user == null) {
                    user = new User();
                    user.setPassword(passwordField1.getText());
                }
                user.setLogin(usernameField.getText());
                user.setName(nameField.getText());

                Role role;
                switch (roleComboBox.getSelectedIndex()) {
                    case 2:
                        role = Role.HEAD;
                        break;
                    case 1:
                        role = Role.MANAGER;
                        break;
                    default:
                        role = Role.EMPLOYEE;
                        break;
                }
                user.setRole(role);
                user.setDepartment((Department) depComboBox.getSelectedItem());
                user.setSchedule(sceduleField.getText());
                try {
                    user.setWage(Float.parseFloat(wageField.getText()));
                } catch (NumberFormatException e) {
                    statusLabel.setText("Could not parse wage.");
                    statusLabel.setForeground(new Color(200, 50, 50));
                    return;
                }

                HibernateUtil.transact(session -> session.saveOrUpdate(user));

                statusLabel.setText("Successfully submitted changes.");
                statusLabel.setForeground(new Color(50, 199, 50));
            }
        }
    }
}
