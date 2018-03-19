package main.ems.util;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import main.ems.bean.UserBean;

@ManagedBean
public class UserBeanInstance {

    public static UserBean getUserBean() {
        FacesContext fctx = FacesContext.getCurrentInstance();
        UserBean userBean = (UserBean) fctx.getApplication().evaluateExpressionGet(fctx, "#{userBean}", UserBean.class);
        System.out.println(userBean.getUser());
        return userBean;
    }
}
