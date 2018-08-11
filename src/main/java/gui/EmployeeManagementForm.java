package gui;

import entity.Role;
import entity.User;
import entity.Vacation;
import org.jdesktop.swingx.JXTable;
import storage.UserStorage;
import util.Container;
import util.HibernateUtil;
import util.ToTableArray;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

//TODO: Create an abstract table class.
public class EmployeeManagementForm extends JFrame {
    private JPanel rootPanel;
    private JPanel buttonPanel;
    private JButton newButton;
    private JButton editButton;
    private JButton deleteButton;
    private JXTable table1;
    private JButton updateTableButton;
    private User user;
    private ArrayList<User> displayedUsers;


    public EmployeeManagementForm(User user) {
        this.user = user; //User that opened form


        setContentPane(rootPanel);
        setTitle("Employee Management Form - EmpMan");
        pack();

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dimension.width / 2 - this.getSize().width / 2, dimension.height / 2 - this.getSize().height / 2);
        setResizable(true);
        setMinimumSize(new Dimension(400, 300));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        table1.setCellSelectionEnabled(true);
        table1.setColumnSelectionAllowed(false);
        table1.setRowSelectionAllowed(true);
        table1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        if (user.getRole() != Role.HEAD) {
            newButton.setEnabled(false);
            editButton.setEnabled(false);
            deleteButton.setEnabled(false);
        }

        newButton.addActionListener(actionEvent -> new UserEditForm(null).setVisible(true));

        deleteButton.addActionListener(actionEvent -> {
            User userToDelete = getSelectedUser();
            if (userToDelete != null) {
                if (userToDelete.getId() != user.getId()) {
                    int n = JOptionPane.showConfirmDialog(
                            this,
                            "Are you sure about deleting the selected item?",
                            "Confirm deletion",
                            JOptionPane.YES_NO_OPTION);
                    if (n == 0) {
                        Container uc = new Container();
                        HibernateUtil.transact(session -> uc.set(session.createQuery("from Vacation where user=:user")
                                .setParameter("user", user)
                                .list()));
                        List<Vacation> usersVacations = (List<Vacation>) uc.get();
                        if (usersVacations.size() == 0) {
                            HibernateUtil.transact(session -> session.delete(userToDelete));
                        } else {
                            JOptionPane.showMessageDialog(this,
                                    "You cannot delete this user\n" +
                                            "from the database. There are\n" +
                                            "still some vacations with this\n" +
                                            "user. Delete them first.",
                                    "Deletion error",
                                    JOptionPane.WARNING_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(this,
                            "You cannot delete yourself\n" +
                                    "from the database.",
                            "Deletion error",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        updateTableButton.addActionListener(actionEvent -> updateTable());

        editButton.addActionListener(actionEvent -> new UserEditForm(getSelectedUser()).setVisible(true));
    }

    private void updateTable() {
        switch (user.getRole()) {
            case MANAGER:
                showTable(displayedUsers = new ArrayList<>(UserStorage.getAll(user.getDepartment())));
                break;
            case HEAD:
                showTable(displayedUsers = new ArrayList<>(UserStorage.getAll()));
                break;
        }
    }

    private User getSelectedUser() {
        User userToEdit = null;
        try {
            long selectedID = Long.parseLong(table1.getStringAt(table1.getSelectedRow(), 0));
            for (User oneUser : displayedUsers)
                if (oneUser.getId() == selectedID) {
                    userToEdit = oneUser;
                }

            if (userToEdit == null) {
                throw new IndexOutOfBoundsException();
            }
        } catch (IndexOutOfBoundsException e) {

            JOptionPane.showMessageDialog(this,
                    "Couldn't get selected ID.\n" +
                            "Index is out of bounds.",
                    "Table error",
                    JOptionPane.WARNING_MESSAGE);
        }
        return userToEdit;
    }


    private void showTable(ArrayList<User> usersToShow) {
        MyTableModel model = (MyTableModel) table1.getModel();
        model.setData(ToTableArray.convertUsers(usersToShow));
        model.fireTableDataChanged(); // notifies the JTable that the model has changed
    }

    private void createUIComponents() {

        int[] widths = {50, 100, 135, 70, 350, 110, 75};
        /*int sum = 0;
        for (int wid:widths) sum += wid;
        System.out.println(sum);*/

        String[] columnNames = {
                "ID",
                "Username",
                "Name",
                "Wage",
                "Schedule",
                "Department",
                "Role"
        };
        Object[][] emptyInfo = {{"", "", "", "", "", "", ""}};
        table1 = new JXTable(new MyTableModel(emptyInfo, columnNames));
        updateTable();

        for (int i = 0; i < 7; i++) {
            TableColumn column = table1.getColumnModel().getColumn(i);
            column.setPreferredWidth(widths[i]);
        }
    }


    private class MyTableModel extends AbstractTableModel {
        private String[] columnNames;

        private Object[][] data;

        public MyTableModel(Object[][] data, String[] columnNames) {
            this.columnNames = columnNames;
            this.data = data;
        }

        public MyTableModel() {
        }

        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            return data.length;
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            return data[row][col];
        }

        public void setData(Object[][] data) {
            this.data = data;
        }

        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        /*
         * Don't need to implement this method unless your table's
         * editable.
         * NOTE: Some why, this time it is editable by default.
         */

        public boolean isCellEditable(int row, int col) {
            return false;
        }

        /*
         * Don't need to implement this method unless your table's
         * data can change.
         */
        public void setValueAt(Object value, int row, int col) {
            data[row][col] = value;
            fireTableCellUpdated(row, col);
        }


    }
}
