package util;

import entity.Department;
import entity.User;
import entity.Vacation;

import java.util.List;

//TODO: Remove this class. Make each entity "Tableble" & implement "toTable" method
public class ToTableArray {
    public static Object[][] convertUsers(List<User> list) {
        Object[][] arr = new Object[list.size()][7];
        //System.out.println(list.toString());
        for (int i = 0; i < list.size(); i++) {
            User user = list.get(i);
            arr[i][0] = new Long(user.getId());
            arr[i][1] = user.getLogin();
            arr[i][2] = user.getName();
            arr[i][3] = new Float(user.getWage());
            arr[i][4] = user.getSchedule();
            arr[i][5] = user.getDepartment() != null ? user.getDepartment().getName() : "";
            arr[i][6] = user.getRole().toString();
        }
        return arr;
    }

    public static Object[][] convertDepartments(List<Department> list) {
        Object[][] arr = new Object[list.size()][2];
        for (int i = 0; i < list.size(); i++) {
            Department department = list.get(i);
            arr[i][0] = new Long(department.getId());
            arr[i][1] = department.getName();
        }
        return arr;
    }

    public  static Object[][] convertVacations(List<Vacation> list) {
        Object[][] arr = new Object[list.size()][6];
        for (int i = 0; i < list.size(); i++) {
            Vacation vac = list.get(i);
            arr[i][0] = new Long(vac.getId());
            arr[i][1] = vac.getUser().getLogin();
            arr[i][2] = vac.getUser().getName();
            arr[i][3] = vac.getDateStart();
            arr[i][4] = vac.getDateEnd();
            arr[i][5] = vac.isApproved();
        }
        return arr;
    }
}
