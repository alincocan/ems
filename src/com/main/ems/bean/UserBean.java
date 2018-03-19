package main.ems.bean;

import java.io.IOException;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import main.ems.model.User;
import main.ems.service.UserService;

@Controller
@ManagedBean(name = "userBean")
@Scope("session")
public class UserBean {

    private static Logger logger = Logger.getLogger(UserBean.class);

    @Autowired
    UserService userService;
    public User user;
    String username;
    String password;

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return userService.getUserByUsernameAndPassword(username, password);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String doLogin() {
        user = userService.getUserByUsernameAndPassword(username, password);
        if (user == null) {
            addMessage("Incorrect username or password. Please try again");
            logger.info("User not null. Login failed");
            return "login?faces-redirect=true";
        } else {
            logger.info("User is not null");

        }
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();

        RequestDispatcher dispatcher = ((ServletRequest) context.getRequest())
                .getRequestDispatcher("/j_spring_security_check");
        try {
            dispatcher.forward((ServletRequest) context.getRequest(), (ServletResponse) context.getResponse());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Error at login", e);
        }
        FacesContext.getCurrentInstance().responseComplete();
        logger.info("Login succesfuly");
        return null;
    }

    public String doLogout() throws IOException, ServletException {
        ExternalContext context = FacesContext.
                getCurrentInstance().getExternalContext();
        RequestDispatcher dispatcher = ((ServletRequest)
                context.getRequest()).getRequestDispatcher
                ("/j_spring_security_logout");
        dispatcher.forward((ServletRequest)
                context.getRequest(), (ServletResponse)
                context.getResponse());
        FacesContext.getCurrentInstance().responseComplete();

        return null;
    }

    public void addMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, null);
        FacesContext.getCurrentInstance().addMessage(null, message);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
    }

    public Boolean isManager() {
        return userService.isManager(user.getUserId());
    }

    public Boolean isHrUser() {
        return userService.isHrUser(user.getUserId());
    }
}
