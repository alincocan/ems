package main.ems.bean;


import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.jsf.FacesContextUtils;

import main.ems.model.HolidayRequest;
import main.ems.model.Notification;
import main.ems.model.User;
import main.ems.service.HolidayRequestService;
import main.ems.service.NotificationService;

@ManagedBean(name = "holidayRequestBean")
@ViewScoped
public class HolidayRequestBean {
    @Autowired
    public HolidayRequestService holidayRequestService;
    @Autowired
    public NotificationService notificationService;

    public HolidayRequest holidayRequest;
    public List<User> substitutes;
    public List<HolidayRequest> ownRequests;
    public List<HolidayRequest> requestsForApproval;
    public List<HolidayRequest> relatedRequests;
    public List<HolidayRequest> history;
    public HolidayRequest selectedRequest;


    @PostConstruct
    private void init() {
        FacesContextUtils
                .getRequiredWebApplicationContext(FacesContext.getCurrentInstance())
                .getAutowireCapableBeanFactory().autowireBean(this);
        holidayRequest = new HolidayRequest();
    }

    public UserBean getUserBean() {
        FacesContext fctx = FacesContext.getCurrentInstance();
        UserBean userBean = (UserBean) fctx.getApplication().evaluateExpressionGet(fctx, "#{userBean}", UserBean.class);
        System.out.println(userBean.getUser());
        return userBean;
    }

    public void addRequest() {
        holidayRequest.setEmployee(getUserBean().getUser());
        holidayRequest.setManager(getUserBean().getUser().getDepartment().getManagerId());
        holidayRequest.setRequestDate(new Date());
        try {
            String msg = holidayRequestService.addRequest(holidayRequest);
            if (msg.equals("Succes")) {

                Notification notification = new Notification(holidayRequest.getSubstitute(), getUserBean().getUser(), Notification.NOTIF_FOR_SUBSTITUTE_WHEN_A_REQUEST_IS_ADDED, "N", new Date(), "approveRequest");
                notificationService.insertNotification(notification);
                notification = new Notification(holidayRequest.getManager(), getUserBean().getUser(), Notification.NOTIF_FOR_MANAGER_WHEN_A_REQUEST_IS_ADDED, "N", new Date(), "approveRequest");
                notificationService.insertNotification(notification);
                addInfo("Cerere adaugata cu succes");

            } else {
                addError(msg);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            addError("A aparut o eroare la adaugarea cererii de concediu");
        }

    }

    public String cancelRequest(HolidayRequest holidayRequest) {
        try {
            holidayRequestService.cancelRequest(holidayRequest);
            Notification notification = new Notification(holidayRequest.getSubstitute(), getUserBean().getUser(), Notification.NOTIF_WHEN_A_REQUEST_IS_CANCELED, "N", new Date(), "nolink");
            notificationService.insertNotification(notification);
            notification = new Notification(holidayRequest.getManager(), getUserBean().getUser(), Notification.NOTIF_WHEN_A_REQUEST_IS_CANCELED, "N", new Date(), "nolink");
            notificationService.insertNotification(notification);
            addInfo("Request canceled");
            return "ownRequest?faces-redirect=true";
        } catch (Exception ex) {
            ex.printStackTrace();
            addError("Error");
            return "ownRequest?faces-redirect=true";
        }
    }

    public String approveRequest(HolidayRequest holidayRequest) {
        int newState = holidayRequest.getState() + 1;
        holidayRequest.setState(newState);
        try {
            holidayRequestService.updateRequest(holidayRequest);
            if (holidayRequest.getSubstitute().getUserId() == getUserBean().getUser().getUserId()) {
                Notification notification = new Notification(holidayRequest.getManager(), getUserBean().getUser(), Notification.NOTIF_FOR_MANAGER_WHEN_A_REQUEST_IS_APPROVED_BY_SUBSTITUTE, "N", new Date(), "approveRequests");
                notificationService.insertNotification(notification);
            }

            Notification notification = new Notification(holidayRequest.getEmployee(), getUserBean().getUser(), Notification.NOTIF_FOR_EMPLOYEE_WHEN_A_REQUEST_IS_APPROVED, "N", new Date(), "ownRequest");
            notificationService.insertNotification(notification);
            addInfo("Request approved");
            return "approveRequest?faces-redirect=true";
        } catch (Exception ex) {
            ex.printStackTrace();
            addError("Error");
            return "approveRequest?faces-redirect=true";
        }
    }

    public String rejectRequest(HolidayRequest holidayRequest) {
        int newState = holidayRequest.getState() + 5;
        holidayRequest.setState(newState);
        try {
            holidayRequestService.updateRequest(holidayRequest);
            Notification notification = new Notification(holidayRequest.getEmployee(), getUserBean().getUser(), Notification.NOTIF_FOR_EMPLOYEE_WHEN_A_REQUEST_IS_REJECTED, "N", new Date(), "ownRequest");
            notificationService.insertNotification(notification);
            addInfo("Request rejecred");
            return "approveRequest?faces-redirect=true";
        } catch (Exception ex) {
            ex.printStackTrace();
            addError("Error");
            return "approveRequest?faces-redirect=true";
        }
    }

    public void addError(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, null);
        FacesContext.getCurrentInstance().addMessage(null, message);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
    }

    public void addInfo(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
        FacesContext.getCurrentInstance().addMessage(null, message);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
    }

    public void cleanAddRequest() {
        holidayRequest = new HolidayRequest();
    }

    public HolidayRequestService getHolidayRequestService() {
        return holidayRequestService;
    }

    public void setHolidayRequestService(HolidayRequestService holidayRequestService) {
        this.holidayRequestService = holidayRequestService;
    }


    public NotificationService getNotificationService() {
        return notificationService;
    }

    public void setNotificationService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public HolidayRequest getHolidayRequest() {
        return holidayRequest;
    }

    public void setHolidayRequest(HolidayRequest holidayRequest) {
        this.holidayRequest = holidayRequest;
    }

    public List<User> getSubstitutes() {
        return holidayRequestService.getSubstitutes(getUserBean().getUser().getDepartment().getDepartmentId(), getUserBean().getUser().getUserId());
    }

    public void setSubstitutes(List<User> substitutes) {
        this.substitutes = substitutes;
    }

    public List<HolidayRequest> getOwnRequests() {
        return holidayRequestService.ownRequests(getUserBean().getUser().getUserId());
    }

    public void setOwnRequests(List<HolidayRequest> ownRequests) {
        this.ownRequests = ownRequests;
    }

    public List<HolidayRequest> getRequestsForApproval() {
        List<HolidayRequest> forApproval = holidayRequestService.getRequestsForApproval(getUserBean().getUser().getUserId());
        List<HolidayRequest> related = holidayRequestService.getRelatedRequests(getUserBean().getUser().getUserId());
        forApproval.addAll(related);
        ;
        return forApproval;
    }

    public Boolean renderButtons(HolidayRequest req) {
        if (req.getManager().getDepartment().getManagerId().getUserId() == getUserBean().getUser().getUserId() && req.getState() == HolidayRequest.APPROVED_BY_SUBSTITUTE)
            return true;
        if (req.getSubstitute().getUserId() == getUserBean().getUser().getUserId() && req.getState() == HolidayRequest.WAITING)
            return true;
        return false;
    }

    public void setRequestsForApproval(List<HolidayRequest> requestsForApproval) {
        this.requestsForApproval = requestsForApproval;
    }

    public List<HolidayRequest> getRelatedRequests() {
        return holidayRequestService.getRelatedRequests(getUserBean().getUser().getUserId());
    }

    public void setRelatedRequests(List<HolidayRequest> relatedRequests) {
        this.relatedRequests = relatedRequests;
    }

    public List<HolidayRequest> getHistory() {
        return holidayRequestService.getHistory(getUserBean().getUser().getUserId());
    }

    public void setHistory(List<HolidayRequest> history) {
        this.history = history;
    }

    public HolidayRequest getSelectedRequest() {
        return selectedRequest;
    }

    public void setSelectedRequest(HolidayRequest selectedRequest) {
        this.selectedRequest = selectedRequest;
    }


}