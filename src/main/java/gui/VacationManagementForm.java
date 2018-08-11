package gui;

import entity.Role;
import entity.User;
import entity.Vacation;
import org.jdesktop.swingx.JXTable;
import util.Container;
import util.HibernateUtil;
import util.ToTableArray;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//TODO: Create an abstract table class.
public class VacationManagementForm extends JFrame {
    private JPanel rootPanel;
    private JButton approveButton;
    private JButton disapproveButton;
    private JButton deleteButton;
    private JButton updateTableButton;
    private JXTable table1;
    private ArrayList<Vacation> vacations;
    private final User user;

    public VacationManagementForm(User user) {
        this.user = user; //User that opened form


        setContentPane(rootPanel);
        setTitle("Vacation Management Form - EmpMan");
        pack();

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dimension.width / 2 - this.getSize().width / 2, dimension.height / 2 - this.getSize().height / 2);
        setResizable(true);
        setMinimumSize(new Dimension(450, 300));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        table1.setCellSelectionEnabled(true);
        table1.setColumnSelectionAllowed(false);
        table1.setRowSelectionAllowed(true);
        table1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        updateTableButton.addActionListener(actionEvent -> {
            updateVacations();
            updateTable(vacations);
        });
        approveButton.addActionListener(actionEvent -> {
            vacationSetApprovement(true);
            updateTable(vacations);
        });
        disapproveButton.addActionListener(actionEvent -> {
            vacationSetApprovement(false);
            updateTable(vacations);
        });
        deleteButton.addActionListener(actionEvent -> {
            int n = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure about deleting the selected item?",
                    "Confirm deletion",
                    JOptionPane.YES_NO_OPTION);
            if (n == 0) {
                Vacation toDelete = getSelected();
                if (toDelete != null) {
                    HibernateUtil.transact(session -> session.delete(toDelete));
                    vacations.remove(toDelete);
                    updateTable(vacations);
                }
            }
        });

        if (user.getRole() == Role.EMPLOYEE) {
            approveButton.setEnabled(false);
            disapproveButton.setEnabled(false);
        }
    }

    private void vacationSetApprovement(boolean approve) {
        Vacation toApprove = getSelected();
        if (toApprove != null) {
            toApprove.setApproved(approve);
            HibernateUtil.transact(session -> session.update(toApprove));
        }

    }

    //   I'm sorry i've got almost same methods in each table form.
    // I am working with swing for the first time, i was creating
    // these forms one by one.
    private Vacation getSelected() {
        Vacation selected = null;
        try {
            long selectedID = Long.parseLong(table1.getStringAt(table1.getSelectedRow(), 0));
            for (Vacation oneVac : vacations)
                if (oneVac.getId() == selectedID) {
                    selected = oneVac;
                }

            if (selected == null) {
                throw new IndexOutOfBoundsException();
            }
        } catch (IndexOutOfBoundsException e) {
            JOptionPane.showMessageDialog(this,
                    "Couldn't get selected ID.\n" +
                            "Index is out of bounds.",
                    "Table error",
                    JOptionPane.WARNING_MESSAGE);
        }
        return selected;
    }

    private void createUIComponents() {
        updateVacations();
        String[] columns = {"ID", "Username", "Name", "Start", "End", "app."};
        table1 = new JXTable(new MyTableModel(ToTableArray.convertVacations(vacations), columns));

        int[] widths = {50, 120, 200, 80, 80, 30};
        /*int sum = 0;
        for (int wid:widths) sum += wid;
        System.out.println(sum);*/

        for (int i = 0; i < 6; i++) {
            TableColumn column = table1.getColumnModel().getColumn(i);
            column.setPreferredWidth(widths[i]);
        }
    }

    private void updateTable(ArrayList<Vacation> vacations) {
        MyTableModel model = (MyTableModel) table1.getModel();
        model.setData(ToTableArray.convertVacations(vacations));
        model.fireTableDataChanged(); // notifies the JTable that the model has changed
    }

    private void updateVacations() {
        switch (user.getRole()) {
            case EMPLOYEE:
                Container c = new Container();
                HibernateUtil.transact(session -> c.set(session
                        .createQuery("from Vacation where user=:user")
                        .setParameter("user", user)
                        .list()));
                vacations = new ArrayList<>((List<Vacation>) c.get());
                break;
            case MANAGER:
                Container c1 = new Container();
                HibernateUtil.transact(session -> c1.set(session
                        .createQuery("from Vacation") //TODO: Make a proper Query
                        .list()));
                List<Vacation> all = (List<Vacation>) c1.get();
                vacations = new ArrayList<>(
                        all.stream()
                                .filter(u -> u.getUser().getDepartment().getId() == user.getDepartment().getId())
                                .collect(Collectors.toList()));
                break;
            case HEAD:
                Container c2 = new Container();
                HibernateUtil.transact(session -> c2.set(session
                        .createQuery("from Vacation")
                        .list()));
                List<Vacation> all1 = (List<Vacation>) c2.get();
                vacations = new ArrayList<>(all1);
                break;
        }
    }

    private class MyTableModel extends AbstractTableModel {
        private String[] columnNames;
        private Object[][] data;

        public MyTableModel(Object[][] data, String[] columnNames) {
            this.columnNames = columnNames;
            this.data = data;
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

        @Override
        public Class getColumnClass(int column) {
            switch (column) {
                case 0:
                    return Long.class;
                case 5:
                    return Boolean.class;
                default:
                    return String.class;
            }
        }
    }
}
