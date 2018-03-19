package main.ems.bean;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.jsf.FacesContextUtils;

import main.ems.model.AdditionalRequirements;
import main.ems.model.CurriculumVitae;
import main.ems.model.Job;
import main.ems.model.Responsabilities;
import main.ems.model.SkillsRequired;
import main.ems.model.Vacancy;
import main.ems.service.JobService;
import main.ems.service.VacancyService;
import main.ems.util.AddMessage;

@ManagedBean(name = "vacancyBean")
@ViewScoped
public class VacancyBean {

    @Autowired
    VacancyService vacancyService;
    @Autowired
    JobService jobService;
    public List<Responsabilities> responsabilitiesList;
    public List<SkillsRequired> skillsRequiredList;
    public List<AdditionalRequirements> additionalRequirementsList;
    public Vacancy vacancy;
    public Responsabilities responsability;
    public SkillsRequired skillRequired;
    public AdditionalRequirements additionalRequirement;
    public List<CurriculumVitae> cvByJobId;

    @PostConstruct
    public void init() {
        FacesContextUtils
                .getRequiredWebApplicationContext(FacesContext.getCurrentInstance())
                .getAutowireCapableBeanFactory().autowireBean(this);

        vacancy = new Vacancy();
        responsability = new Responsabilities();
        skillRequired = new SkillsRequired();
        additionalRequirement = new AdditionalRequirements();
        responsabilitiesList = new ArrayList<Responsabilities>();
        skillsRequiredList = new ArrayList<SkillsRequired>();
        additionalRequirementsList = new ArrayList<AdditionalRequirements>();
    }

    public List<Vacancy> getVacancyById(int jobId) {
        return vacancyService.getVacanciesByJob(jobId);
    }

    public List<Job> getJobs() {
        return jobService.getJobs();
    }

    public void addResponsabilityToList() {
        responsabilitiesList.add(responsability);
        responsability = new Responsabilities();
    }

    public void addSkillsRequiredToList() {
        skillsRequiredList.add(skillRequired);
        skillRequired = new SkillsRequired();
    }

    public void addAdditionalRequirementsToList() {
        additionalRequirementsList.add(additionalRequirement);
        additionalRequirement = new AdditionalRequirements();
    }

    public String addVacancy() {
        try {
            vacancyService.saveVacancy(vacancy, responsabilitiesList, skillsRequiredList, additionalRequirementsList);

            new AddMessage().addInfo("Succes");
            return "vacancies";
        } catch (Exception ex) {
            ex.printStackTrace();
            new AddMessage().addError("Error");
            return null;
        }
    }

    public List<CurriculumVitae> getCV(int vacancyId) {
        return vacancyService.getCVByVacancy(vacancyId);
    }

    public List<Vacancy> getVacancies() {
        return vacancyService.getVacancies();
    }

    public List<Responsabilities> getResponsabilitiesByVacancy(int id) {
        return vacancyService.getResponsabilities(id);
    }

    public List<SkillsRequired> getSkillsRequiredByVacancy(int id) {
        return vacancyService.getSkillsRequired(id);
    }

    public List<AdditionalRequirements> getAdditionalRequirementsByVacancy(int id) {
        return vacancyService.getAdditionalRequirements(id);
    }

    public List<Responsabilities> getResponsabilitiesList() {
        return responsabilitiesList;
    }

    public void setResponsabilitiesList(List<Responsabilities> responsabilitiesList) {
        this.responsabilitiesList = responsabilitiesList;
    }

    public List<SkillsRequired> getSkillsRequiredList() {
        return skillsRequiredList;
    }

    public void setSkillsRequiredList(List<SkillsRequired> skillsRequiredList) {
        this.skillsRequiredList = skillsRequiredList;
    }

    public List<AdditionalRequirements> getAdditionalRequirementsList() {
        return additionalRequirementsList;
    }

    public void setAdditionalRequirementsList(List<AdditionalRequirements> additionalRequirementsList) {
        this.additionalRequirementsList = additionalRequirementsList;
    }

    public Vacancy getVacancy() {
        return vacancy;
    }

    public void setVacancy(Vacancy vacancy) {
        this.vacancy = vacancy;
    }

    public Responsabilities getResponsability() {
        return responsability;
    }

    public void setResponsability(Responsabilities responsability) {
        this.responsability = responsability;
    }

    public SkillsRequired getSkillRequired() {
        return skillRequired;
    }

    public void setSkillRequired(SkillsRequired skillRequired) {
        this.skillRequired = skillRequired;
    }

    public AdditionalRequirements getAdditionalRequirement() {
        return additionalRequirement;
    }

    public void setAdditionalRequirement(AdditionalRequirements additionalRequirement) {
        this.additionalRequirement = additionalRequirement;
    }

    public List<CurriculumVitae> getCvByJobId() {
        return cvByJobId;
    }

    public void setCvByJobId(List<CurriculumVitae> cvByJobId) {
        this.cvByJobId = cvByJobId;
    }
} 
