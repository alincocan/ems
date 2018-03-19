package main.ems.service;

import java.util.List;

import main.ems.model.Department;
import main.ems.model.Role;
import main.ems.model.User;

public interface EmployeeService {
    public Role getRoleById(int roleId);

    public User addEmployee(User user, int role, String fileName , byte[] fileContent);

    public List<User> getEmployees();

    public Boolean isManager(int managerId);

    public void editEmployee(User employee);

    public void deleteEmployee(User user);
}