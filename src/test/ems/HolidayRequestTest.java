package ems;

import main.ems.dao.HolidayRequestDao;
import main.ems.model.Department;
import main.ems.model.HolidayRequest;
import main.ems.model.User;
import main.ems.service.HolidayRequestService;
import main.ems.service.HolidayRequestServiceImpl;
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
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;
import java.util.stream.IntStream;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

public class HolidayRequestTest extends AbstractTest {

    @Mock
    HolidayRequestDao holidayRequestDao;

    @InjectMocks
    HolidayRequestService holidayRequestService = new HolidayRequestServiceImpl();




    @Before
    public void setUp() {
        this.authenticateUser();

    }

    /**
     * Test pentru metoda care intoarce lista de inlocuitori
     */

    @Test
    public void getSubstitutesTest() {
        List<User> substitues = new ArrayList<>();

        IntStream.range(0, 10).forEach(iter -> {
            User user = new User();
            user.setUsername("username " + iter);
            substitues.add(user);
        });

        when(holidayRequestDao.getSubstitutes(anyInt(), anyInt())).thenReturn(substitues);

        List<User> result = holidayRequestService.getSubstitutes(1, 1);
        Assert.assertEquals(substitues.size(), 10);

        IntStream.range(0, 10).forEach(iter -> {
            Assert.assertNotNull(result.get(iter));
            Assert.assertEquals(result.get(iter).getUsername(), "username " + iter);
        });
    }

    /**
     * Testeaza daca atunci cand se adauga o cerere de concediu fara nicio zi lucratoare se intoarce un mesaj de eroare corespunzator
     */
    @Test
    public void addRequestWithNoDays() {

        doNothing().when(holidayRequestDao).addRequest(any(HolidayRequest.class));
        HolidayRequest holidayRequest = new HolidayRequest();

        Calendar start = new GregorianCalendar(2018,Calendar.MARCH,18);
        Calendar end = new GregorianCalendar(2018,Calendar.MARCH,18);

        holidayRequest.setStartDate(start.getTime());
        holidayRequest.setEndDate(end.getTime());
        String result = holidayRequestService.addRequest(holidayRequest);

        Assert.assertEquals(result , "You're period has no work days");

    }

    /**
     * testeaza daca atunci cand se adauga o cerere de concediu a carei data de start este mai veche de o luna se intoarce un  mesaj de eroare corespunzator
     */
    @Test
    public void addRequestWithStartOlderThenOneMonth() {

        doNothing().when(holidayRequestDao).addRequest(any(HolidayRequest.class));

        HolidayRequest holidayRequest = new HolidayRequest();

        holidayRequest.setStartDate(new GregorianCalendar(2018,Calendar.JANUARY,18).getTime());
        holidayRequest.setEndDate(new GregorianCalendar(2018,Calendar.MARCH,17).getTime());

        String result = holidayRequestService.addRequest(holidayRequest);
        Assert.assertEquals(result , "Start date cannot be older then 1 month");

    }

    /**
     * Testeaza daca atunci cand utilizatorul care adauga cererea de concediu nu are suficiente zile disponibile se intoarce un mesaj de eroare corespunzator
     */

    @Test
    public void addRequestWithNoHD() {

        doNothing().when(holidayRequestDao).addRequest(any(HolidayRequest.class));

        HolidayRequest holidayRequest = new HolidayRequest();

        holidayRequest.setStartDate(new GregorianCalendar(2018,Calendar.MARCH,18).getTime());
        holidayRequest.setEndDate(new GregorianCalendar(2018,Calendar.MARCH,30).getTime());


        User user = new User();
        user.setHdReceivedCurrentYear(21);
        user.setHdCurrentYear(2);
        user.setHdLastYear(0);
        holidayRequest.setEmployee(user);
        holidayRequest.setRequestType(1);
        String result = holidayRequestService.addRequest(holidayRequest);
        Assert.assertEquals(result, "You don't enough days for request");

    }

    /**
     * Testeaza corectiudinea adaugarii unei cereri de concediu deja in curs
     */

    @Test
    public void addPendingRequest() {

        doNothing().when(holidayRequestDao).addRequest(any(HolidayRequest.class));

        HolidayRequest holidayRequest = this.createHolidayRequest();

        Calendar calendar = new GregorianCalendar();

        holidayRequest.setStartDate(new GregorianCalendar(2018,Calendar.MARCH,18).getTime());

        holidayRequest.setEndDate(new GregorianCalendar(calendar.get(Calendar.YEAR) + 1,Calendar.MARCH,3).getTime());
        holidayRequest.setRequestType(2);

        String result = holidayRequestService.addRequest(holidayRequest);

        Assert.assertEquals(result, "Succes");

        ArgumentCaptor<HolidayRequest> hdCaptor = ArgumentCaptor.forClass(HolidayRequest.class);
        verify(holidayRequestDao, times(1)).addRequest(hdCaptor.capture());

        HolidayRequest capturedHd = hdCaptor.getValue();

        Assert.assertEquals(capturedHd.getState(),3);

    }

    /**
     * Testeaza corectitudinea adaugarii unei cereri de concediu pentru care este necesara aprobarea de catre inlocuitor si manager
     */
    @Test
    public void addRequest() {

        doNothing().when(holidayRequestDao).addRequest(any(HolidayRequest.class));

        HolidayRequest holidayRequest = this.createHolidayRequest();

        Calendar start = new GregorianCalendar();
        start.add(Calendar.DAY_OF_WEEK, 1);

        Calendar end = new GregorianCalendar();
        end.add(Calendar.DAY_OF_WEEK,6);

        holidayRequest.setStartDate(start.getTime());
        holidayRequest.setEndDate(end.getTime());
        holidayRequest.setRequestType(1);

        String result = holidayRequestService.addRequest(holidayRequest);

        Assert.assertEquals(result, "Succes");

        ArgumentCaptor<HolidayRequest> hdCaptor = ArgumentCaptor.forClass(HolidayRequest.class);
        verify(holidayRequestDao, times(1)).addRequest(hdCaptor.capture());

        HolidayRequest capturedHd = hdCaptor.getValue();

        Assert.assertEquals(capturedHd.getState(),0);

    }

    /**
     * Testeaza metodaca care returneaza lista cu propriile cereri de concediu
     */
    @Test
    public void getOwnRequests() {

        User currentUser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Assert.assertNotNull(currentUser);
        List<HolidayRequest> ownHolidayRequests = new ArrayList();

        IntStream.range(0,10).forEach(iter -> {
            HolidayRequest holidayRequest = this.createHolidayRequest();
            holidayRequest.setEmployee(currentUser);
            ownHolidayRequests.add(holidayRequest);
        });

        when(holidayRequestDao.ownRequests(currentUser.getUserId())).thenReturn(ownHolidayRequests);

        List<HolidayRequest> resultRequests = holidayRequestService.ownRequests(currentUser.getUserId());

        Assert.assertEquals(resultRequests.size(), 10);

        resultRequests.stream().forEach(request -> {
            Assert.assertNotNull(request.getEmployee());
            Assert.assertEquals(request.getEmployee().getUserId(), currentUser.getUserId());
        });

    }

    /**
     * Testeaza metoda care returneaza lista istoricului propriilor cereri de concediu pentru un angajat(cereri de concediu pentru care fluxul este incheiat)
     */
    @Test
    public void getHistoryForUserTest() {
        User currentUser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Assert.assertNotNull(currentUser);
        List<HolidayRequest> historyRequests = new ArrayList();

        IntStream.range(0,10).forEach(iter -> {
            HolidayRequest holidayRequest = this.createHolidayRequest();
            holidayRequest.setSubstitute(currentUser);
            holidayRequest.setState(HolidayRequest.REJECTED_BY_SUBSTITUTE);
            historyRequests.add(holidayRequest);
        });

        when(holidayRequestDao.getHistory(currentUser.getUserId())).thenReturn(historyRequests);

        List<HolidayRequest> resultRequests = holidayRequestService.getHistory(currentUser.getUserId());

        Assert.assertEquals(resultRequests.size(), 10);

        resultRequests.stream().forEach(request -> {
            Assert.assertNotNull(request.getEmployee());
            Assert.assertEquals(request.getSubstitute().getUserId(), currentUser.getUserId());
            Assert.assertNotEquals(request.getSubstitute(), HolidayRequest.PENDING);
        });

    }

    /**
     * Testeaza metoda care returneaza lista istoricului propriilor cereri de concediu pentru un manager de departament
     */
    @Test
    public void getHistoryForManagerTest() {
        User currentUser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Assert.assertNotNull(currentUser);
        List<HolidayRequest> historyRequests = new ArrayList();

        /**
         * Create substitute
         */
        User user = new User();
        user.setUserId(1);
        user.setUsername("test.test");

        Department department = new Department();
        department.setDepartmentId(100);
        department.setDepartmentName("Department");

        /**
         * Set current user as manager for substitute's department
         */
        department.setManagerId(currentUser);

        user.setDepartment(department);

        IntStream.range(0,10).forEach(iter -> {
            HolidayRequest holidayRequest = this.createHolidayRequest();
            holidayRequest.setSubstitute(user);
            holidayRequest.setState(HolidayRequest.FINISHED);
            historyRequests.add(holidayRequest);
        });

        when(holidayRequestDao.getHistory(currentUser.getUserId())).thenReturn(historyRequests);

        List<HolidayRequest> resultRequests = holidayRequestService.getHistory(currentUser.getUserId());

        Assert.assertEquals(resultRequests.size(), 10);

        resultRequests.stream().forEach(request -> {
            Assert.assertNotNull(request.getEmployee());
            Assert.assertNotNull(request.getSubstitute());
            Assert.assertNotNull(request.getSubstitute().getDepartment());
            Assert.assertNotNull(request.getSubstitute().getDepartment().getManagerId());
            Assert.assertEquals(request.getSubstitute().getDepartment().getManagerId().getUserId(), currentUser.getUserId());
            Assert.assertNotEquals(request.getSubstitute(), HolidayRequest.PENDING);
        });
    }

    /**
     * Testeaza metoda care returneza lista de cereri de concediu ce trebuiesc aprobate
     */
    @Test
    public void getRequestsForApproval() {

        User currentUser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Assert.assertNotNull(currentUser);

        List<HolidayRequest> holidayRequests = new ArrayList();

        IntStream.range(0,10).forEach(iter -> {
            HolidayRequest holidayRequest = this.createHolidayRequest();
            holidayRequest.setSubstitute(currentUser);
            holidayRequests.add(holidayRequest);
        });

        when(holidayRequestDao.getRequestsForApproval(currentUser.getUserId())).thenReturn(holidayRequests);

        List<HolidayRequest> resultRequests = holidayRequestDao.getRequestsForApproval(currentUser.getUserId());

        Assert.assertEquals(resultRequests.size(), 10);

        resultRequests.stream().forEach(request -> {
            Assert.assertNotNull(request.getSubstitute());
            Assert.assertEquals(request.getSubstitute().getUserId(), currentUser.getUserId());
        });

    }

    /**
     * Intoarce lista de cereri de concediu in care utilziatorul este implicat dar pentru care momentan nu are disponibila nicio actiune
     */
    @Test
    public void getRelatedRequests() {

        User currentUser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Assert.assertNotNull(currentUser);

        List<HolidayRequest> holidayRequests = new ArrayList();

        IntStream.range(0,10).forEach(iter -> {
            HolidayRequest holidayRequest = this.createHolidayRequest();
            holidayRequest.setSubstitute(currentUser);
            holidayRequest.setState(2);
            holidayRequests.add(holidayRequest);
        });

        when(holidayRequestDao.getRelatedRequests(currentUser.getUserId())).thenReturn(holidayRequests);

        List<HolidayRequest> resultRequests = holidayRequestDao.getRelatedRequests(currentUser.getUserId());

        Assert.assertEquals(resultRequests.size(), 10);

        resultRequests.stream().forEach(request -> {
            Assert.assertNotNull(request.getSubstitute());
            Assert.assertEquals(request.getSubstitute().getUserId(), currentUser.getUserId());

            //check request state
            Assert.assertNotEquals(request.getState(), HolidayRequest.WAITING);
            Assert.assertNotEquals(request.getState(), HolidayRequest.PENDING);
            Assert.assertNotEquals(request.getState(), HolidayRequest.FINISHED);
            Assert.assertNotEquals(request.getState(), HolidayRequest.REJECTED_BY_SUBSTITUTE);
            Assert.assertNotEquals(request.getState(), HolidayRequest.REJECTED_BY_MANAGER)
            ;
        });

    }

    /**
     * Testeaza metoda care anuleaza o cerere de concediu fara plata
     * Se verifica ca utilizatorul nu primeste inapoi nici o zi de concediu
     */
    @Test
    public void cancelRequestPaidDaysOff () {

        HolidayRequest holidayRequest = this.createHolidayRequest();
        holidayRequest.setDaysNumber(3);
        holidayRequest.setRequestType(2);

        Assert.assertNotNull(holidayRequest.getEmployee());
        doNothing().when(holidayRequestDao).cancelRequest(any(HolidayRequest.class));

        holidayRequestService.cancelRequest(holidayRequest);

        ArgumentCaptor<HolidayRequest> hdCaptor = ArgumentCaptor.forClass(HolidayRequest.class);
        verify(holidayRequestDao, times(1)).cancelRequest(hdCaptor.capture());

        HolidayRequest capturedHd = hdCaptor.getValue();

        Assert.assertNotNull(capturedHd.getEmployee());

        Assert.assertEquals(capturedHd.getEmployee().getHdCurrentYear(), holidayRequest.getEmployee().getHdCurrentYear());
        Assert.assertEquals(capturedHd.getEmployee().getHdLastYear(), holidayRequest.getEmployee().getHdLastYear());

    }

    /**
     * Testeaza anularea unei cereri de concediu
     * Se verifica ca utilizatorul primeste inapoi zilele de concediu.
     */
    @Test
    public void cancelHolidayRequest () {

        HolidayRequest holidayRequest = this.createHolidayRequest();
        holidayRequest.setDaysNumber(3);
        holidayRequest.setRequestType(1);

        //user to compare the output
        User employee = new User();
        employee.setHdCurrentYear(holidayRequest.getEmployee().getHdCurrentYear());
        employee.setHdLastYear(holidayRequest.getEmployee().getHdLastYear());
        employee.setHdReceivedCurrentYear(holidayRequest.getEmployee().getHdReceivedCurrentYear());
        Assert.assertNotNull(holidayRequest.getEmployee());
        doNothing().when(holidayRequestDao).cancelRequest(any(HolidayRequest.class));

        holidayRequestService.cancelRequest(holidayRequest);

        ArgumentCaptor<HolidayRequest> hdCaptor = ArgumentCaptor.forClass(HolidayRequest.class);
        verify(holidayRequestDao, times(1)).cancelRequest(hdCaptor.capture());

        HolidayRequest capturedHd = hdCaptor.getValue();

        Assert.assertNotNull(capturedHd.getEmployee());

        if (holidayRequest.getDaysNumber() + holidayRequest.getEmployee().getHdCurrentYear() <= holidayRequest.getEmployee().getHdReceivedCurrentYear()) {
              Assert.assertEquals(capturedHd.getEmployee().getHdCurrentYear(),holidayRequest.getDaysNumber() + employee.getHdCurrentYear());
        } else {
            Assert.assertEquals(capturedHd.getEmployee().getHdLastYear(), employee.getHdCurrentYear() + employee.getHdLastYear() + holidayRequest.getDaysNumber() - employee.getHdReceivedCurrentYear());
            Assert.assertEquals(capturedHd.getEmployee().getHdCurrentYear(), employee.getHdReceivedCurrentYear());
        }


    }


    /**
     * Testeaza metoda care aproba o cerere de concediu.
     */
    @Test
    public void approveRequest() {

        HolidayRequest holidayRequest = this.createHolidayRequest();

        Assert.assertNotNull(holidayRequest.getEmployee());

        HolidayRequest result = this.createHolidayRequest();
        result.setState(result.getState() + 1);

         when(holidayRequestDao.updateRequest(any(HolidayRequest.class))).thenReturn(result);
        HolidayRequest resultHD = holidayRequestService.updateRequest(holidayRequest);

        ArgumentCaptor<HolidayRequest> hdCaptor = ArgumentCaptor.forClass(HolidayRequest.class);
        verify(holidayRequestDao, times(1)).updateRequest(hdCaptor.capture());

        HolidayRequest capturedHd = hdCaptor.getValue();


        Assert.assertEquals(resultHD.getState(), holidayRequest.getState() + 1);

    }

    /**
     * Testeaza metoda care respinge o cerere de concediu
     */
    @Test
    public void rejectRequest() {

        HolidayRequest holidayRequest = this.createHolidayRequest();

        Assert.assertNotNull(holidayRequest.getEmployee());

        HolidayRequest result = this.createHolidayRequest();
        result.setState(result.getState() + 5);


        when(holidayRequestDao.updateRequest(any(HolidayRequest.class))).thenReturn(result);
        HolidayRequest resultHD = holidayRequestService.updateRequest(holidayRequest);

        ArgumentCaptor<HolidayRequest> hdCaptor = ArgumentCaptor.forClass(HolidayRequest.class);
        verify(holidayRequestDao, times(1)).updateRequest(hdCaptor.capture());

        HolidayRequest capturedHd = hdCaptor.getValue();


        Assert.assertEquals(capturedHd, holidayRequest);
        Assert.assertEquals(resultHD.getState(), holidayRequest.getState() + 5);


    }

    private HolidayRequest createHolidayRequest() {
        HolidayRequest holidayRequest = new HolidayRequest();
        User user = new User();
        user.setHdReceivedCurrentYear(21);
        user.setHdCurrentYear(21);
        user.setHdLastYear(10);
        holidayRequest.setEmployee(user);
        holidayRequest.setRequestType(1);
        return holidayRequest;
    }
}
