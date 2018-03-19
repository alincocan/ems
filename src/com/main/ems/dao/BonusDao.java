package main.ems.dao;

import java.util.List;

import main.ems.model.Bonus;
import main.ems.model.BonusType;
import main.ems.model.User;

public interface BonusDao {
    public List<User> getEmployees(int departmentId, int userId);

    public BonusType getBonusType(int id);

    public void addBonus(Bonus bonus);

    public List<Bonus> getBonusForApproval(int userId);

    public void updateBonus(Bonus bonus);

    public List<Bonus> getBonusProposed(int id);

    public List<Bonus> getBonusById(int id);

    public List<Bonus> getMyBonus(int id);

    public void updateEmployeeHolidayDays(User user);

    public List<Bonus> getUnpaidMoneyBonus(User emp);
}
