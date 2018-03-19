package main.ems.dao;

import java.util.List;

import main.ems.model.Taxes;

public interface TaxesDao {
    public void addOrUpdateFee(Taxes fee);

    public void deleteFee(Taxes fee);

    public List<Taxes> getAllTaxes();

    public Taxes getFeeById(int id);
}