package gui;

import entity.Department;
import org.jdesktop.swingx.JXTable;
import storage.UserStorage;
import util.Container;
import util.HibernateUtil;
import util.ToTableArray;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.ArrayList;


//TODO: Create an abstract table class.
public class DepartmentManagementForm extends JFrame {
    private JPanel rootPanel;
    private JButton addButton;
    private JButton deleteButton;
    private JTextField nameField;
    private JTextArea textArea;
    private JButton submitButton;
    private JXTable table1;
    private JButton editButton;
    private JPanel buttons;
    private ArrayList<Department> departments;
    private Department toEdit;

    public DepartmentManagementForm() {

        setContentPane(rootPanel);
        setTitle("Departments - EmpMan");
        pack();

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dimension.width / 2 - this.getSize().width / 2, dimension.height / 2 - this.getSize().height / 2);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        table1.setCellSelectionEnabled(true);
        table1.setColumnSelectionAllowed(false);
        table1.setRowSelectionAllowed(true);
        table1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        addButton.addActionListener(actionEvent -> {
            Department newDep = new Department();
            newDep.setName("<Edit Name>");
            HibernateUtil.transact(session -> session.save(newDep));
            updateTable();
        });

        deleteButton.addActionListener(actionEvent -> {
            Department toDelete = getSelected();
            if (toDelete != null) {
                if (UserStorage.getAll(toDelete).size() > 0) {
                    JOptionPane.showMessageDialog(this,
                            "This department cannot be\n" +
                                    "deleted because there are\n" +
                                    "still some users assigned to it.",
                            "Deletion error",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    HibernateUtil.transact(session -> session.delete(toDelete));
                    updateTable();
                }

            }

        });

        editButton.addActionListener(actionEvent -> {
            toEdit = getSelected();
            if (toEdit != null) {
                nameField.setText(toEdit.getName());
                textArea.setText(toEdit.getDescription());
                nameField.setEnabled(true);
                textArea.setEnabled(true);
                submitButton.setEnabled(true);
            }
            updateTable();
        });

        submitButton.addActionListener(actionEvent -> {
            if (nameField.getText().equals("")) {
                JOptionPane.showMessageDialog(this,
                        "Name cannot be empty.",
                        "Renaming error",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                toEdit.setName(nameField.getText());
                toEdit.setDescription(textArea.getText());
                HibernateUtil.transact(session -> session.update(toEdit));
                nameField.setEnabled(false);
                textArea.setEnabled(false);
                submitButton.setEnabled(false);
                updateTable();
            }
        });
    }


    private void updateTable() {
        Container uc = new Container();
        HibernateUtil.transact(session -> uc.set(session.createQuery("from Department").list()));

        MyTableModel model = (MyTableModel) table1.getModel();
        departments = new ArrayList<>((java.util.List<Department>) uc.get());
        model.setData(ToTableArray.convertDepartments(departments));
        model.fireTableDataChanged(); // notifies the JTable that the model has changed
    }

    private void createUIComponents() {
        int[] widths = {30, 220};

        String[] columnNames = {
                "ID",
                "Name",
        };
        Object[][] emptyInfo = {{"", "", "", "", "", "", ""}};
        table1 = new JXTable(new MyTableModel(emptyInfo, columnNames));
        updateTable();

        for (int i = 0; i < 2; i++) {
            TableColumn column = table1.getColumnModel().getColumn(i);
            column.setPreferredWidth(widths[i]);
        }
    }

    private Department getSelected() {
        Department selected = null;
        try {
            long selectedID = Long.parseLong(table1.getStringAt(table1.getSelectedRow(), 0));
            for (Department dep : departments)
                if (dep.getId() == selectedID) selected = dep;
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

        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        /*
         * Don't need to implement this method unless your table's
         * editable.
         * NOTE: Some why, this time it is editable by default.
         * NOTE: Oh. It was IntelliJ option in form constructor.
         */
        public boolean isCellEditable(int row, int col) {
            return false;
        }

        public void setValueAt(Object value, int row, int col) {
            data[row][col] = value;
            fireTableCellUpdated(row, col);
        }


    }
}
