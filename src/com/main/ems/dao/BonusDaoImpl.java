package main.ems.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import main.ems.model.Bonus;
import main.ems.model.BonusType;
import main.ems.model.User;

@Repository
public class BonusDaoImpl implements BonusDao {

    @Autowired
    SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<User> getEmployees(int departmentId, int userId) {
        String hql = "From User where department.departmentId = :id and userId <> :u_id";
        return sessionFactory.getCurrentSession().createQuery(hql).setParameter("id", departmentId).setParameter("u_id", userId).list();
    }

    @Override
    public BonusType getBonusType(int id) {
        String hql = "From BonusType where id = :id";
        List<BonusType> bonusType = sessionFactory.getCurrentSession().createQuery(hql).setParameter("id", id).list();
        try {
            return bonusType.get(0);
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public void addBonus(Bonus bonus) {
        sessionFactory.getCurrentSession().save(bonus);
    }

    @Override
    public void updateBonus(Bonus bonus) {
        sessionFactory.getCurrentSession().update(bonus);
    }

    @Override
    public List<Bonus> getBonusForApproval(int userId) {
        String hql = "From Bonus where currentState.userId = :id and approved = :param";
        return sessionFactory.getCurrentSession().createQuery(hql).setParameter("id", userId).setParameter("param", "N").list();
    }

    @Override
    public List<Bonus> getBonusProposed(int id) {
        String hql = "From Bonus where proponent.userId = :id order by bonusDate desc";
        return sessionFactory.getCurrentSession().createQuery(hql).setParameter("id", id).list();
    }

    @Override
    public List<Bonus> getBonusById(int id) {
        String hql = "From Bonus where id = :id";
        return sessionFactory.getCurrentSession().createQuery(hql).setParameter("id", id).list();
    }

    @Override
    public List<Bonus> getMyBonus(int id) {
        String hql = "From Bonus where employee.userId = :id order by approvedDate desc";
        return sessionFactory.getCurrentSession().createQuery(hql).setParameter("id", id).list();
    }

    @Override
    public void updateEmployeeHolidayDays(User user) {
        sessionFactory.getCurrentSession().update(user);
    }

    @Override
    public List<Bonus> getUnpaidMoneyBonus(User emp) {
        String hql = "From Bonus where bonusType.id = 1 and employee.userId = :id and approved = 'Y' and paid = 'N'";
        return sessionFactory.getCurrentSession().createQuery(hql).setParameter("id", emp.getUserId()).list();
    }
}
