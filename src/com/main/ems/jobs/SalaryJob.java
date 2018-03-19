package main.ems.jobs;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import main.ems.model.Bonus;
import main.ems.model.Salaries;
import main.ems.model.Settings;
import main.ems.model.Taxes;
import main.ems.model.User;
import main.ems.service.BonusService;
import main.ems.service.EmployeeService;
import main.ems.service.PaySalariesService;
import main.ems.service.SettingsService;

@Component
public class SalaryJob {

    @Autowired
    EmployeeService employeeService;
    @Autowired
    SettingsService settingsService;
    @Autowired
    PaySalariesService paySalariesService;
    @Autowired
    BonusService bonusService;

    public SettingsService getSettingsService() {
        return settingsService;
    }

    public void setSettingsService(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    public EmployeeService getEmployeeService() {
        return employeeService;
    }

    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    public double calculateNetSalary(User user) {
        double salary = user.getSalary();
        for (Taxes tax : user.getTaxes()) {
            if (tax.getType().equals("%")) {
                if (tax.getType2().equals("+")) {
                    salary = salary + (salary * tax.getValue() / 100);
                } else {
                    salary = salary - (salary * tax.getValue() / 100);
                }
            } else {
                if (tax.getType2().equals("+")) {
                    salary += tax.getValue();
                } else {
                    salary -= tax.getValue();
                }
            }
        }
        return salary;
    }

    @Scheduled(cron = "0 0/2 * * * ?")
    public void paySalaries() {

        Settings settings = settingsService.getSetting(Settings.BILUNAR);
        String bilunar = settings.getValue();
        int firstDay = 0;
        int secondDay = 0;
        int day = 0;
        if (bilunar.equals("true")) {
            firstDay = Integer.parseInt(settingsService.getSetting(Settings.FIRST_DAY_OF_PAYMENT).getValue());
            secondDay = Integer.parseInt(settingsService.getSetting(Settings.SECOND_DAY_OF_PAYMENT).getValue());
        } else {
            day = Integer.parseInt(settingsService.getSetting(Settings.DAY_OF_PAYMENT).getValue());
        }

        int currentDayOfMonth;
        Calendar calendar = Calendar.getInstance();
        currentDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        if (bilunar.equals("true")) {
            if (currentDayOfMonth == firstDay) {
                System.out.println("Starting paying salaries:");
                pay(2);
            } else if (currentDayOfMonth == secondDay) {
                pay(2);
            }
        } else {
            if (currentDayOfMonth == day) {
                pay(1);
            }
        }
    }

    public void pay(int divide) {
        List<User> employees = employeeService.getEmployees();
        for (User emp : employees) {
            System.out.println("Employee name: " + emp.toString());
            Salaries salary = new Salaries();
            double toPay;
            double netSalary = this.calculateNetSalary(emp);
            toPay = netSalary / divide;
            System.out.println("Brut salary: " + emp.getSalary());
            System.out.println("Net salary: " + netSalary);
            System.out.println("To pay this time: " + toPay);

            List<Bonus> bonuses = bonusService.getUnpaidMoneyBonus(emp);

            for (Bonus bonus : bonuses) {
                toPay += bonus.getValue();
                System.out.println("--> Bonus: - Value:" + bonus.getValue());
                System.out.println("To pay: " + toPay);

                try {
                    bonus.setPaid("Y");
                    bonusService.updateBonus(bonus);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
            System.out.println("Total to pay: " + toPay);
            System.out.println("===========================================================");
            salary.setSalary(toPay);
            salary.setSalaryDate(new Date());
            salary.setUser(emp);
            try {
                paySalariesService.salaryPaid(salary);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public PaySalariesService getPaySalariesService() {
        return paySalariesService;
    }

    public void setPaySalariesService(PaySalariesService paySalariesService) {
        this.paySalariesService = paySalariesService;
    }

    public BonusService getBonusService() {
        return bonusService;
    }

    public void setBonusService(BonusService bonusService) {
        this.bonusService = bonusService;
    }
}
