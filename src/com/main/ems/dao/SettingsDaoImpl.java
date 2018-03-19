package main.ems.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import main.ems.model.Settings;

@Repository
public class SettingsDaoImpl implements SettingsDao {

    @Autowired
    SessionFactory sessionFactory;


    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Settings getSetting(int id) {
        String hql = "From Settings where id = :id";
        List<Settings> settings = sessionFactory.getCurrentSession().createQuery(hql).setParameter("id", id).list();
        if (settings.size() > 0) {
            return settings.get(0);
        } else {
            return new Settings();
        }
    }

    @Override
    public void saveSalarySettings(List<Settings> settings) {
        for (Settings set : settings) {
            sessionFactory.getCurrentSession().update(set);
        }
    }
}
