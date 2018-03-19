package main.ems.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import main.ems.dao.UserDao;
import main.ems.model.User;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    @Transactional
    public User getUserByUsernameAndPassword(String username, String password) {
        return userDao.getUserByUsernameAndPassword(username, password);
    }

    @Override
    @Transactional
    public User getUserByUsername(String username) {
        return userDao.getUserByUsername(username);
    }

    @Override
    @Transactional
    public Boolean isManager(int empId) {
        return userDao.isManager(empId);
    }

    @Override
    @Transactional
    public Boolean isHrUser(int empId) {
        return userDao.isHrUser(empId);
    }
}
