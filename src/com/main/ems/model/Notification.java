package main.ems.model;

import org.hibernate.annotations.GenericGenerator;

import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "Notifications")
public class Notification {

    public final static String NOTIF_FOR_SUBSTITUTE_WHEN_A_REQUEST_IS_ADDED = "notification.notifForSubstituteWhenARequestIsAdded";
    public final static String NOTIF_FOR_MANAGER_WHEN_A_REQUEST_IS_APPROVED_BY_SUBSTITUTE = "notification.notifForManagerWhenARequestIsApprovedBySubstitute";
    public final static String NOTIF_WHEN_A_REQUEST_IS_CANCELED = "notification.notifWhenARequestIsCanceled";
    public final static String NOTIF_FOR_EMPLOYEE_WHEN_A_REQUEST_IS_APPROVED = "notification.notifForEmployeeWhenARequestIsApproved";
    public final static String NOTIF_FOR_EMPLOYEE_WHEN_A_REQUEST_IS_REJECTED = "notification.notifForEmployeeWhenARequestIsRejected";
    public final static String NOTIF_FOR_MANAGER_WHEN_A_REQUEST_IS_ADDED = "notification.notifForManagerWhenARequestIsAdded";
    public final static String NOTIF_FOR_MANAGER_TO_APPROVE_BONUS = "notification.notifForManagerToApproveBonus";
    public final static String NOTIF_FOR_PROPONENT_WHEN_BONUS_IS_APPROVED = "notification.notifForProponentWhenBonusIsApproved";
    public final static String NOTIF_FOR_PROPONENT_WHEN_BONUS_IS_COMPLETELY_APPROVED = "notification.notifForProponentWhenBonusIsCompletelyApproved";
    public final static String NOTIF_FOR_EMPLOYEE_WHEN_BONUS_IS_COMPLETELY_APPROVED = "notification.notifForEmployeeWhenBonusIsCompletelyAproved";

    public int id;
    public User toEmp;
    public User fromEmp;
    public String message;
    public String seen;
    public Date notifDate;
    public String link;

    public Notification() {
    }

    public Notification(User toEmp, User fromEmp, String message, String seen, Date notifDate, String link) {
        this.toEmp = toEmp;
        this.fromEmp = fromEmp;
        this.message = message;
        this.seen = seen;
        this.notifDate = notifDate;
        this.link = link;
    }

    @Id
    @GenericGenerator(
            name = "sequence",
            strategy = "sequence",
            parameters = {
                    @org.hibernate.annotations.Parameter(
                            name = "sequence",
                            value = "sequence"
                    )

            })
    @GeneratedValue(generator = "sequence")
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "to_emp")
    public User getToEmp() {
        return toEmp;
    }

    public void setToEmp(User toEmp) {
        this.toEmp = toEmp;
    }

    @ManyToOne
    @JoinColumn(name = "from_emp")
    public User getFromEmp() {
        return fromEmp;
    }

    public void setFromEmp(User fromEmp) {
        this.fromEmp = fromEmp;
    }

    @Column(name = "message")
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Column(name = "seen")
    public String getSeen() {
        return seen;
    }

    public void setSeen(String seen) {
        this.seen = seen;
    }

    @Column(name = "notif_date")
    public Date getNotifDate() {
        return notifDate;
    }

    public void setNotifDate(Date notifDate) {
        this.notifDate = notifDate;
    }

    @Column(name = "link")
    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}