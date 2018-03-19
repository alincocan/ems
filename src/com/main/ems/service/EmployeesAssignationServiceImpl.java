package main.ems.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import main.ems.dao.EmployeesAssignationDao;
import main.ems.model.Department;
import main.ems.model.Job;
import main.ems.model.User;

@Service
public class EmployeesAssignationServiceImpl implements EmployeesAssignationService {

    @Autowired
    EmployeesAssignationDao employeesAssignationDao;

    public void setEmployeesAssignationDao(EmployeesAssignationDao employeesAssignationDao) {
        this.employeesAssignationDao = employeesAssignationDao;
    }

    @Override
    @Transactional
    public List<User> getUnassignedDepartmentEmployees() {
        return employeesAssignationDao.getUnassignedDepartmentEmployees();
    }

    @Override
    @Transactional
    public List<User> getAssignedDepartmentEmployees(int departmentId) {
        return employeesAssignationDao.getAssignedDepartmentEmployees(departmentId);
    }

    @Override
    @Transactional
    public List<User> getUnassignedJobEmployees() {
        return employeesAssignationDao.getUnassignedJobEmployees();
    }

    @Override
    @Transactional
    public List<User> getAssignedJobEmployees(int jobId) {
        return employeesAssignationDao.getAssignedJobEmployees(jobId);
    }

    @Override
    @Transactional
    public List<Department> getDepartments() {
        return employeesAssignationDao.getDepartments();
    }

    @Override
    @Transactional
    public void updateEmployee(User employee) {
        employeesAssignationDao.updateEmployee(employee);
    }

    @Override
    @Transactional
    public List<Job> getJobs() {
        return employeesAssignationDao.getJobs();
    }

    @Override
    @Transactional
    public void addManagerToDepartment(Department department) {
        employeesAssignationDao.addManagerToDepartment(department);
    }
}