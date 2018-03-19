package main.ems.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import main.ems.model.Department;
import main.ems.model.Role;
import main.ems.model.User;

@Repository
public class EmployeeDaoimpl implements EmployeeDao {

    @Autowired
    SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Role getRoleById(int roleId) {
        String hql = "From Role where roleId = :id";
        return (Role) sessionFactory.getCurrentSession().createQuery(hql).setParameter("id", roleId).list().get(0);
    }

    @Override
    public User addEmployee(User user) {
        sessionFactory.getCurrentSession().save(user);
        return user;
    }

    @Override
    public List<User> getEmployees() {
        String hql = "From User";
        return sessionFactory.getCurrentSession().createQuery(hql).list();
    }

    @Override
    public Boolean isManager(int managerId) {
        String hql = "From Department where managerId.userId = :id";
        List<Department> departments = sessionFactory.getCurrentSession().createQuery(hql).setParameter("id", managerId).list();
        if (departments.size() > 0)
            return true;
        else
            return false;
    }

    @Override
    public void editEmployee(User user) {
        sessionFactory.getCurrentSession().update(user);
    }

    @Override
    public void deleteEmployee(User user) {
        sessionFactory.getCurrentSession().delete(user);
    }
}