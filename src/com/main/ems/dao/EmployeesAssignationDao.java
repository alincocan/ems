package main.ems.dao;

import java.util.List;

import main.ems.model.Department;
import main.ems.model.Job;
import main.ems.model.User;

public interface EmployeesAssignationDao {
    public List<User> getUnassignedDepartmentEmployees();

    public List<User> getAssignedDepartmentEmployees(int departmentId);

    public List<User> getUnassignedJobEmployees();

    public List<User> getAssignedJobEmployees(int jobId);

    public List<Department> getDepartments();

    public List<Job> getJobs();

    public void updateEmployee(User employee);

    public void addManagerToDepartment(Department department);

}