package entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 50)
    private String name;

    @Column(length = 50, nullable = false, unique = true)
    private String login;

    @Column(nullable = false)
    private String password;

    @Column(length = 50)
    private String schedule;


    @Column(precision = 3)
    private float wage;

    @Enumerated(EnumType.STRING)
    @Column(length = 8, nullable = false)
    private Role role;

    @ManyToOne
    private Department department;



    public User() {
    }

    public User(long id, String name, String login, String password, String schedule, float wage, Role role, Department department) {
        this.id = id;
        this.name = name;
        this.login = login;
        this.password = password;
        this.schedule = schedule;
        this.wage = wage;
        this.role = role;
        this.department = department;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public float getWage() {
        return wage;
    }

    public void setWage(float wage) {
        this.wage = wage;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login.trim().toLowerCase();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getId() == user.getId() &&
                Float.compare(user.getWage(), getWage()) == 0 &&
                Objects.equals(getName(), user.getName()) &&
                Objects.equals(getPassword(), user.getPassword()) &&
                Objects.equals(getSchedule(), user.getSchedule()) &&
                Objects.equals(getRole(), user.getRole()) &&
                Objects.equals(getDepartment(), user.getDepartment());
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", schedule='" + schedule + '\'' +
                ", wage=" + wage +
                ", role=" + role +
                ", department=" + department +
                '}';
    }
}
