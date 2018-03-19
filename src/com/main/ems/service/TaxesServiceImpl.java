package main.ems.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import main.ems.dao.TaxesDao;
import main.ems.model.Taxes;
import main.ems.model.User;

@Service
public class TaxesServiceImpl implements TaxesService {

    @Autowired
    TaxesDao taxesDao;

    public TaxesDao getTaxesDao() {
        return taxesDao;
    }

    public void setTaxesDao(TaxesDao taxesDao) {
        this.taxesDao = taxesDao;
    }

    @Override
    @Transactional
    public void addOrUpdateFee(Taxes fee) {
        taxesDao.addOrUpdateFee(fee);

    }

    @Override
    @Transactional
    public void deleteFee(Taxes fee) {
        taxesDao.deleteFee(fee);

    }

    @Override
    @Transactional
    public List<Taxes> getAllTaxes(User selectedEmployee) {
        List<Taxes> taxes = taxesDao.getAllTaxes();
        List<Taxes> filteredTaxes = new ArrayList<Taxes>();
        boolean ok;
        for (Taxes tax : taxes) {
            ok = false;
            for (Taxes selTax : selectedEmployee.getTaxes()) {
                if (selTax.getId() == tax.getId()) {
                    ok = true;
                    continue;
                }
            }
            if (ok == false) {
                filteredTaxes.add(tax);
            }

        }
        return filteredTaxes;
    }

    @Override
    @Transactional
    public Taxes getFeeById(int id) {
        return taxesDao.getFeeById(id);
    }

    @Override
    @Transactional
    public List<Taxes> getAllTaxes() {
        return taxesDao.getAllTaxes();
    }
}