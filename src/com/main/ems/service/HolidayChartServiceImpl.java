package main.ems.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import main.ems.dao.HolidayChartDao;
import main.ems.model.User;

@Service
public class HolidayChartServiceImpl implements HolidayChartService {
    @Autowired
    HolidayChartDao holidayChartDao;


    public HolidayChartDao getHolidayChartDao() {
        return holidayChartDao;
    }


    public void setHolidayChartDao(HolidayChartDao holidayChartDao) {
        this.holidayChartDao = holidayChartDao;
    }

    @Override
    @Transactional
    public List<User> getEmployeesFromDepartment(int depId, int manId) {
        return holidayChartDao.getEmployeesFromDepartment(depId, manId);
    }
}
