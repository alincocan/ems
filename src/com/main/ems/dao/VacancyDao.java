package main.ems.dao;

import java.util.List;

import main.ems.model.AdditionalRequirements;
import main.ems.model.CurriculumVitae;
import main.ems.model.Responsabilities;
import main.ems.model.SkillsRequired;
import main.ems.model.Vacancy;


public interface VacancyDao {
    public List<Vacancy> getVacanciesByJob(int jobId);

    public List<Vacancy> getVacancies();

    public List<Responsabilities> getResponsabilities(int vacancyId);

    public List<SkillsRequired> getSkillsRequired(int vacancyId);

    public List<AdditionalRequirements> getAdditionalRequirements(int vacancyId);

    public int saveVacancy(Vacancy vacancy, List<Responsabilities> responsabilities, List<SkillsRequired> skillsRequired, List<AdditionalRequirements> additionalRequirements);

    public void saveResponsabilities(Responsabilities responsabilities);

    public void saveSkillsRequired(SkillsRequired skillsRequired);

    public void saveAdditionalRequirements(AdditionalRequirements additionalRequirements);

    public Vacancy getVacancy(int id);

    public List<CurriculumVitae> getCVByVacancy(int vacancyId);
    //public void addVacancy(List<Responsabilities> responsabilities,List<SkillsRequired> skillsRequired,List<AdditionalRequirements> additionalRequirements,Vacancy vacancy);
}
