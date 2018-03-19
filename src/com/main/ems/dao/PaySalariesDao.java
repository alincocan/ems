package main.ems.dao;

import java.util.Date;
import java.util.List;

import main.ems.model.Salaries;

public interface PaySalariesDao {
    public void salaryPaid(Salaries salary);

    public List<Date> getDistinctDatesForPaidSalaries();

    public List<Salaries> getSalariesByDate(Date date);

    public List<Salaries> getMyPaidSalaries(int id);
}
