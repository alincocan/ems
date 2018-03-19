package main.ems.service;

import java.util.List;

import main.ems.model.HolidayRequest;
import main.ems.model.User;

public interface HolidayRequestService {
    public List<User> getSubstitutes(int departmentId, int userId);

    public String addRequest(HolidayRequest holidayRequest);

    public List<HolidayRequest> ownRequests(int empId);

    public List<HolidayRequest> getRequestsForApproval(int empId);

    public List<HolidayRequest> getHistory(int empId);

    public List<HolidayRequest> getRelatedRequests(int empId);

    public void cancelRequest(HolidayRequest holidayRequest);

    public HolidayRequest updateRequest(HolidayRequest holidayRequest);
}