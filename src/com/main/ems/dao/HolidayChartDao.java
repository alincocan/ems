package main.ems.dao;

import java.util.List;

import main.ems.model.User;

public interface HolidayChartDao {
    public List<User> getEmployeesFromDepartment(int depId, int manId);
}
