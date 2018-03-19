package main.ems.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import main.ems.model.User;

@Repository
public class HolidayChartDaoImpl implements HolidayChartDao {

    @Autowired
    SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<User> getEmployeesFromDepartment(int depId, int manId) {
        String hql = "From User where department.departmentId = :id and userId <> :mandId";
        return sessionFactory.getCurrentSession().createQuery(hql).setParameter("id", depId).setParameter("mandId", manId).list();
    }
}
