package main.ems.dao;

import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import main.ems.model.AdditionalRequirements;
import main.ems.model.CurriculumVitae;
import main.ems.model.Responsabilities;
import main.ems.model.SkillsRequired;
import main.ems.model.Vacancy;

@Repository
public class VacancyDaoImpl implements VacancyDao {

    @Autowired
    SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Vacancy> getVacanciesByJob(int jobId) {
        String hql = "From Vacancy where job.jobId = :id order by job.jobName asc";
        return sessionFactory.getCurrentSession().createQuery(hql).setParameter("id", jobId).list();
    }

    @Override
    public List<Vacancy> getVacancies() {
        String hql = "From Vacancy order by job.jobName asc";
        return sessionFactory.getCurrentSession().createQuery(hql).list();
    }

    @Override
    public List<Responsabilities> getResponsabilities(int vacancyId) {
        String hql = "From Responsabilities where vacancy.id = :id";
        return sessionFactory.getCurrentSession().createQuery(hql).setParameter("id", vacancyId).list();
    }

    @Override
    public List<SkillsRequired> getSkillsRequired(int vacancyId) {
        String hql = "From SkillsRequired where vacancy.id = :id";
        return sessionFactory.getCurrentSession().createQuery(hql).setParameter("id", vacancyId).list();
    }

    @Override
    public List<AdditionalRequirements> getAdditionalRequirements(int vacancyId) {
        String hql = "From AdditionalRequirements where vacancy.id = :id";
        return sessionFactory.getCurrentSession().createQuery(hql).setParameter("id", vacancyId).list();
    }

    @Override
    public void saveResponsabilities(Responsabilities responsabilities) {
        sessionFactory.getCurrentSession().save(responsabilities);
    }

    @Override
    public void saveSkillsRequired(SkillsRequired skillsRequired) {
        sessionFactory.getCurrentSession().save(skillsRequired);
    }

    @Override
    public void saveAdditionalRequirements(AdditionalRequirements additionalRequirements) {
        sessionFactory.getCurrentSession().save(additionalRequirements);
    }

    @Override
    public Vacancy getVacancy(int id) {
        return (Vacancy) sessionFactory.getCurrentSession().get(Vacancy.class, id);
    }

    @Override
    public int saveVacancy(Vacancy vacancy, List<Responsabilities> responsabilities, List<SkillsRequired> skillsRequired, List<AdditionalRequirements> additionalRequirements) {
        int vacancyId = (int) sessionFactory.getCurrentSession().save(vacancy);
        sessionFactory.getCurrentSession().flush();
        for (Responsabilities resp : responsabilities) {
            String query = "insert into responsabilities(value,vacancy_id) values (:value,:id)";
            SQLQuery sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query);
            sqlQuery.setParameter("value", resp.getValue());
            sqlQuery.setParameter("id", vacancyId);
            sqlQuery.executeUpdate();
        }

        for (SkillsRequired skills : skillsRequired) {
            String query = "insert into skills_required (value,vacancy_id) values (:value,:id)";
            SQLQuery sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query);
            sqlQuery.setParameter("value", skills.getValue());
            sqlQuery.setParameter("id", vacancyId);
            sqlQuery.executeUpdate();
        }

        for (AdditionalRequirements add : additionalRequirements) {
            String query = "insert into additional_requirements (value,vacancy_id) values (:value,:id)";
            SQLQuery sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query);
            sqlQuery.setParameter("value", add.getValue());
            sqlQuery.setParameter("id", vacancyId);
            sqlQuery.executeUpdate();
        }
        return 1;
    }

    @Override
    public List<CurriculumVitae> getCVByVacancy(int vacancyId) {
        String hql = "From CurriculumVitae where vacancy.id = :id";
        return sessionFactory.getCurrentSession().createQuery(hql).setParameter("id", vacancyId).list();
    }
}
