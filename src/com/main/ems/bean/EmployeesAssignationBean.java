package main.ems.bean;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.jsf.FacesContextUtils;

import main.ems.model.Department;
import main.ems.model.Job;
import main.ems.model.User;
import main.ems.service.EmployeesAssignationService;

@ManagedBean(name = "employeesAssignationBean")
@ViewScoped
public class EmployeesAssignationBean {

    @Autowired
    EmployeesAssignationService employeesAssignationService;

    List<User> unassignedDepartmentEmployees;
    List<User> assignedDepartmentEmployees;

    List<User> unassignedJobEmployees;
    List<User> assignedJobEmployees;

    Department selectedDepartment;
    Job selectedJob;

    List<Department> departments;
    List<Job> jobs;

    List<User> selectedAssignedDepartmentEmployees;
    List<User> selectedUnassignedDepartmentEmployees;

    List<User> selectedAssignedJobEmployees;
    List<User> selectedUnassignedJobEmployees;


    @PostConstruct
    private void init() {
        FacesContextUtils
                .getRequiredWebApplicationContext(FacesContext.getCurrentInstance())
                .getAutowireCapableBeanFactory().autowireBean(this);
    }

    public void addManagerToDepartment() {

        if (selectedAssignedDepartmentEmployees.size() > 1) {
            addError("Selectati un singur angajat");
            return;
        }
        try {
            selectedDepartment.setManagerId(selectedAssignedDepartmentEmployees.get(0));
            employeesAssignationService.addManagerToDepartment(selectedDepartment);
            addInfo("Manager adaugat cu succes");
        } catch (IndexOutOfBoundsException ex) {
            addError("Selectati mai intai un angajat!");
            ex.printStackTrace();
        }
    }

    public void addEmployeesToDepartment() {
        for (int i = 0; i < selectedUnassignedDepartmentEmployees.size(); i++) {
            User employee = selectedUnassignedDepartmentEmployees.get(i);
            employee.setDepartment(selectedDepartment);
            try {
                employeesAssignationService.updateEmployee(employee);
            } catch (Exception ex) {
                addError("A aparut o eroare. Operatiune finalizata fara succes!");
            }
        }

        addInfo("Operatiune efectuata cu succes!");
    }

    public void getEmployeesFromDepartment() {
        for (int i = 0; i < selectedAssignedDepartmentEmployees.size(); i++) {
            User employee = selectedAssignedDepartmentEmployees.get(i);
            employee.setDepartment(null);
            try {
                if (selectedDepartment.getManagerId() != null)
                    if (selectedDepartment.getManagerId().getUserId() == selectedAssignedDepartmentEmployees.get(i).getUserId()) {
                        selectedDepartment.setManagerId(null);
                        employeesAssignationService.addManagerToDepartment(selectedDepartment);
                    }

                employeesAssignationService.updateEmployee(employee);
            } catch (Exception ex) {
                ex.printStackTrace();
                addError("A aparut o eroare. Operatiune finalizata fara succes!");
            }
        }

        addInfo("Operatiune efectuata cu succes!");
    }

    public void addEmployeesToJob() {
        for (int i = 0; i < selectedUnassignedJobEmployees.size(); i++) {
            User employee = selectedUnassignedJobEmployees.get(i);
            employee.setJob(selectedJob);
            try {
                employeesAssignationService.updateEmployee(employee);
            } catch (Exception ex) {
                addError("A aparut o eroare. Operatiune finalizata fara succes!");
            }
        }

        addInfo("Operatiune efectuata cu succes!");
    }

    public void getEmployeesFromJob() {
        for (int i = 0; i < selectedAssignedJobEmployees.size(); i++) {
            User employee = selectedAssignedJobEmployees.get(i);
            employee.setJob(null);
            try {
                employeesAssignationService.updateEmployee(employee);
            } catch (Exception ex) {
                addError("A aparut o eroare. Operatiune finalizata fara succes!");
            }
        }

        addInfo("Operatiune efectuata cu succes!");
    }

    public void initPage() {
        if (FacesContext.getCurrentInstance().getPartialViewContext().isAjaxRequest())
            return;
        selectedDepartment = null;
        selectedJob = null;
        selectedAssignedDepartmentEmployees = null;
        selectedUnassignedDepartmentEmployees = null;

        selectedAssignedJobEmployees = null;
        selectedUnassignedJobEmployees = null;
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

    public EmployeesAssignationService getEmployeesAssignationService() {
        return employeesAssignationService;
    }

    public void setEmployeesAssignationService(EmployeesAssignationService employeesAssignationService) {
        this.employeesAssignationService = employeesAssignationService;
    }

    public List<User> getUnassignedDepartmentEmployees() {
        return employeesAssignationService.getUnassignedDepartmentEmployees();
    }

    public void setUnassignedDepartmentEmployees(List<User> unassignedDepartmentEmployees) {
        this.unassignedDepartmentEmployees = unassignedDepartmentEmployees;
    }

    public List<User> getAssignedDepartmentEmployees() {
        if (selectedDepartment != null) {
            return employeesAssignationService.getAssignedDepartmentEmployees(selectedDepartment.getDepartmentId());
        } else {
            return new ArrayList<User>();
        }
    }

    public void setAssignedDepartmentEmployees(List<User> assignedDepartmentEmployees) {
        this.assignedDepartmentEmployees = assignedDepartmentEmployees;
    }

    public List<User> getUnassignedJobEmployees() {
        return employeesAssignationService.getUnassignedJobEmployees();
    }

    public void setUnassignedJobEmployees(List<User> unassignedJobEmployees) {
        this.unassignedJobEmployees = unassignedJobEmployees;
    }

    public List<User> getAssignedJobEmployees() {
        if (selectedJob != null) {
            return employeesAssignationService.getAssignedJobEmployees(selectedJob.getJobId());
        } else {
            return new ArrayList<User>();
        }
    }

    public void setAssignedJobEmployees(List<User> assignedJobEmployees) {
        this.assignedJobEmployees = assignedJobEmployees;
    }

    public Department getSelectedDepartment() {
        return selectedDepartment;
    }

    public void setSelectedDepartment(Department selectedDepartment) {
        this.selectedDepartment = selectedDepartment;
    }

    public List<Department> getDepartments() {
        return employeesAssignationService.getDepartments();
    }

    public void setDepartments(List<Department> departments) {
        this.departments = departments;
    }

    public List<User> getSelectedAssignedDepartmentEmployees() {
        return selectedAssignedDepartmentEmployees;
    }

    public void setSelectedAssignedDepartmentEmployees(List<User> selectedAssignedDepartmentEmployees) {
        this.selectedAssignedDepartmentEmployees = selectedAssignedDepartmentEmployees;
    }

    public List<User> getSelectedUnassignedDepartmentEmployees() {
        return selectedUnassignedDepartmentEmployees;
    }

    public void setSelectedUnassignedDepartmentEmployees(List<User> selectedUnassignedDepartmentEmployees) {
        this.selectedUnassignedDepartmentEmployees = selectedUnassignedDepartmentEmployees;
    }

    public List<User> getSelectedAssignedJobEmployees() {
        return selectedAssignedJobEmployees;
    }

    public void setSelectedAssignedJobEmployees(List<User> selectedAssignedJobEmployees) {
        this.selectedAssignedJobEmployees = selectedAssignedJobEmployees;
    }

    public List<User> getSelectedUnassignedJobEmployees() {
        return selectedUnassignedJobEmployees;
    }

    public void setSelectedUnassignedJobEmployees(List<User> selectedUnassignedJobEmployees) {
        this.selectedUnassignedJobEmployees = selectedUnassignedJobEmployees;
    }

    public Job getSelectedJob() {
        return selectedJob;
    }

    public void setSelectedJob(Job selectedJob) {
        this.selectedJob = selectedJob;
    }

    public List<Job> getJobs() {
        return employeesAssignationService.getJobs();
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }

}