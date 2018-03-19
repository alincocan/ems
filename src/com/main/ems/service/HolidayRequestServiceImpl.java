package main.ems.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import main.ems.dao.HolidayRequestDao;
import main.ems.model.HolidayRequest;
import main.ems.model.User;

@Service
public class HolidayRequestServiceImpl implements HolidayRequestService {

    @Autowired
    HolidayRequestDao holidayRequestDao;

    public void setHolidayRequestDao(HolidayRequestDao holidayRequestDao) {
        this.holidayRequestDao = holidayRequestDao;
    }

    int workDays;

    private boolean checkHolidayRequestPending(HolidayRequest holidayRequest) {
        Date now = new Date();
        return holidayRequest.getStartDate().before(now) && holidayRequest.getEndDate().after(now);
    }

    private boolean checkHolidayRequestEnded(HolidayRequest holidayRequest) {
        Date now = new Date();
        return holidayRequest.getEndDate().before(now);
    }

    private int getWorkDays(HolidayRequest holidayRequest) {
        workDays = 0;

        try {
            Calendar start = Calendar.getInstance();
            start.setTime(holidayRequest.getStartDate());

            Calendar end = Calendar.getInstance();
            end.setTime(holidayRequest.getEndDate());

            do {
                if (start.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && start.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                    workDays++;
                }
                start.add(Calendar.DAY_OF_MONTH, 1);
            } while (start.getTimeInMillis() <= end.getTimeInMillis());

        } catch (Exception e) {

        }
        return workDays;
    }

    private String verifyDate(HolidayRequest holidayRequest) {
        if (holidayRequest.getEndDate().before(holidayRequest.getStartDate())) {
            return "End date cannot be before start date!";
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MONTH, -1);
        Date limitDate = cal.getTime();

        if (holidayRequest.getStartDate().before(limitDate)) {
            return "Start date cannot be older then 1 month";
        }

        return null;
    }

    @Override
    @Transactional
    public List<User> getSubstitutes(int departmentId, int userId) {
        return holidayRequestDao.getSubstitutes(departmentId, userId);
    }

    @Override
    @Transactional
    public String addRequest(HolidayRequest holidayRequest) {
        int workDays = getWorkDays(holidayRequest);
        if (workDays == 0) {
            return "You're period has no work days";
        } else {
            holidayRequest.setDaysNumber(workDays);
        }
        String errMsg = verifyDate(holidayRequest);
        if (errMsg != null) {
            return errMsg;
        }


        if (checkHolidayRequestPending(holidayRequest)) {
            holidayRequest.setState(3);
        } else if (checkHolidayRequestEnded(holidayRequest)) {
            holidayRequest.setState(4);
        } else {
            holidayRequest.setState(0);
        }


        if (holidayRequest.getRequestType() == 1) {
            if (holidayRequest.getDaysNumber() > holidayRequest.getEmployee().getHdCurrentYear() + holidayRequest.getEmployee().getHdLastYear()) {
                return "You don't enough days for request";
            }
            if (holidayRequest.getEmployee().getHdLastYear() >= holidayRequest.getDaysNumber()) {
                holidayRequest.getEmployee().setHdLastYear(holidayRequest.getEmployee().getHdLastYear() - holidayRequest.getDaysNumber());
            } else if (holidayRequest.getEmployee().getHdLastYear() != 0) {
                int aux = holidayRequest.getEmployee().getHdLastYear() % holidayRequest.getDaysNumber();
                holidayRequest.getEmployee().setHdCurrentYear(holidayRequest.getEmployee().getHdCurrentYear() - (holidayRequest.getDaysNumber() - aux));
                holidayRequest.getEmployee().setHdLastYear(0);
            } else {
                holidayRequest.getEmployee().setHdCurrentYear(holidayRequest.getEmployee().getHdCurrentYear() - holidayRequest.getDaysNumber());
            }
        }
        holidayRequestDao.addRequest(holidayRequest);

        return "Succes";
    }

    @Override
    @Transactional
    public List<HolidayRequest> ownRequests(int empId) {
        return holidayRequestDao.ownRequests(empId);
    }

    @Override
    @Transactional
    public List<HolidayRequest> getRequestsForApproval(int empId) {
        return holidayRequestDao.getRequestsForApproval(empId);
    }

    @Override
    @Transactional
    public List<HolidayRequest> getRelatedRequests(int empId) {
        return holidayRequestDao.getRelatedRequests(empId);
    }

    @Override
    @Transactional
    public List<HolidayRequest> getHistory(int empId) {
        return holidayRequestDao.getHistory(empId);
    }

    @Override
    @Transactional
    public void cancelRequest(HolidayRequest holidayRequest) {
        if (holidayRequest.getRequestType() == 1) {
            if (holidayRequest.getDaysNumber() + holidayRequest.getEmployee().getHdCurrentYear() <= holidayRequest.getEmployee().getHdReceivedCurrentYear()) {
                holidayRequest.getEmployee().setHdCurrentYear(holidayRequest.getDaysNumber() + holidayRequest.getEmployee().getHdCurrentYear());
            } else {
                holidayRequest.getEmployee().setHdLastYear(holidayRequest.getEmployee().getHdCurrentYear() + holidayRequest.getEmployee().getHdLastYear() + holidayRequest.getDaysNumber() - holidayRequest.getEmployee().getHdReceivedCurrentYear());
                holidayRequest.getEmployee().setHdCurrentYear(holidayRequest.getEmployee().getHdReceivedCurrentYear());
            }
        }

        holidayRequestDao.cancelRequest(holidayRequest);
    }

    @Override
    @Transactional
    public HolidayRequest updateRequest(HolidayRequest holidayRequest) {
        return holidayRequestDao.updateRequest(holidayRequest);
    }
}