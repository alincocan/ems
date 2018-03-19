package main.ems.model;

import org.hibernate.annotations.GenericGenerator;

import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "Salaries")
public class Salaries {
    int id;
    User user;
    double salary;
    Date salaryDate;

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
    @JoinColumn(name = "user_id")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Column(name = "net_salary")
    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    @Column(name = "salary_date")
    public Date getSalaryDate() {
        return salaryDate;
    }

    public void setSalaryDate(Date salaryDate) {
        this.salaryDate = salaryDate;
    }
}
