package ems;

import main.ems.dao.BonusDao;
import main.ems.model.*;
import main.ems.service.BonusService;
import main.ems.service.BonusServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.mockito.Mockito.*;

public class BonusTest  extends AbstractTest {

    @Mock
    private BonusDao bonusDao;

    @InjectMocks
    private BonusService bonusService = new BonusServiceImpl();

    private BonusType tickets;
    private BonusType money;
    private BonusType holidays;

    private final int TICKETS_ID = 1;
    private final int MONEY_ID = 2;
    private final int HOLIDAYS_ID = 3;


    /**
     * Creeaza cele 3 tipuri de bonusuri si authentifica un utilizator.
     */
    @Before
    public void setUp() {

        when(bonusDao.getBonusType(TICKETS_ID)).thenReturn(tickets);

        tickets = new BonusType();
        tickets.setId(1);
        tickets.setTypeName("Tickets");

        money = new BonusType();
        money.setId(2);
        money.setTypeName("Money");

        holidays = new BonusType();
        holidays.setId(1);
        holidays.setTypeName("Holidays");


        //authentication

        this.authenticateUser();

    }

    /**
     * Test pentru metoda care intoarce un bonus in functie de id
     */
    @Test
    public void getBonusTypeTest() {

        when(bonusDao.getBonusType(TICKETS_ID)).thenReturn(tickets);
        when(bonusDao.getBonusType(MONEY_ID)).thenReturn(money);
        when(bonusDao.getBonusType(HOLIDAYS_ID)).thenReturn(holidays);


        BonusType bonusType = bonusService.getBonusType(TICKETS_ID);

        Assert.assertNotNull(bonusType);
        Assert.assertEquals(bonusType.getId(), tickets.getId());
        Assert.assertEquals(bonusType.getTypeName(), tickets.getTypeName());

        bonusType = bonusService.getBonusType(MONEY_ID);

        Assert.assertNotNull(bonusType);
        Assert.assertEquals(bonusType.getId(), money.getId());
        Assert.assertEquals(bonusType.getTypeName(), money.getTypeName());

        bonusType = bonusService.getBonusType(HOLIDAYS_ID);

        Assert.assertNotNull(bonusType);
        Assert.assertEquals(bonusType.getId(), holidays.getId());
        Assert.assertEquals(bonusType.getTypeName(), holidays.getTypeName());

    }

    /**
     * Testeaza daca atunci cand se adauga un bonus fara un tip selectat(tichete de amsa, zile de concediu sau bani) see intoarce un mesaj de eroare corespunzator
     */
    @Test
    public void addBonusTestWithNoTypeSelected() {


        MockData data = new MockData(false,false,false,5);
        String result = bonusService.addBonus(data.getSelectedEmployee(), data.isMoney(),data.isTickets(),data.isHoliday(),data.getValue(),data.getDescription(),data.getCurrentUser());
        Assert.assertEquals(result, "Please choose a bonus type!");

    }


    /**
     * Testeaza daca atunci cand se adauga un bonus cu o valoare negative se intoarce un mesaj de eroare corespunzator
     */
    @Test
    public void addBonusTestNegativeValue() {

        MockData data = new MockData(true,false,false,-2);
        String result = bonusService.addBonus(data.getSelectedEmployee(), data.isMoney(),data.isTickets(),data.isHoliday(),data.getValue(),data.getDescription(),data.getCurrentUser());
        Assert.assertEquals(result, "The value can't be negative!");

    }

    /**
     * Testeaza adaugarea unui bonus de catre cel mai inalt manager de departament.
     * Deoarece acesta nu are alt sef, bonusul nu mai trebuie aprobat de nimeni, prin urmare trece direct in starea "Aprobat"
     */
    @Test
    public void addBonusIfCurrentUserHasNoParentDepartment() {

        doNothing().when(bonusDao).addBonus(any(Bonus.class));
        when(bonusDao.getBonusType(TICKETS_ID)).thenReturn(tickets);

        MockData data = new MockData(true,false,false,21.0);

        Assert.assertNotNull(data.getSelectedEmployee().getDepartment());
        Assert.assertNull(data.getSelectedEmployee().getDepartment().getParent());
        String result = bonusService.addBonus(data.getSelectedEmployee(), data.isMoney(),data.isTickets(),data.isHoliday(),data.getValue(),data.getDescription(),data.getCurrentUser());
        Assert.assertEquals(result, "Succes");

        ArgumentCaptor<Bonus> bonusCaptor = ArgumentCaptor.forClass(Bonus.class);
        verify(bonusDao, times(1)).addBonus(bonusCaptor.capture());

        Bonus capturedBonus = bonusCaptor.getValue();

        Assert.assertEquals(capturedBonus.getApproved(), "Y");
        Assert.assertEquals(capturedBonus.getDescription(), data.getDescription());
        Assert.assertNotNull(capturedBonus.getProponent());
        Assert.assertEquals(capturedBonus.getProponent().getUserId(), data.getCurrentUser().getUserId());
        Assert.assertEquals(capturedBonus.getValue(), data.getValue(),0);
        Assert.assertNotNull(capturedBonus.getBonusType());

        int bonusType = 0;

        if(data.isMoney()) {
            bonusType = 1;
        }
        if(data.isHoliday()) {
            bonusType = 2;
        }
        if(data.isTickets()) {
            bonusType = 3;
        }

        Assert.assertEquals(capturedBonus.getBonusType().getId(), bonusType);
        Assert.assertNotNull(capturedBonus.getEmployee());
        Assert.assertEquals(capturedBonus.getEmployee().getUserId(), data.getSelectedEmployee().getUserId());
        Assert.assertNull(capturedBonus.getCurrentState());

    }

    /**
     * Testeaza adaugarea unui bonus de catre un manager care are superiori.
     * Bonusul trebuie aprobat de superiorii managerului care a propus bonusul, deci acesta va avea setata valoarea "N" pentru campul approved.
     */
    @Test
    public void addBonusIfCurrentUserHasParentDepartment() {

        doNothing().when(bonusDao).addBonus(any(Bonus.class));
        when(bonusDao.getBonusType(TICKETS_ID)).thenReturn(tickets);

        MockData data = new MockData(true,false,false,21.0);

        Assert.assertNotNull(data.getSelectedEmployee().getDepartment());

        Department parent = new Department();
        parent.setDepartmentId(2);
        parent.setDepartmentName("parent");

        data.getCurrentUser().getDepartment().setParent(parent);

        Assert.assertNotNull(data.getCurrentUser().getDepartment().getParent());
        String result = bonusService.addBonus(data.getSelectedEmployee(), data.isMoney(),data.isTickets(),data.isHoliday(),data.getValue(),data.getDescription(),data.getCurrentUser());
        Assert.assertEquals(result, "Succes");

        ArgumentCaptor<Bonus> bonusCaptor = ArgumentCaptor.forClass(Bonus.class);
        verify(bonusDao, times(1)).addBonus(bonusCaptor.capture());

        Bonus capturedBonus = bonusCaptor.getValue();

        Assert.assertEquals(capturedBonus.getApproved(), "N");
        Assert.assertEquals(capturedBonus.getDescription(), data.getDescription());
        Assert.assertNotNull(capturedBonus.getProponent());
        Assert.assertEquals(capturedBonus.getProponent().getUserId(), data.getCurrentUser().getUserId());
        Assert.assertEquals(capturedBonus.getValue(), data.getValue(),0);
        Assert.assertNotNull(capturedBonus.getBonusType());

        int bonusType = 0;

        if(data.isMoney()) {
            bonusType = 1;
        }
        if(data.isHoliday()) {
            bonusType = 2;
        }
        if(data.isTickets()) {
            bonusType = 3;
        }

        Assert.assertEquals(capturedBonus.getBonusType().getId(), bonusType);
        Assert.assertNotNull(capturedBonus.getEmployee());
        Assert.assertEquals(capturedBonus.getEmployee().getUserId(), data.getSelectedEmployee().getUserId());

    }

    /**
     * Testeaza metoda care returneaza bonusurile pe care un manager trebuie sa le aprobe
     */

    @Test
    public void getBonusForApprovelTest() {

        List<Bonus> bonuses = new ArrayList();
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Assert.assertNotNull(currentUser);

        Department department = new Department();
        department.setDepartmentId(200);
        department.setDepartmentName("Department name");
        department.setManagerId(currentUser);

        currentUser.setDepartment(department);
        IntStream.range(0,10).forEach( iter -> {
            Bonus bonus = new Bonus();
            bonus.setCurrentState(currentUser);
            bonuses.add(bonus);
        });

        when(bonusDao.getBonusForApproval(currentUser.getUserId())).thenReturn(bonuses);

        List<Bonus> result = bonusService.getBonusForApproval(currentUser.getUserId());

        Assert.assertEquals(result.size(), 10);
        IntStream.range(0,10).forEach( iter -> {
            Assert.assertNotNull(bonuses.get(iter));
            Assert.assertNotNull(bonuses.get(iter).getCurrentState());
            Assert.assertEquals(bonuses.get(iter).getCurrentState().getUserId(), currentUser.getUserId());
        });

    }

    /**
     * Testeaza lista de bonusuri primite de un angajat
     */
    @Test
    public void getMyBonuses() {

        List<Bonus> bonuses = new ArrayList();
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Assert.assertNotNull(currentUser);


        IntStream.range(0,10).forEach( iter -> {
            Bonus bonus = new Bonus();
            bonus.setEmployee(currentUser);
            bonuses.add(bonus);
        });

        when(bonusDao.getMyBonus(currentUser.getUserId())).thenReturn(bonuses);

        List<Bonus> result = bonusService.getMyBonus(currentUser.getUserId());

        Assert.assertEquals(result.size(), 10);
        IntStream.range(0,10).forEach( iter -> {
            Assert.assertNotNull(bonuses.get(iter));
            Assert.assertNotNull(bonuses.get(iter).getEmployee());
            Assert.assertEquals(bonuses.get(iter).getEmployee().getUserId(), currentUser.getUserId());
        });

    }

    /**
     * Testeaza aprobarea unui bonus de catre cel mai inalt manager de departament.
     * Deoarece acesta nu are alt sef, bonusul nu mai trebuie aprobat de nimeni, prin urmare trece in starea "Aprobat"
     */
    @Test
    public void approveBonusTestByTheLastManager() {

        doNothing().when(bonusDao).updateBonus(any(Bonus.class));

        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Bonus bonus = new Bonus();
        bonusService.approveBonus(bonus, user);

        ArgumentCaptor<Bonus> bonusCaptor = ArgumentCaptor.forClass(Bonus.class);
        verify(bonusDao, times(1)).updateBonus(bonusCaptor.capture());

        Bonus capturedBonus = bonusCaptor.getValue();

        Assert.assertNull(capturedBonus.getCurrentState());
        Assert.assertEquals(capturedBonus.getApproved(), "Y");
    }

    /**
     * Testeaza aprobarea unui bonus de catre un manager care are superiori.
     * Bonusul trebuie aprobat si de de superiorii managerului curent, deci acesta va avea setata valoarea "N" pentru campul approved.
     */

    @Test
    public void approveBonusTest() {

        doNothing().when(bonusDao).updateBonus(any(Bonus.class));
        User manager = new User();
        manager.setUserId(1000);


        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Assert.assertNotNull(user.getDepartment());
        Department parent = new Department();
        parent.setDepartmentId(100);
        parent.setDepartmentName("Parent");
        parent.setManagerId(manager);

        user.getDepartment().setParent(parent);

        Bonus bonus = new Bonus();
        bonusService.approveBonus(bonus, user);

        ArgumentCaptor<Bonus> bonusCaptor = ArgumentCaptor.forClass(Bonus.class);
        verify(bonusDao, times(1)).updateBonus(bonusCaptor.capture());

        Bonus capturedBonus = bonusCaptor.getValue();

        Assert.assertNotNull(capturedBonus.getCurrentState());
        Assert.assertEquals(capturedBonus.getCurrentState().getUserId(), manager.getUserId());
        Assert.assertEquals(capturedBonus.getApproved(), "N");
    }
}


class MockData {

    private User selectedEmployee;
    private boolean money;
    private boolean tickets;
    private boolean holiday;
    private String description;
    private double value;
    private User currentUser;

    public MockData(boolean money, boolean tickets, boolean holiday, double value) {

        selectedEmployee = new User();
        selectedEmployee.setUserId(100);
        selectedEmployee.setUsername("test.test");
        selectedEmployee.setLastName("Test");
        selectedEmployee.setFirstName("Test");
        selectedEmployee.setEmail("test.test@email.com");
        selectedEmployee.setHdReceivedCurrentYear(21);
        selectedEmployee.setHdLastYear(10);
        selectedEmployee.setHdCurrentYear(10);

        Department department = new Department();

        department.setDepartmentId(1);
        department.setDepartmentName("Department");
        selectedEmployee.setDepartment(department);


        description = "Description";

        this.money = money;
        this.tickets = tickets;
        this.holiday = holiday;

        this.value = value;

        currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    }

    public User getSelectedEmployee() {
        return selectedEmployee;
    }

    public void setSelectedEmployee(User selectedEmployee) {
        this.selectedEmployee = selectedEmployee;
    }

    public boolean isMoney() {
        return money;
    }

    public void setMoney(boolean money) {
        this.money = money;
    }

    public boolean isTickets() {
        return tickets;
    }

    public void setTickets(boolean tickets) {
        this.tickets = tickets;
    }

    public boolean isHoliday() {
        return holiday;
    }

    public void setHoliday(boolean holiday) {
        this.holiday = holiday;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}