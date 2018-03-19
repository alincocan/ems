package main.ems.service;

import main.ems.model.User;

public interface UserService {

    public User getUserByUsernameAndPassword(String username, String password);

    public User getUserByUsername(String username);

    public Boolean isManager(int empId);

    public Boolean isHrUser(int empId);

}
