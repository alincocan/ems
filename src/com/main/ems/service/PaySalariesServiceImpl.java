package main.ems.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import main.ems.dao.PaySalariesDao;
import main.ems.model.Salaries;

@Service
public class PaySalariesServiceImpl implements PaySalariesService {

    @Autowired
    PaySalariesDao paySalariesDao;

    public PaySalariesDao getPaySalariesDao() {
        return paySalariesDao;
    }

    public void setPaySalariesDao(PaySalariesDao paySalariesDao) {
        this.paySalariesDao = paySalariesDao;
    }

    @Override
    @Transactional
    public void salaryPaid(Salaries salary) {
        paySalariesDao.salaryPaid(salary);
    }

    @Override
    @Transactional
    public List<Date> getDistinctDatesForPaidSalaries() {
        return paySalariesDao.getDistinctDatesForPaidSalaries();
    }

    @Override
    @Transactional
    public List<Salaries> getSalariesByDate(Date date) {
        return paySalariesDao.getSalariesByDate(date);
    }

    @Override
    @Transactional
    public List<Salaries> getMyPaidSalaries(int id) {
        return paySalariesDao.getMyPaidSalaries(id);
    }
}
