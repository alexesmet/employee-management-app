import entity.Role;
import entity.User;
import util.HibernateUtil;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Runnable init = () -> HibernateUtil.transact(session -> {
            List from_user = session.createQuery("FROM User").list();
            if (from_user.size() < 1) {
                User newUser = new User();
                newUser.setRole(Role.HEAD);
                newUser.setLogin("root");
                newUser.setPassword("root");
                newUser.setSchedule("Delete this user as soon as you finish setting up.");
                session.save(newUser);
            }
        });
        init.run();
        new gui.LogInForm().setVisible(true);
    }
}