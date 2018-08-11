package gui;

import entity.Role;
import entity.User;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class AccountPage extends JFrame {

    private JPanel rootPanel;
    private JPanel infoPanel;
    private JTextField idField;
    private JTextField usernameField;
    private JTextField nameField;
    private JTextField wageField;
    private JTextField scheduleField;
    private JTextField roleField;
    private JTextField departmentField;
    private JPanel actionPanel;
    private JButton reqVacButton;
    private JButton changeButton;
    private JPanel privelegedPanel;
    private JButton vacManButton;
    private JButton empManButton;
    private JButton depManButton;

    public AccountPage(User user) {

        setContentPane(rootPanel);
        setTitle("Your account - EmpMan");
        pack();

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dimension.width / 2 - this.getSize().width / 2, dimension.height / 2 - this.getSize().height / 2);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        idField.setText("" + user.getId());
        usernameField.setText(user.getLogin());
        nameField.setText(user.getName());
        wageField.setText("" + user.getWage());
        scheduleField.setText(user.getSchedule());
        roleField.setText(user.getRole().toString());
        departmentField.setText(user.getDepartment() != null ? user.getDepartment().getName() : "");

        if (user.getRole().equals(Role.EMPLOYEE)) {
            empManButton.setEnabled(false);
        }

        changeButton.addActionListener((event) -> new ChangePasswordForm(user).setVisible(true));
        reqVacButton.addActionListener((event) -> new VacationRequestForm(user).setVisible(true));
        empManButton.addActionListener((event) -> new EmployeeManagementForm(user).setVisible(true));
        depManButton.addActionListener((event) -> new DepartmentManagementForm().setVisible(true));
        vacManButton.addActionListener((event) -> new VacationManagementForm(user).setVisible(true));

        switch (user.getRole()) {
            case HEAD:
                depManButton.setEnabled(true);
            case MANAGER:
                empManButton.setEnabled(true);

        }

    }

}
