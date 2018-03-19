package main.ems.bean;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.jsf.FacesContextUtils;

import main.ems.model.Document;
import main.ems.model.User;
import main.ems.service.DocumentService;

@ManagedBean(name = "documentBean")
@ViewScoped
public class DocumentBean {

    @Autowired
    DocumentService documentService;
    public List<User> employees;
    public User selectedEmployee;
    public List<Document> documents;
    public List<Document> myDocuments;
    public int flag;

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

    public void deleteDocuments(Document document) {
        try {
            documentService.deleteDocument(document);
            addInfo("Document sters cu succes!");
        } catch (Exception ex) {
            ex.printStackTrace();
            addError("A aparut o eraore la stergerea documentului");
        }
    }

    public void handleFileUpload(FileUploadEvent event) {

        Document document = new Document();
        try {
            document.setDocumentName(event.getFile().getFileName());
            document.setDocumentContent(event.getFile().getContents());
            document.setUser(selectedEmployee);
            documentService.insertDocument(document);
            addInfo("Succesful" + event.getFile().getFileName() + " is uploaded.");
        } catch (Exception ex) {
            ex.printStackTrace();
            addError("Error. Please try again later");
        }
    }

    public DefaultStreamedContent convertToInputStream(byte[] file, String fileName) {
        InputStream stream = new ByteArrayInputStream(file);
        return new DefaultStreamedContent(stream, getType(fileName), fileName);
    }

    public void toAdd(User employee) {
        selectedEmployee = employee;
        flag = 2;
    }

    public void toView(User employee) {
        selectedEmployee = employee;
        flag = 1;
    }

    public void toTable() {
        selectedEmployee = null;
        flag = 0;
    }

    public String getType(String fileName) {
        String type = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (type.equals("jpg")) {
            return "image/jpg";
        } else if (type.equals("png")) {
            return "image/png";
        } else if (type.equals("pdf")) {
            return "application/pdf";
        } else {
            return null;
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

    public DocumentService getDocumentService() {
        return documentService;
    }

    public void setDocumentService(DocumentService documentService) {
        this.documentService = documentService;
    }

    public List<User> getEmployees() {
        return documentService.getEmployees();
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

    public List<Document> getDocuments() {
        return documentService.getDocumentsForUser(selectedEmployee.getUserId());
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public List<Document> getMyDocuments() {
        return documentService.getDocumentsForUser(getUserBean().getUser().getUserId());
    }

    public void setMyDocuments(List<Document> myDocuments) {
        this.myDocuments = myDocuments;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
