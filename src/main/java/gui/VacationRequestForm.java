package gui;

import entity.User;
import entity.Vacation;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.jdesktop.swingx.JXDatePicker;
import util.HibernateUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

public class VacationRequestForm extends JFrame {
    private JPanel rootPanel;
    private JPanel datePickPanel;
    private JXDatePicker beginDate;
    private JXDatePicker endDate;
    private JLabel statusLabel;
    private JButton submitButton;
    private User user;

    public VacationRequestForm(User user) {
        this.user = user;

        setContentPane(rootPanel);
        setTitle("Request a vacation - EmpMan");
        statusLabel.setText("The end date must be after the start date.");
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
        public void actionPerformed(ActionEvent actionEvent) {
            if (beginDate.getDate().after(endDate.getDate())) {
                statusLabel.setText("The end date must be after the start date.");
                statusLabel.setForeground(new Color(200, 50, 50));
            } else if (beginDate.getDate().before(new Date())) {
                statusLabel.setText("you can make requests only for the future.");
                statusLabel.setForeground(new Color(200, 50, 50));
            } else {
                try {

                    Vacation vacation = new Vacation(user, beginDate.getDate(), endDate.getDate());
                    HibernateUtil.transact(session -> session.save(vacation));


                    statusLabel.setText("Request successfully sent.");
                    statusLabel.setForeground(new Color(50, 199, 50));
                    beginDate.setEnabled(false);
                    endDate.setEnabled(false);
                    submitButton.setText("Exit");
                    submitButton.removeActionListener(this);
                    submitButton.addActionListener(action -> dispose());


                } catch (Exception e) {
                    e.printStackTrace();
                    statusLabel.setText("Unexpected error!");
                    statusLabel.setForeground(new Color(200, 50, 50));
                }
            }

        }
    }
}
