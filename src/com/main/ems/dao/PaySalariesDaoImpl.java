package main.ems.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import main.ems.model.Salaries;

@Repository
public class PaySalariesDaoImpl implements PaySalariesDao {

    @Autowired
    SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void salaryPaid(Salaries salary) {
        sessionFactory.getCurrentSession().save(salary);
    }

    @Override
    public List<Date> getDistinctDatesForPaidSalaries() {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Salaries.class);
        criteria.setProjection(Projections.distinct(Projections.property("salaryDate")));
        criteria.addOrder(Order.desc("salaryDate"));
        return criteria.list();
    }

    @Override
    public List<Salaries> getSalariesByDate(Date date) {
        String hql = "From Salaries where salaryDate = :date";
        return sessionFactory.getCurrentSession().createQuery(hql).setParameter("date", date).list();
    }

    @Override
    public List<Salaries> getMyPaidSalaries(int id) {
        String hql = "From Salaries where user.userId = :id order by salaryDate desc";
        return sessionFactory.getCurrentSession().createQuery(hql).setParameter("id", id).list();
    }
}
