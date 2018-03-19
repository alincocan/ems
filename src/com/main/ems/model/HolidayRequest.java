package main.ems.model;

import org.hibernate.annotations.GenericGenerator;

import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "Holiday_requests")
public class HolidayRequest {

    public static final int HOLIDAY = 1;
    public static final int PAID_DAYS_OFF = 2;

    public static final int WAITING = 0;
    public static final int APPROVED_BY_SUBSTITUTE = 1;
    public static final int APPROVED_BY_MANAGER = 2;
    public static final int PENDING = 3;
    public static final int FINISHED = 4;
    public static final int REJECTED_BY_SUBSTITUTE = 5;
    public static final int REJECTED_BY_MANAGER = 6;

    int id;
    Date startDate;
    Date endDate;
    int daysNumber;
    int requestType;
    Date requestDate;
    User substitute;
    User manager;
    User employee;
    int state;
    String substituteMessage;
    String managerMessage;

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

    @Column(name = "start_date")
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Column(name = "end_date")
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Column(name = "days_number")
    public int getDaysNumber() {
        return daysNumber;
    }

    public void setDaysNumber(int daysNumber) {
        this.daysNumber = daysNumber;
    }

    @Column(name = "request_type")
    public int getRequestType() {
        return requestType;
    }

    public void setRequestType(int requestType) {
        this.requestType = requestType;
    }

    @Column(name = "request_date")
    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "substitute_id")
    public User getSubstitute() {
        return substitute;
    }

    public void setSubstitute(User substitute) {
        this.substitute = substitute;
    }

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "manager_id")
    public User getManager() {
        return manager;
    }

    public void setManager(User manager) {
        this.manager = manager;
    }

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "employee_id")
    public User getEmployee() {
        return employee;
    }

    public void setEmployee(User employee) {
        this.employee = employee;
    }

    @Column(name = "state")
    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Column(name = "substitute_message")
    public String getSubstituteMessage() {
        return substituteMessage;
    }

    public void setSubstituteMessage(String substituteMessage) {
        this.substituteMessage = substituteMessage;
    }

    @Column(name = "manager_message")
    public String getManagerMessage() {
        return managerMessage;
    }

    public void setManagerMessage(String managerMessage) {
        this.managerMessage = managerMessage;
    }


}