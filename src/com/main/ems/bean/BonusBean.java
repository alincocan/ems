package main.ems.bean;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import main.ems.util.AddMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.jsf.FacesContextUtils;

import main.ems.model.Bonus;
import main.ems.model.BonusType;
import main.ems.model.Department;
import main.ems.model.Notification;
import main.ems.model.User;
import main.ems.service.BonusService;
import main.ems.service.NotificationService;

@ManagedBean(name = "bonusBean")
@ViewScoped
public class BonusBean {

    @Autowired
    BonusService bonusService;
    @Autowired
    NotificationService notificationService;
    List<User> employees;
    List<Bonus> bonusForApproval;
    List<Bonus> bonusProposed;
    List<Bonus> myBonus;
    User selectedEmployee;
    boolean money;
    boolean holiday;
    boolean tickets;
    String description;
    double value;

    @PostConstruct
    private void init() {
        FacesContextUtils
                .getRequiredWebApplicationContext(FacesContext.getCurrentInstance())
                .getAutowireCapableBeanFactory().autowireBean(this);
    }

    public UserBean getUserBean() {
        FacesContext fctx = FacesContext.getCurrentInstance();
        UserBean userBean = (UserBean) fctx.getApplication().evaluateExpressionGet(fctx, "#{userBean}", UserBean.class);
        System.out.println(userBean.getUser());
        return userBean;
    }

    public List<User> getEmployees() {
        return bonusService.getEmployees(getUserBean().getUser().getDepartment().getDepartmentId(), getUserBean().getUser().getUserId());
    }

    public void chooseMoney() {
        holiday = false;
        tickets = false;
    }

    public void chooseHoliday() {
        money = false;
        tickets = false;
    }

    public void chooseTickets() {
        holiday = false;
        money = false;
    }

    public void addBonus() {

        try {

            String result = bonusService.addBonus(selectedEmployee,money,holiday,tickets,value,description, getUserBean().getUser());
            if(!result.equalsIgnoreCase("succes")) {
                this.addError(result);
                return;
            }

            clearAddBonus();
            addInfo("Bonus has been added succesfuly");
            if (getUserBean().getUser().getDepartment().getParent() != null) {
                Notification notification = new Notification(getUserBean().getUser().getDepartment().getParent().getManagerId(), getUserBean().getUser(), Notification.NOTIF_FOR_MANAGER_TO_APPROVE_BONUS, "N", new Date(), "approveBonus");
                notificationService.insertNotification(notification);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            addError("An error occured during adding operation. Please try again later!");
        }
    }

    public void clearAddBonus() {
        selectedEmployee = null;
        money = false;
        holiday = false;
        tickets = false;
        value = 0.0;
        description = null;
    }

    public void approveBonus(Bonus bonus) {

        try {
            bonusService.approveBonus(bonus, getUserBean().getUser());
            addInfo("Bonus has been apporoved succesfuly!");
            Notification notification;
            if (getUserBean().getUser().getDepartment().getParent() != null) {
                notification = new Notification(getUserBean().getUser().getDepartment().getParent().getManagerId(), getUserBean().getUser(), Notification.NOTIF_FOR_MANAGER_TO_APPROVE_BONUS, "N", new Date(), "approveBonus");
                notificationService.insertNotification(notification);


            }
            if (getUserBean().getUser().getDepartment().getParent() == null) {
                notification = new Notification(bonus.getProponent(), null, Notification.NOTIF_FOR_PROPONENT_WHEN_BONUS_IS_COMPLETELY_APPROVED, "N", new Date(), "bonus-proposed.xhtml?faces-redirect=true&id=" + bonus.getId());
                notificationService.insertNotification(notification);

                notification = new Notification(bonus.getEmployee(), bonus.getProponent(), Notification.NOTIF_FOR_EMPLOYEE_WHEN_BONUS_IS_COMPLETELY_APPROVED, "N", new Date(), "my-bonuses.xhtml?faces-redirect=true&id=" + bonus.getId());
                notificationService.insertNotification(notification);

                if (bonus.getBonusType().getId() == 2) {
                    User employee = bonus.getEmployee();
                    employee.setHdCurrentYear(employee.getHdCurrentYear() + (int) bonus.getValue());
                    bonusService.updateEmployeeHolidayDays(employee);
                }
            }
            notification = new Notification(bonus.getProponent(), getUserBean().getUser(), Notification.NOTIF_FOR_PROPONENT_WHEN_BONUS_IS_APPROVED, "N", new Date(), "bonus-proposed.xhtml?faces-redirect=true&id=" + bonus.getId());
            notificationService.insertNotification(notification);
        } catch (Exception ex) {
            ex.printStackTrace();
            addError("An error ocurred during approving bonus. Please try again later!");
        }
    }

    public double calculateProgress(User employee, User currentState) {
        if (currentState == null) {
            return 100;
        }
        double total = 0.0;
        double currentProgress = 0.0;
        boolean ok = true;
        Department department = employee.getDepartment().getParent();
        while (department != null) {
            total++;

            if (department.getManagerId().getUserId() == currentState.getUserId()) {
                ok = false;
            }
            if (ok) {
                currentProgress++;
            }
            department = department.getParent();
        }
        if (ok) {
            currentProgress = 0;
        }
        if (total == 0) {
            return 100;
        }

        return currentProgress / total * 100;
    }

    public void addError(String summary) {
       new AddMessage().addError(summary);
    }

    public void addInfo(String summary) {
        new AddMessage().addInfo(summary);
    }

    public void setEmployees(List<User> employees) {
        this.employees = employees;
    }

    public User getSelectedEmployee() {
        return selectedEmployee;
    }

    public void setSelectedEmployee(User selectedEmployee) {
        this.selectedEmployee = selectedEmployee;
    }

    public boolean isMoney() {
        return money;
    }

    public void setMoney(boolean money) {
        this.money = money;
    }

    public boolean isHoliday() {
        return holiday;
    }

    public void setHoliday(boolean holiday) {
        this.holiday = holiday;
    }

    public boolean isTickets() {
        return tickets;
    }

    public void setTickets(boolean tickets) {
        this.tickets = tickets;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public NotificationService getNotificationService() {
        return notificationService;
    }

    public void setNotificationService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public List<Bonus> getBonusForApproval() {
        return bonusService.getBonusForApproval(getUserBean().getUser().getUserId());
    }

    public void setBonusForApproval(List<Bonus> bonusForApproval) {
        this.bonusForApproval = bonusForApproval;
    }

    public List<Bonus> getBonusProposed() {
        Map<String, String> params = FacesContext.getCurrentInstance().
                getExternalContext().getRequestParameterMap();
        String id = params.get("id");
        if (id == null) {
            return bonusService.getBonusProposed(getUserBean().getUser().getUserId());
        } else {
            return bonusService.getBonusById(Integer.parseInt(id));
        }
    }

    public void setBonusProposed(List<Bonus> bonusProposed) {
        this.bonusProposed = bonusProposed;
    }

    public List<Bonus> getMyBonus() {
        Map<String, String> params = FacesContext.getCurrentInstance().
                getExternalContext().getRequestParameterMap();
        String id = params.get("id");
        if (id == null) {
            return bonusService.getMyBonus(getUserBean().getUser().getUserId());
        } else {
            return bonusService.getBonusById(Integer.parseInt(id));
        }
    }

    public void setMyBonus(List<Bonus> myBonus) {
        this.myBonus = myBonus;
    }

}
