package main.ems.dao;

import java.util.List;

import main.ems.model.Department;
import main.ems.model.User;

public interface DepartmentDao {

    public String addDepartment(Department department);

    public List<Department> getDepartments();

    public List<User> getEmployeesByDepartment(int departmentId);

    public List<Department> getDepartmentByName(String departmentName);

    public void deleteDepartment(Department department);

    public Boolean checkIsFather(int departmentId);

    public void editDepartment(Department department);
}