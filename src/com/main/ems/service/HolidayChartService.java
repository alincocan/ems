package main.ems.service;

import java.util.List;

import main.ems.model.User;

public interface HolidayChartService {
    public List<User> getEmployeesFromDepartment(int depId, int manId);

}
