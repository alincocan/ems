package main.ems.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import main.ems.dao.BonusDao;
import main.ems.model.Bonus;
import main.ems.model.BonusType;
import main.ems.model.User;

@Service
public class BonusServiceImpl implements BonusService {

    @Autowired
    BonusDao bonusDao;

    public BonusDao getBonusDao() {
        return bonusDao;
    }

    public void setBonusDao(BonusDao bonusDao) {
        this.bonusDao = bonusDao;
    }

    @Override
    @Transactional
    public List<User> getEmployees(int departmentId, int userId) {
        return bonusDao.getEmployees(departmentId, userId);
    }

    @Override
    @Transactional
    public BonusType getBonusType(int id) {
        return bonusDao.getBonusType(id);
    }

    @Override
    @Transactional
    public String addBonus( User selectedEmployee, boolean money, boolean holiday, boolean tickets, double value, String description, User user) {

        if (selectedEmployee == null) {
            return "Please select an employee!";
        }
        if (money == false && holiday == false && tickets == false) {
            return "Please choose a bonus type!";
        }

        if (value == 0) {
            return ("Please add a value!");
        }

        if (value < 0) {
            return ("The value can't be negative!");
        }
        if (description == null || description.isEmpty()) {
            return ("Please add a description!");
        }
        Bonus bonus = new Bonus();
        int bonusTypeId = 0;
        if (money == true) {
            bonusTypeId = 1;
        } else if (holiday == true) {
            bonusTypeId = 2;
        } else if (tickets == true) {
            bonusTypeId = 3;
        }


        BonusType bonusType =  bonusDao.getBonusType(bonusTypeId);
        if (bonusType == null) {
            return ("An error occured during adding operation. Please try again later!");
        }
        bonus.setBonusType(bonusType);
        if (bonus.getBonusType().getId() == 1) {
            bonus.setPaid("N");
        }
        if (user.getDepartment().getParent() == null) {
            bonus.setApproved("Y");
            bonus.setCurrentState(null);
            bonus.setApprovedDate(new Date());
        } else {
            bonus.setApproved("N");
            bonus.setCurrentState(user.getDepartment().getParent().getManagerId());
        }

        bonus.setProponent(user);

        if (holiday == true || tickets == true) {
            if ((value - (int) value) != 0) {
                return ("For holiday days and meal tickets the value has to be without decimals!");
            }
        }

        bonus.setEmployee(selectedEmployee);
        bonus.setBonusDate(new Date());
        bonus.setValue(value);
        bonus.setDescription(description);

        bonusDao.addBonus(bonus);

        return "Succes";
    }

    @Override
    @Transactional
    public List<Bonus> getBonusForApproval(int userId) {
        return bonusDao.getBonusForApproval(userId);
    }

    @Override
    @Transactional
    public void approveBonus(Bonus bonus, User user) {

        if (user.getDepartment().getParent() == null) {
            bonus.setApproved("Y");
            bonus.setCurrentState(null);
            bonus.setApprovedDate(new Date());
        } else {
            bonus.setApproved("N");
            bonus.setCurrentState(user.getDepartment().getParent().getManagerId());
        }
        bonusDao.updateBonus(bonus);
    }

    @Override
    @Transactional
    public void updateBonus(Bonus bonus) {
        bonusDao.updateBonus(bonus);
    }

    @Override
    @Transactional
    public List<Bonus> getBonusProposed(int id) {
        return bonusDao.getBonusProposed(id);
    }

    @Override
    @Transactional
    public List<Bonus> getBonusById(int id) {
        return bonusDao.getBonusById(id);
    }

    @Override
    @Transactional
    public List<Bonus> getMyBonus(int id) {
        return bonusDao.getMyBonus(id);
    }

    @Override
    @Transactional
    public void updateEmployeeHolidayDays(User user) {
        bonusDao.updateEmployeeHolidayDays(user);
    }

    @Override
    @Transactional
    public List<Bonus> getUnpaidMoneyBonus(User emp) {
        return bonusDao.getUnpaidMoneyBonus(emp);
    }
}
