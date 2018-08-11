package storage;

import entity.Department;
import entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import util.Container;
import util.HibernateUtil;

import java.util.List;

//TODO: remove this mostly useless class.
//TODO: create half-container & half-"transact" class that will do all the same, but without container.
public class UserStorage {

    public static User getUser(String login) { //only used in log in form
        Container uc = new Container();
        HibernateUtil.transact(session -> uc.set(session
                .createQuery("from User where login=:login")
                .setParameter("login",login)
                .uniqueResult()));
        return (User) uc.get();
    }

    public static List<User> getAll() { //used ONCE
        Container uc = new Container();
        HibernateUtil.transact(session -> uc.set(session.createQuery("from User").list()));
        return (List<User>) uc.get();
    }

    public static List<User> getAll(Department dep) {
        Container uc = new Container();
        HibernateUtil.transact(session -> uc.set(session.createQuery("from User where department=:department")
                .setParameter("department",dep)
                .list()));
        return (List<User>) uc.get();
    }

}


