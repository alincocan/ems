package main.ems.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import main.ems.dao.EmployeeDao;
import main.ems.model.Role;
import main.ems.model.User;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    EmployeeDao employeeDao;

    public void setEmployeeDao(EmployeeDao employeeDao) {
        this.employeeDao = employeeDao;
    }

    @Override
    @Transactional
    public Role getRoleById(int roleId) {
        return employeeDao.getRoleById(roleId);
    }

    @Override
    @Transactional
    public User addEmployee(User employee, int role, String fileName , byte[] fileContent) {

        employee.setUsername(employee.getFirstName().toLowerCase() + "." + employee.getLastName().toLowerCase());
        employee.setHdReceivedCurrentYear(employee.getHdCurrentYear());
        employee.setHdLastYear(0);
        Role roleForUser = this.getRoleById(role);
        employee.setRole(roleForUser);
        employee.setAvatarName(fileName);
        employee.setAvatarContent(fileContent);

        return employeeDao.addEmployee(employee);
    }

    @Override
    @Transactional
    public List<User> getEmployees() {
        return employeeDao.getEmployees();
    }

    @Override
    @Transactional
    public Boolean isManager(int managerId) {
        return employeeDao.isManager(managerId);
    }

    @Override
    @Transactional
    public void editEmployee(User user) {
        employeeDao.editEmployee(user);
    }

    @Override
    @Transactional
    public void deleteEmployee(User user) {
        employeeDao.deleteEmployee(user);
    }
}