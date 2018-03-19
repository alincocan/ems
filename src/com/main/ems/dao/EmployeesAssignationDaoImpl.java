package main.ems.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import main.ems.model.Department;
import main.ems.model.Job;
import main.ems.model.User;

@Repository
public class EmployeesAssignationDaoImpl implements EmployeesAssignationDao {

    @Autowired
    SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<User> getUnassignedDepartmentEmployees() {
        String hql = "From User where department is null";
        return sessionFactory.getCurrentSession().createQuery(hql).list();
    }

    @Override
    public List<User> getAssignedDepartmentEmployees(int departmentId) {
        String hql = "From User where department.departmentId = :dep";
        return sessionFactory.getCurrentSession().createQuery(hql).setParameter("dep", departmentId).list();
    }

    @Override
    public List<User> getUnassignedJobEmployees() {
        String hql = "From User where job is null";
        return sessionFactory.getCurrentSession().createQuery(hql).list();
    }

    @Override
    public List<User> getAssignedJobEmployees(int jobId) {
        String hql = "From User where job.jobId = :job";
        return sessionFactory.getCurrentSession().createQuery(hql).setParameter("job", jobId).list();
    }

    @Override
    public List<Department> getDepartments() {
        String hql = "From Department";
        return sessionFactory.getCurrentSession().createQuery(hql).list();
    }

    @Override
    public void updateEmployee(User employee) {
        sessionFactory.getCurrentSession().update(employee);
    }

    @Override
    public List<Job> getJobs() {
        String hql = "From Job";
        return sessionFactory.getCurrentSession().createQuery(hql).list();
    }

    @Override
    public void addManagerToDepartment(Department department) {
        sessionFactory.getCurrentSession().update(department);
    }
}