package gui;

import entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import storage.UserStorage;
import util.HibernateUtil;

import javax.persistence.PersistenceException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChangePasswordForm extends JFrame {
    private JPasswordField newPswdField;
    private JPanel rootPanel;
    private JPasswordField oldPswdField;
    private JPasswordField repPswdField;
    private JLabel statusLabel;
    private JButton submitButton;
    private JPanel fieldPanel;
    private User user;

    public ChangePasswordForm(User user) {
        this.user = user;
        setContentPane(rootPanel);
        setTitle("Change password");
        pack();

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dimension.width / 2 - this.getSize().width / 2, dimension.height / 2 - this.getSize().height / 2);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getRootPane().setDefaultButton(submitButton);

        submitButton.addActionListener(new SubmitButtonActionListener());
        statusLabel.setText("   ");
    }

    private class SubmitButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {

            if (!newPswdField.getText().equals(repPswdField.getText())) {
                statusLabel.setText("Passwords do not match.");
                statusLabel.setForeground(new Color(200, 50, 50));
            } else if (newPswdField.getText().equals(oldPswdField.getText())) {
                statusLabel.setText("Old and new one do not differ.");
                statusLabel.setForeground(new Color(199, 162, 52));
            } else if (!user.getPassword().equals(oldPswdField.getText())) {
                statusLabel.setText("Incorrect old password.");
                statusLabel.setForeground(new Color(200, 50, 50));
            } else {
                try {
                    SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
                    Session session = sessionFactory.openSession();
                    Transaction transaction = session.beginTransaction();

                    user.setPassword(newPswdField.getText());
                    session.update(user);
                    transaction.commit();
                    session.close();

                    statusLabel.setText("Successfully changed password!");
                    statusLabel.setForeground(new Color(50, 199, 50));

                } catch (PersistenceException e) {
                    System.err.println("Persistence Exception!!!");
                    statusLabel.setText("Unexpected error!.");
                    statusLabel.setForeground(new Color(200, 50, 50));
                }
            }

        }
    }
}
