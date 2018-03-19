package main.ems.dao;

import java.util.List;

import main.ems.model.Role;
import main.ems.model.User;

public interface EmployeeDao {
    public Role getRoleById(int roleId);

    public User addEmployee(User user);

    public List<User> getEmployees();

    public Boolean isManager(int managerId);

    public void editEmployee(User user);

    public void deleteEmployee(User user);
}