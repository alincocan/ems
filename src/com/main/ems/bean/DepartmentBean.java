package main.ems.bean;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import main.ems.model.Department;
import main.ems.model.User;
import main.ems.service.DepartmentService;

@Controller
@ManagedBean(name = "departmentBean")
@Scope("session")
public class DepartmentBean {

    @Autowired
    DepartmentService departmentService;
    public String departmentName;
    public Department parent;
    public List<Department> departments;
    public Department departmentSelected;
    public List<User> employees;
    public Boolean editButtonGenerator;

    public String addDepartment() {
        try {
            List<Department> departments = departmentService.getDepartmentByName(departmentName);
            if (departments == null) {
                addError("Eroare la introducerea departamentului cu numele '" + departmentName + "'");
                return null;
            } else {
                if (departments.size() > 0) {
                    addError("Departamentul cu numele '" + departmentName + "' exista deja in sistem!");
                    return null;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            addError("Eroare la introducerea departamentului cu numele '" + departmentName + "'!!");
            return null;
        }

        Department newDepartment = new Department(departmentName, parent);
        try {
            String errMessage = departmentService.addDepartment(newDepartment);


            if (errMessage == null) {
                addInfo("Departamentul '" + departmentName + "' a fost adaugat cu succes!");
                departmentName = "";
                parent = null;
                return "departments";
            } else {
                addError("Eroare la introducerea departamentului cu numele '" + departmentName + "'!!!");
                return null;
            }
        } catch (Exception ex) {
            addError("Eroare la introducerea departamentului cu numele '" + departmentName + "'!!!!");
            ex.printStackTrace();
        }
        return null;
    }

    public void deleteDepartment(ActionEvent actionEvent) {
        try {
            if (departmentService.getEmployeesByDepartment(departmentSelected.getDepartmentId()).size() > 0) {
                addError("Nu puteti sterge un departament care contine angajati!");
            } else {
                if (departmentService.checkIsFather(departmentSelected.getDepartmentId())) {
                    addError("Nu puteti sterge un departament parinte!");
                    return;
                }
                try {
                    departmentService.deleteDepartment(departmentSelected);
                    addInfo("Departamentul " + departmentSelected.getDepartmentName() + " a fost sters cu succes");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    addError("A aparut o eroare la stergerea departamentului. Va rugam incercati mai tarziu!");
                }
            }
        } catch (Exception ex) {
            addError("A aparut o eroare la stergerea departamentului. Va rugam incercati mai tarziu!");
            return;
        }
    }

    public String toEdit() {
        editButtonGenerator = true;
        departmentName = departmentSelected.getDepartmentName();
        parent = departmentSelected.getParent();
        return "department";
    }

    public String editDepartment() {
        String oldDepartmentName = departmentSelected.getDepartmentName();
        try {
            List<Department> departments = departmentService.getDepartmentByName(departmentName);
            if (departments.size() > 0 && !(departments.get(0).getDepartmentName().equals(oldDepartmentName))) {
                addError("Departamentul cu numele '" + departmentName + "' exista deja in sistem!");
                return "/pages/department.xhtml?=faces=true";
            }
            departmentSelected.setDepartmentName(departmentName);
            departmentSelected.setParent(parent);
            departmentService.editDepartment(departmentSelected);
            addInfo("Departamentul '" + oldDepartmentName + "' a fost editat cu succes!");
            return "departments";
        } catch (Exception ex) {
            ex.printStackTrace();
            addError("A aparut o eroare la editarea departamentului. Va rugam incercati mai tarziu!");
            return "/pages/department.xhtml?=faces=true";
        }
    }

    public void initDepartmentPage() {
        if (FacesContext.getCurrentInstance().getPartialViewContext().isAjaxRequest())
            return;
        departmentSelected = null;
        departmentName = "";
        parent = null;
        editButtonGenerator = false;
    }

    public String onRowSelect() {
        addInfo(departmentSelected.getDepartmentName() + " selected");
        return "/pages/departments?faces-redirect=true";
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

    public DepartmentService getDepartmentService() {
        return departmentService;
    }

    public void setDepartmentService(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public Department getParent() {
        return parent;
    }

    public List<Department> getDepartments() {
        try {
            return departmentService.getDepartments();
        } catch (Exception ex) {
            addError("A aparut o problema la transmiterea datelor!");
            return new ArrayList<Department>();
        }
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public void setParent(Department parent) {
        this.parent = parent;
    }

    public void setDepartments(List<Department> departments) {
        this.departments = departments;
    }

    public Department getDepartmentSelected() {
        return departmentSelected;
    }

    public void setDepartmentSelected(Department departmentSelected) {
        this.departmentSelected = departmentSelected;
    }

    public List<User> getEmployees() {
        if (departmentSelected != null) {
            try {
                return departmentService.getEmployeesByDepartment(departmentSelected.getDepartmentId());
            } catch (Exception ex) {
                return new ArrayList<User>();
            }
        } else {
            return employees;
        }
    }

    public void setEmployees(List<User> employees) {
        this.employees = employees;
    }

    public Boolean getEditButtonGenerator() {
        return editButtonGenerator;
    }

    public void setEditButtonGenerator(Boolean editButtonGenerator) {
        this.editButtonGenerator = editButtonGenerator;
    }


}