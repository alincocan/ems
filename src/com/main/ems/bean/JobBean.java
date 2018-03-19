package main.ems.bean;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import main.ems.model.Job;
import main.ems.model.User;
import main.ems.service.JobService;

@Controller("jobBean")
@Scope("session")
public class JobBean {
    @Autowired
    JobService jobService;
    public List<Job> jobs;
    public String jobName;
    public String jobDescription;
    public List<User> employees;
    public Job jobSelected;
    public Boolean editButtonGenerator;

    public String addJob() {
        List<Job> jobs = jobService.getJobByName(jobName);
        if (jobs == null) {
            addError("Eroare la introducerea jobului cu numele '" + jobName + "'!");
            return null;
        } else {
            if (jobs.size() > 0) {
                addError("Jobul cu numele '" + jobName + "' exista deja in sistem!");
                return null;
            }
        }
        Job job = new Job(jobName, jobDescription);
        String errMessage = jobService.addJob(job);
        if (errMessage == null) {
            addInfo("Job-ul '" + jobName + "' a fost adaugat cu succes!");
            jobName = "";
            jobDescription = "";
            return "jobs";
        } else {
            addError("Eroare la introducerea jobului cu numele '" + jobName + "'!");
            return null;
        }
    }

    public void deleteJob(ActionEvent actionEvent) {
        if (jobService.checkHasEmployees(jobSelected)) {
            addError("Nu puteti sterge un job care contine angajati!");
            return;
        }
        String errorMessage;
        String jobName = jobSelected.getJobName();
        try {
            errorMessage = jobService.deleteJob(jobSelected);
            if (errorMessage == null) {
                addInfo("Job-ul " + jobName + " a fost sters cu succes!");
            } else {
                addError("O eroare a aparut la stergerea jobului. Va rugam incercati mai tarziu!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();

        }
    }

    public String toEdit() {
        editButtonGenerator = true;
        jobName = jobSelected.getJobName();
        jobDescription = jobSelected.getJobDescription();
        return "job";
    }

    public String editJob() {
        String oldJobName = jobSelected.getJobName();
        List<Job> jobs = jobService.getJobByName(jobName);
        if (jobs == null) {
            addError("Eroare la editarea jobului cu numele '" + jobName + "'!");
            return null;
        } else {
            if (jobs.size() > 0 && !(jobs.get(0).getJobName().equals(oldJobName))) {
                addError("Jobul cu numele '" + jobName + "' exista deja in sistem!");
                return null;
            }
        }
        jobSelected.setJobName(jobName);
        jobSelected.setJobDescription(jobDescription);
        String errorMsg = jobService.editJob(jobSelected);
        if (errorMsg == null) {
            addInfo("Job-ul " + oldJobName + "a fost editat cu succes!");
            jobName = "";
            jobDescription = "";
            return "jobs";
        } else {
            addError("A aparut o problema la editarea job-ului cu numele +" + jobName);
            return null;
        }
    }

    public void redirectToEdit() {

    }

    public String onRowSelect() {
        addInfo(jobSelected.getJobName() + " selected");
        return "/pages/jobs?faces-redirect=true";
    }

    public Boolean checkJobIsNull() {
        if (this.jobSelected == null)
            return true;
        else
            return false;
    }

    public void initJobPage() {
        if (FacesContext.getCurrentInstance().getPartialViewContext().isAjaxRequest())
            return;
        jobSelected = null;
        jobName = "";
        jobDescription = "";
        editButtonGenerator = false;
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

    public JobService getJobService() {
        return jobService;
    }

    public void setJobService(JobService jobService) {
        this.jobService = jobService;
    }

    public List<Job> getJobs() {
        return jobService.getJobs();
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public List<User> getEmployees() {
        if (jobSelected != null)
            return jobService.getEmployeesByJob(jobSelected.getJobId());
        else
            return employees;
    }

    public void setEmployees(List<User> employees) {
        this.employees = employees;
    }

    public Job getJobSelected() {
        return jobSelected;
    }

    public void setJobSelected(Job jobSelected) {
        this.jobSelected = jobSelected;
    }

    public Boolean getEditButtonGenerator() {
        return editButtonGenerator;
    }

    public void setEditButtonGenerator(Boolean editButtonGenerator) {
        this.editButtonGenerator = editButtonGenerator;
    }
}
