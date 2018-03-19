package main.ems.bean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.jsf.FacesContextUtils;

import main.ems.model.Taxes;
import main.ems.model.User;
import main.ems.service.EmployeeService;
import main.ems.service.TaxesService;
import main.ems.util.AddMessage;

@ManagedBean(name = "taxesBean")
@ViewScoped
public class TaxesBean {

    @Autowired
    TaxesService taxesService;

    @Autowired
    EmployeeService employeeService;

    List<Taxes> taxes;
    List<User> employees;
    User selectedEmployee;
    Taxes selectedFee;
    Taxes selectedFeeFromEmp;
    Taxes fee;
    int flag;

    @PostConstruct
    private void init() {
        FacesContextUtils.getRequiredWebApplicationContext(FacesContext.getCurrentInstance()).getAutowireCapableBeanFactory().autowireBean(this);
        fee = new Taxes();
    }

    public String addFee() {
        try {
            if (fee.getName() == null || fee.getName().isEmpty() || fee.getValue() == null) {
                new AddMessage().addError("Name and value are required!");
                return null;
            }
            taxesService.addOrUpdateFee(fee);
            new AddMessage().addInfo("Taxes has been added succesfuly");
            return "taxes";
        } catch (Exception ex) {
            ex.printStackTrace();
            new AddMessage().addError("Error.Please try again later");
            return null;
        }
    }

    public String updateFee() {
        try {
            taxesService.addOrUpdateFee(fee);
            new AddMessage().addInfo("Succes");
            return "taxes";
        } catch (Exception ex) {
            ex.printStackTrace();
            new AddMessage().addError("Error! Please try again later!");
            return null;
        }
    }

    public void deleteFee(Taxes fee) {
        try {
            taxesService.deleteFee(fee);
            new AddMessage().addInfo("Fee has been deleted");
        } catch (Exception ex) {
            ex.printStackTrace();
            new AddMessage().addError("Error. Please, try again later");
        }
    }

    public String toAdd() {
        return "fee";
    }

    public void toUpdate(int id) {

        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        try {
            response.sendRedirect("fee.xhtml?faces-redirect=true&id=" + id);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String goBack() {
        return "taxes";
    }

    public void initFeeFromParams() {
        Map<String, String> parameters = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String feeId = parameters.get("id");
        if (feeId != null) {
            flag = 1;
            fee = taxesService.getFeeById(Integer.parseInt(feeId));
        }
    }

    public void updateTaxesForEmployee() {

        selectedEmployee.getTaxes().add(selectedFee);
        try {
            employeeService.editEmployee(selectedEmployee);
            selectedFee = null;
            new AddMessage().addInfo("Succes");
        } catch (Exception ex) {
            ex.printStackTrace();
            new AddMessage().addInfo("Error");
        }
    }

    public void deleteEmployeeTaxes() {
        Iterator<Taxes> iterator = selectedEmployee.getTaxes().iterator();

        while (iterator.hasNext()) {
            if (iterator.next().getId() == selectedFeeFromEmp.getId()) {
                iterator.remove();
                continue;
            }
        }

        try {
            employeeService.editEmployee(selectedEmployee);
            selectedFeeFromEmp = null;
            new AddMessage().addInfo("Succes");
        } catch (Exception ex) {
            ex.printStackTrace();
            new AddMessage().addInfo("Error");
        }
    }

    public void onEmployeeChange() {
        selectedFee = null;
        selectedFeeFromEmp = null;
    }

    public List<Taxes> getTaxes() {
        if (selectedEmployee != null) {
            return taxesService.getAllTaxes(selectedEmployee);
        } else {
            return taxesService.getAllTaxes();
        }
    }

    public void setTaxes(List<Taxes> taxes) {
        this.taxes = taxes;
    }

    public List<User> getEmployees() {
        return employeeService.getEmployees();
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

    public Taxes getFee() {
        return fee;
    }

    public void setFee(Taxes fee) {
        this.fee = fee;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public Taxes getSelectedFee() {
        return selectedFee;
    }

    public void setSelectedFee(Taxes selectedFee) {
        this.selectedFee = selectedFee;
    }

    public Taxes getSelectedFeeFromEmp() {
        return selectedFeeFromEmp;
    }

    public void setSelectedFeeFromEmp(Taxes selectedFeeFromEmp) {
        this.selectedFeeFromEmp = selectedFeeFromEmp;
    }

}