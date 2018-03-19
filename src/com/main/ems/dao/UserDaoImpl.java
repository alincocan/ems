package main.ems.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import main.ems.model.Department;
import main.ems.model.User;

@Repository
public class UserDaoImpl implements UserDao {
    @Autowired
    SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public User getUserByUsernameAndPassword(String username, String password) {
        List<User> users = new ArrayList<User>();
        String hql = "FROM User where username = :user and password = :pass";
        users = (List<User>) sessionFactory.getCurrentSession().createQuery(hql).setParameter("user", username).setParameter("pass", password).list();
        if (users.size() > 0) {
            return users.get(0);
        } else {
            return null;
        }
    }

    @Override
    public User getUserByUsername(String username) {
        List<User> users = new ArrayList<User>();
        String hql = "From User where username = :user";
        try {
            users = (List<User>) sessionFactory.getCurrentSession().createQuery(hql).setParameter("user", username).list();
            if (users.size() > 0) {
                return users.get(0);
            } else {
                return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public Boolean isManager(int empId) {
        String hql = "From Department where managerId.userId = :id";
        List<Department> departments = sessionFactory.getCurrentSession().createQuery(hql).setParameter("id", empId).list();
        if (departments.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean isHrUser(int empId) {
        String hql = "From User where userId = :id and role.roleId = :rid";
        List<User> users = sessionFactory.getCurrentSession().createQuery(hql).setParameter("id", empId).setParameter("rid", 2).list();
        if (users.size() > 0) {
            return true;
        } else {
            return false;
        }

    }
}
