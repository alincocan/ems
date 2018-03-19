package main.ems.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import main.ems.model.HolidayRequest;
import main.ems.model.User;

@Repository
public class HolidayRequestDaoImpl implements HolidayRequestDao {

    @Autowired
    SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<User> getSubstitutes(int departmentId, int userId) {
        String hql = "From User where department.departmentId = :dep and userId != :us";
        return sessionFactory.getCurrentSession().createQuery(hql).setParameter("dep", departmentId).setParameter("us", userId).list();
    }

    @Override
    public void addRequest(HolidayRequest holidayRequest) {
        sessionFactory.getCurrentSession().merge(holidayRequest);
    }

    @Override
    public List<HolidayRequest> ownRequests(int empId) {
        String hql = "From HolidayRequest where employee.userId = :id order by requestDate desc";
        return sessionFactory.getCurrentSession().createQuery(hql).setParameter("id", empId).list();
    }

    @Override
    public List<HolidayRequest> getRequestsForApproval(int empId) {
        //String hql = "FROM HolidayRequest where (substitute.department.managerId.userId = :id and state = :st1)";
        String hql = "FROM HolidayRequest where (substitute.userId = :id and state = :st1) or (substitute.department.managerId.userId = :id and state = :st2) order by requestDate desc";
        return sessionFactory.getCurrentSession().createQuery(hql).setParameter("id", empId).setParameter("st1", HolidayRequest.WAITING).setParameter("st2", HolidayRequest.APPROVED_BY_SUBSTITUTE).list();
    }

    @Override
    public List<HolidayRequest> getRelatedRequests(int empId) {
        String hql = "FROM HolidayRequest where (substitute.userId = :id and state != :st1 and state <= :st3) or (substitute.department.managerId.userId = :id and state != :st2 and state < :st3) order by requestDate desc";
        return sessionFactory.getCurrentSession().createQuery(hql).setParameter("id", empId).setParameter("st1", HolidayRequest.WAITING).setParameter("st3", HolidayRequest.PENDING).setParameter("st2", HolidayRequest.APPROVED_BY_SUBSTITUTE).list();
    }

    @Override
    public List<HolidayRequest> getHistory(int empId) {
        String hql = "FROM HolidayRequest where (substitute.userId = :id and state > :st1) or (substitute.department.managerId.userId = :id and state > :st1) order by requestDate desc";
        return sessionFactory.getCurrentSession().createQuery(hql).setParameter("id", empId).setParameter("st1", HolidayRequest.PENDING).list();

    }

    @Override
    public void cancelRequest(HolidayRequest holidayRequest) {
        sessionFactory.getCurrentSession().merge(holidayRequest.getEmployee());
        sessionFactory.getCurrentSession().delete(holidayRequest);
    }

    @Override
    public HolidayRequest updateRequest(HolidayRequest holidayRequest) {
        sessionFactory.getCurrentSession().update(holidayRequest);
        return holidayRequest;
    }
}