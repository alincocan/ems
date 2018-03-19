package main.ems.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import main.ems.model.Department;
import main.ems.model.User;

@Repository
public class DepartmentDaoImpl implements DepartmentDao {
    @Autowired
    SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public String addDepartment(Department department) {
        try {
            sessionFactory.getCurrentSession().merge(department);
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "Error";
        }
    }

    @Override
    public List<Department> getDepartments() {
        String hql = "FROM Department";
        return sessionFactory.getCurrentSession().createQuery(hql).list();
    }

    @Override
    public List<User> getEmployeesByDepartment(int departmentId) {
        String hql = "From User where department.departmentId = :depId";
        return sessionFactory.getCurrentSession().createQuery(hql).setParameter("depId", departmentId).list();
    }

    @Override
    public List<Department> getDepartmentByName(String departmentName) {
        String hql = "FROM Department where departmentName = :depName";
        return sessionFactory.getCurrentSession().createQuery(hql).setParameter("depName", departmentName).list();
    }

    @Override
    public void deleteDepartment(Department department) {
        sessionFactory.getCurrentSession().delete(department);
    }

    @Override
    public Boolean checkIsFather(int departmentId) {
        String hql = "From Department where parent.departmentId = :dep";
        List<Department> departments = new ArrayList<Department>();
        departments = sessionFactory.getCurrentSession().createQuery(hql).setParameter("dep", departmentId).list();
        if (departments.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void editDepartment(Department department) {
        sessionFactory.getCurrentSession().update(department);
    }
}