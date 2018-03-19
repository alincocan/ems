package main.ems.service;

import java.util.List;

import main.ems.model.Taxes;
import main.ems.model.User;

public interface TaxesService {

    public void addOrUpdateFee(Taxes fee);

    public void deleteFee(Taxes fee);

    public List<Taxes> getAllTaxes(User selectedEmployee);

    public List<Taxes> getAllTaxes();

    public Taxes getFeeById(int id);
}