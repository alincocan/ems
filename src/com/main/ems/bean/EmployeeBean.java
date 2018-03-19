package main.ems.bean;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import main.ems.util.AddMessage;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.FlowEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.jsf.FacesContextUtils;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import main.ems.model.Role;
import main.ems.model.User;
import main.ems.service.EmployeeService;

@ManagedBean(name = "employeeBean")
@ViewScoped
public class EmployeeBean {

    AddMessage addMessage = new AddMessage();

    public Boolean skip;
    public String confirmPassword;
    public int role;
    public UploadedFile uploadFile;
    public String fileName;
    public byte[] fileContent;
    public InputStream is;

    @Autowired
    public EmployeeService employeeService;
    public List<User> employees;
    public User employee;
    public User selectedEmployee;
    public int flag;

    @PostConstruct
    private void init() {
        FacesContextUtils
                .getRequiredWebApplicationContext(FacesContext.getCurrentInstance())
                .getAutowireCapableBeanFactory().autowireBean(this);

        employee = new User();
        selectedEmployee = new User();
        System.out.println("dsadasdsasadadasdsa");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("Bean will destroy now.");
    }

    public String onFlowProcess(FlowEvent event) {
        return event.getNewStep();
    }

    public AddMessage getAddMessage() {
        return addMessage;
    }

    public void setAddMessage(AddMessage addMessage) {
        this.addMessage = addMessage;
    }

    public UserBean getUserBean() {
        FacesContext fctx = FacesContext.getCurrentInstance();
        UserBean userBean = (UserBean) fctx.getApplication().evaluateExpressionGet(fctx, "#{userBean}", UserBean.class);
        System.out.println(userBean.getUser());
        return userBean;
    }

    public String addEmployee() {

        try {


            employeeService.addEmployee(employee, role,fileName, fileContent);
            addInfo("Anagajatul '" + employee.toString() + "' a fost adaugat cu succes!");
            return "employees";
        } catch (Exception ex) {
            ex.printStackTrace();
            addError("A aparut o problema la inserarea angajatului!");
            return "pages/employee?faces-redirect=true";
        }
    }

    public void toAdd() {
        flag = 1;
    }

    public void toEdit() {
        flag = 2;
    }

    public void handleFileUpload(FileUploadEvent event) {

        addInfo("Succesful" + event.getFile().getFileName() + " is uploaded.");
        try {
            fileName = event.getFile().getFileName();
            InputStream is = event.getFile().getInputstream();
            fileContent = new byte[(int) event.getFile().getSize()];
            is.read(fileContent);
            is.close();


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String editEmployee() {
        try {

            Role rol = employeeService.getRoleById(role);
            selectedEmployee.setRole(rol);

            if (fileContent != null) {
                selectedEmployee.setAvatarName(fileName);
                selectedEmployee.setAvatarContent(fileContent);
            }
            employeeService.editEmployee(selectedEmployee);
            addInfo("Angajatul " + selectedEmployee.toString() + " a fost editat cu succes!");
            return "employees";
        } catch (Exception ex) {
            ex.printStackTrace();
            addError("A aparut o eroare la editarea angajatului cu numele " + selectedEmployee.toString() + "!");
            return "employees";
        }
    }


    public String deleteEmployee() {
        if (employeeService.isManager(selectedEmployee.getUserId())) {
            addError("Nu puteti sterge un angajat care este manager intr-un departament!");
            return "/pages/employees.xhtml?faces-redirect=true";
        }
        try {
            employeeService.deleteEmployee(selectedEmployee);
            addInfo("Angajatul " + selectedEmployee.toString() + " a fost sters cu succes!");
            return "/pages/employees.xhtml?faces-redirect=true";
        } catch (Exception ex) {
            ex.printStackTrace();
            addError("A aparut o eroare la stergerea angajatului. Va rugam incercati mai tarziu!");
            return "/pages/employees.xhtml?faces-redirect=true";
        }
    }

    public void addError(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, null);
        FacesContext.getCurrentInstance().addMessage(null, message);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
    }

    public void addInfo(String summary) {
//        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
//        FacesContext.getCurrentInstance().addMessage(null, message);
//        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
//        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();

        addMessage.addInfo(summary);
    }

    public void preProcessPDF(Object document) throws IOException, BadElementException, DocumentException {
        Document pdf = (Document) document;
        pdf.open();
        pdf.setPageSize(PageSize.A4);

        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        String logo = servletContext.getRealPath("") + File.separator + "resources" + File.separator + "images" + File.separator + "ems-logo.png";
        Image.getInstance(logo).setWidthPercentage(10);

        pdf.add(Image.getInstance(logo));

        pdf.add(new Paragraph("     " + "Author:"));
        pdf.add(new Paragraph("     " + getUserBean().getUser().toString()));
        pdf.add(new Paragraph(" "));


    }

    public Boolean getSkip() {
        return skip;
    }

    public void setSkip(Boolean skip) {
        this.skip = skip;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }


    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }


    public EmployeeService getEmployeeService() {
        return employeeService;
    }

    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    public List<User> getEmployees() {
        return employeeService.getEmployees();
    }

    public void setEmployees(List<User> employees) {
        this.employees = employees;
    }

    public UploadedFile getUploadFile() {
        return uploadFile;
    }

    public void setUploadFile(UploadedFile uploadFile) {
        this.uploadFile = uploadFile;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getFileContent() {
        return fileContent;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
    }

    public InputStream getIs() {
        return is;
    }

    public void setIs(InputStream is) {
        this.is = is;
    }

    public User getEmployee() {
        return employee;
    }

    public void setEmployee(User employee) {
        this.employee = employee;
    }

    public User getSelectedEmployee() {
        return selectedEmployee;
    }

    public void setSelectedEmployee(User selectedEmployee) {
        this.selectedEmployee = selectedEmployee;
    }


}