package main.ems.model;

import org.hibernate.annotations.GenericGenerator;

import java.util.Date;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "Users")
public class User {
    int userId;
    String username;
    String password;
    String firstName;
    String lastName;
    String email;
    Role role;
    Department department;
    Job job;
    User managerId;
    Date hireDate;
    String phoneNumber;
    String address;
    String county;
    String city;
    int hdCurrentYear;
    int hdLastYear;
    int hdReceivedCurrentYear;
    double salary;
    String avatarName;
    byte[] avatarContent;
    Set<Taxes> taxes;

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
    @Column(name = "user_id")
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "first_name")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "last_name")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @ManyToOne
    @JoinColumn(name = "role_id")
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @ManyToOne
    @JoinColumn(name = "department_id")
    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    @ManyToOne
    @JoinColumn(name = "job_id")
    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    @ManyToOne
    @JoinColumn(name = "manager_id")
    public User getManagerId() {
        return managerId;
    }

    public void setManagerId(User managerId) {
        this.managerId = managerId;
    }

    @Column(name = "hire_date")
    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }

    @Column(name = "phone")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Column(name = "address")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Column(name = "county")
    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    @Column(name = "city")
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Column(name = "hd_current_year")
    public int getHdCurrentYear() {
        return hdCurrentYear;
    }

    public void setHdCurrentYear(int hdCurrentYear) {
        this.hdCurrentYear = hdCurrentYear;
    }

    @Column(name = "hd_last_year")
    public int getHdLastYear() {
        return hdLastYear;
    }

    public void setHdLastYear(int hdLastYear) {
        this.hdLastYear = hdLastYear;
    }

    @Column(name = "hd_received_current_year")
    public int getHdReceivedCurrentYear() {
        return hdReceivedCurrentYear;
    }

    public void setHdReceivedCurrentYear(int hdReceivedCurrentYear) {
        this.hdReceivedCurrentYear = hdReceivedCurrentYear;
    }

    @Column(name = "salary")
    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    @Column(name = "avatar_name")
    public String getAvatarName() {
        return avatarName;
    }

    public void setAvatarName(String avatarName) {
        this.avatarName = avatarName;
    }

    @Column(name = "avatar_content")
    public byte[] getAvatarContent() {
        return avatarContent;
    }

    public void setAvatarContent(byte[] avatarContent) {
        this.avatarContent = avatarContent;
    }

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "Users_Taxes",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "fee_id")
    )
    public Set<Taxes> getTaxes() {
        return taxes;
    }

    public void setTaxes(Set<Taxes> taxes) {
        this.taxes = taxes;
    }

    @Override
    public String toString() {
        return lastName + " " + firstName;
    }

    @Override
    public boolean equals(Object other) {
        return (other instanceof User) ? userId == ((User) other).userId : (other == this);
    }

    @Override
    public int hashCode() {
        return (this.getClass().hashCode() + userId);

    }
}