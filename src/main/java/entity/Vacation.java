package entity;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "vacations")
public class Vacation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private User user;

    @Column(name = "date_start", nullable = false)
    @Temporal(value=TemporalType.DATE)
    private Date dateStart;

    @Column(name = "date_end", nullable = false)
    @Temporal(value=TemporalType.DATE)
    private Date dateEnd;

    @Column(name = "approved", nullable = false)
    private boolean approved;

    public Vacation() {}

    public Vacation(long id, User user, Date dateStart, Date dateEnd, boolean approved) {
        this.id = id;
        this.user = user;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.approved = approved;
    }

    public Vacation(User user, Date dateStart, Date dateEnd) {
        this.user = user;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.approved = false;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vacation)) return false;
        Vacation vacation = (Vacation) o;
        return getId() == vacation.getId() &&
                Objects.equals(getUser(), vacation.getUser()) &&
                Objects.equals(getDateStart(), vacation.getDateStart()) &&
                Objects.equals(getDateEnd(), vacation.getDateEnd());
    }

    @Override
    public String toString() {
        return "Vacation{" +
                "id=" + id +
                ", user=" + user +
                ", dateStart=" + dateStart +
                ", dateEnd=" + dateEnd +
                '}';
    }
}
