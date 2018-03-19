package main.ems.bean;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.jsf.FacesContextUtils;

import main.ems.model.Salaries;
import main.ems.model.Taxes;
import main.ems.model.User;
import main.ems.service.EmployeeService;
import main.ems.service.PaySalariesService;
import main.ems.util.UserBeanInstance;

@ManagedBean(name = "paySalariesBean")
@ViewScoped
public class PaySalariesBean {
    List<User> employees;
    List<Taxes> taxes;
    List<Date> salariesDistinctByDate;
    List<Salaries> salariesByDate;
    List<Salaries> myPaidSalaries;
    @Autowired
    EmployeeService employeeService;

    @Autowired
    PaySalariesService paySalariesService;

    @PostConstruct
    private void init() {
        FacesContextUtils
                .getRequiredWebApplicationContext(FacesContext.getCurrentInstance())
                .getAutowireCapableBeanFactory().autowireBean(this);
    }

    public List<Salaries> salariesByDate(Date date) {
        return paySalariesService.getSalariesByDate(date);
    }

    public String formatDate(Date date) {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        return df.format(date);

    }

    public List<User> getEmployees() {
        return employeeService.getEmployees();
    }

    public void setEmployees(List<User> employees) {
        this.employees = employees;
    }

    public List<Taxes> getTaxes() {
        return taxes;
    }

    public void setTaxes(List<Taxes> taxes) {
        this.taxes = taxes;
    }

    public List<Date> getSalariesDistinctByDate() {
        return paySalariesService.getDistinctDatesForPaidSalaries();
    }

    public void setSalariesDistinctByDate(List<Date> salariesDistinctByDate) {
        this.salariesDistinctByDate = salariesDistinctByDate;
    }

    public List<Salaries> getSalariesByDate() {
        return salariesByDate;
    }

    public void setSalariesByDate(List<Salaries> salariesByDate) {
        this.salariesByDate = salariesByDate;
    }

    public List<Salaries> getMyPaidSalaries() {
        return paySalariesService.getMyPaidSalaries(UserBeanInstance.getUserBean().getUser().getUserId());
    }

    public void setMyPaidSalaries(List<Salaries> myPaidSalaries) {
        this.myPaidSalaries = myPaidSalaries;
    }

}
