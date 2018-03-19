package main.ems.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import main.ems.model.Taxes;

@Repository
public class TaxesDaoImpl implements TaxesDao {

    @Autowired
    SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public void addOrUpdateFee(Taxes fee) {
        sessionFactory.getCurrentSession().saveOrUpdate(fee);
    }

    @Override
    public void deleteFee(Taxes fee) {
        sessionFactory.getCurrentSession().delete(fee);
    }

    @Override
    public List<Taxes> getAllTaxes() {
        String hql = "From Taxes";
        return sessionFactory.getCurrentSession().createQuery(hql).list();
    }

    @Override
    public Taxes getFeeById(int id) {
        String hql = "From Taxes where id = :id";
        List<Taxes> taxes = sessionFactory.getCurrentSession().createQuery(hql).setParameter("id", id).list();
        if (taxes.size() > 0) {
            return taxes.get(0);
        } else {
            return new Taxes();
        }
    }
}