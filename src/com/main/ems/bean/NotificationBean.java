package main.ems.bean;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import main.ems.model.Notification;
import main.ems.service.NotificationService;

@Controller
@ManagedBean(name = "notificationBean")
@Scope("session")
public class NotificationBean {
    @Autowired
    NotificationService notificationService;

    public List<Notification> notifications;
    public int notificationSize;

    public UserBean getUserBean() {
        FacesContext fctx = FacesContext.getCurrentInstance();
        UserBean userBean = (UserBean) fctx.getApplication().evaluateExpressionGet(fctx, "#{userBean}", UserBean.class);
        System.out.println(userBean.getUser());
        return userBean;
    }

    @PostConstruct
    public void init() {
        System.out.println("dasdsadsa");
    }

    public String goToPage(String link) {
        notificationService.updateNotification(getUserBean().getUser().getUserId(), link);
        if (link.contains("?faces-redirect=true&id=")) {
            HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            try {
                response.sendRedirect(link);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            return link;
        }
        return null;
    }


    public NotificationService getNotificationService() {
        return notificationService;
    }

    public void setNotificationService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public List<Notification> getNotifications() {
        return notificationService.getNotifications(getUserBean().getUser().getUserId());
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    public int getNotificationSize() {

        return notificationService.getNotifications(getUserBean().getUser().getUserId()).size();

    }

    public void setNotificationSize(int notificationSize) {
        this.notificationSize = notificationSize;
    }
}