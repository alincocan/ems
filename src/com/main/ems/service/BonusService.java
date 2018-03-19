package main.ems.service;

import java.util.List;

import main.ems.model.Bonus;
import main.ems.model.BonusType;
import main.ems.model.User;

public interface BonusService {
    public List<User> getEmployees(int departmentId, int userId);

    public BonusType getBonusType(int id);

    public String addBonus( User selectedEmployee, boolean money, boolean holiday, boolean tickets, double value, String description, User user) ;

    public List<Bonus> getBonusForApproval(int userId);

    public void approveBonus(Bonus bonus, User user);

    public void updateBonus(Bonus bonus);

    public List<Bonus> getBonusProposed(int id);

    public List<Bonus> getBonusById(int id);

    public List<Bonus> getMyBonus(int id);

    public void updateEmployeeHolidayDays(User user);

    public List<Bonus> getUnpaidMoneyBonus(User emp);
}
