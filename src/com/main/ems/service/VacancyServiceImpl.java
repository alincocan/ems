package main.ems.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import main.ems.dao.VacancyDao;
import main.ems.model.AdditionalRequirements;
import main.ems.model.CurriculumVitae;
import main.ems.model.Responsabilities;
import main.ems.model.SkillsRequired;
import main.ems.model.Vacancy;

@Service
public class VacancyServiceImpl implements VacancyService {

    @Autowired
    VacancyDao vacancyDao;

    public VacancyDao getVacancyDao() {
        return vacancyDao;
    }

    public void setVacancyDao(VacancyDao vacancyDao) {
        this.vacancyDao = vacancyDao;
    }

    @Override
    @Transactional
    public List<Vacancy> getVacanciesByJob(int jobId) {
        return vacancyDao.getVacanciesByJob(jobId);
    }

    @Override
    @Transactional
    public List<Vacancy> getVacancies() {
        return vacancyDao.getVacancies();
    }

    @Override
    @Transactional
    public List<Responsabilities> getResponsabilities(int vacancyId) {
        return vacancyDao.getResponsabilities(vacancyId);
    }

    @Override
    @Transactional
    public List<SkillsRequired> getSkillsRequired(int vacancyId) {
        return vacancyDao.getSkillsRequired(vacancyId);
    }

    @Override
    @Transactional
    public List<AdditionalRequirements> getAdditionalRequirements(int vacancyId) {
        return vacancyDao.getAdditionalRequirements(vacancyId);
    }

    @Override
    @Transactional
    public int saveVacancy(Vacancy vacancy, List<Responsabilities> responsabilities, List<SkillsRequired> skillsRequired, List<AdditionalRequirements> additionalRequirements) {
        return vacancyDao.saveVacancy(vacancy, responsabilities, skillsRequired, additionalRequirements);
    }

    @Override
    @Transactional
    public void saveResponsabilities(Responsabilities responsabilities) {
        vacancyDao.saveResponsabilities(responsabilities);
    }

    @Override
    @Transactional
    public void saveSkillsRequired(SkillsRequired skillsRequired) {
        vacancyDao.saveSkillsRequired(skillsRequired);
    }

    @Override
    @Transactional
    public void saveAdditionalRequirements(AdditionalRequirements additionalRequirements) {
        vacancyDao.saveAdditionalRequirements(additionalRequirements);
    }

    @Override
    @Transactional
    public Vacancy getVacancy(int id) {
        return vacancyDao.getVacancy(id);
    }

    @Override
    @Transactional
    public List<CurriculumVitae> getCVByVacancy(int vacancyId) {
        return vacancyDao.getCVByVacancy(vacancyId);
    }
}
