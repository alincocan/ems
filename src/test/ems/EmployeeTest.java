package ems;

import main.ems.dao.EmployeeDao;
import main.ems.model.Role;
import main.ems.model.User;
import main.ems.service.EmployeeServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;


public class EmployeeTest extends AbstractTest{

    private final int  USER = 1;
    private final int HR_USER = 2;

    private final int IS_MANAGER = 1;
    private final int IS_NOT_MANAGER = 2;


    @Mock
    private EmployeeDao employeeDao;

    @InjectMocks
    EmployeeServiceImpl employeeService = new EmployeeServiceImpl();

    Role role;
    @Before
    public void setUp() {

        Assert.assertNotNull(employeeDao);

//        employeeService.setEmployeeDao(employeeDao);
        role = new Role();
        role.setRoleId(1);
        role.setRoleName("User");

    }


    /**
     * Test pentru adaugarea unui angajat
     */
    @Test
    public void saveEmployee () {


        when(employeeDao.addEmployee(any(User.class))).thenReturn(new User());
        when(employeeDao.getRoleById(1)).thenReturn(role);
        User inputUser = this.createaUser();
        employeeService.addEmployee(inputUser, 1, "fileName", null);
//        verify(employeeDao).addEmployee(inputUser);
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(employeeDao, times(1)).addEmployee(userCaptor.capture());

        User capturedUser = userCaptor.getValue();
        Assert.assertEquals(capturedUser.getUsername(), capturedUser.getFirstName().toLowerCase() + "." + capturedUser.getLastName().toLowerCase());
        Assert.assertEquals(capturedUser.getHdReceivedCurrentYear(), capturedUser.getHdCurrentYear());
        Assert.assertEquals( capturedUser.getHdLastYear(),0);
        Assert.assertEquals(capturedUser.getRole().getRoleId(), 1);

    }

    /**
     * Test pentru metoda care intoarce un rol in functie de id
     */

    @Test
    public void getRole() {
        Role userRole = new Role();
        userRole.setRoleId(1);
        userRole.setRoleName("User");

        Role hrRole = new Role();
        hrRole.setRoleId(2);
        hrRole.setRoleName("HR");


        when(employeeDao.getRoleById(USER)).thenReturn(userRole);
        when(employeeDao.getRoleById(HR_USER)).thenReturn(hrRole);

        Role resultUserRole = employeeService.getRoleById(USER);

        Assert.assertNotNull(resultUserRole);
        Assert.assertEquals(resultUserRole.getRoleId(), USER);
        Assert.assertEquals(resultUserRole.getRoleName(), userRole.getRoleName());

        Role resultHrRole = employeeService.getRoleById(HR_USER);

        Assert.assertNotNull(resultHrRole);
        Assert.assertEquals(resultHrRole.getRoleId(), HR_USER);
        Assert.assertEquals(resultHrRole.getRoleName(), hrRole.getRoleName());

    }

    /**
     * Test care verifica daca un utilizator este manager de departament
     */
    @Test
    public void isManagerTest() {

        when(employeeDao.isManager(IS_MANAGER)).thenReturn(true);
        when(employeeDao.isManager(IS_NOT_MANAGER)).thenReturn(false);

        boolean resultIsManager = employeeService.isManager(IS_MANAGER);
        Assert.assertEquals(resultIsManager, true);

        boolean resultIsNotManager = employeeService.isManager(IS_NOT_MANAGER);
        Assert.assertEquals(resultIsNotManager, false);

    }

    /**
     * Test pentru metoda care intoarce lista angajatilor
     */
    @Test
    public void getEmployeesTest() {

        List<User> employees = new ArrayList<>();

        IntStream.range(0, 10).forEach(iter -> {
            User user = this.createaUser();
            user.setUsername(user.getUsername() + " " + iter);
            user.setUserId(iter);

            employees.add(user);
        });

        when(employeeDao.getEmployees()).thenReturn(employees);

        List<User> result = employeeService.getEmployees();

        IntStream.range(0, 10).forEach(iter -> {

            Assert.assertNotNull(employees.get(iter));
            Assert.assertEquals(iter, employees.get(iter).getUserId());

        });
    }

    /**
     * test pentru editarea unui angajat
     */
    @Test
    public void editEmployee() {

        User employee = this.createaUser();

        doNothing().when(employeeDao).editEmployee(any(User.class));

        employeeService.editEmployee(employee);

        ArgumentCaptor<User> employeeCaptor = ArgumentCaptor.forClass(User.class);
        verify(employeeDao, times(1)).editEmployee(employeeCaptor.capture());

        User capturedEmployee = employeeCaptor.getValue();

        Assert.assertEquals(employee.getFirstName(), capturedEmployee.getFirstName());
        Assert.assertEquals(employee.getLastName(), capturedEmployee.getLastName());
        Assert.assertEquals(employee.getHdCurrentYear(), capturedEmployee.getHdCurrentYear());
        Assert.assertEquals(employee.getHdReceivedCurrentYear(), capturedEmployee.getHdReceivedCurrentYear());
        Assert.assertEquals(employee.getSalary(), capturedEmployee.getSalary(),0);
        Assert.assertEquals(employee.getEmail(), capturedEmployee.getEmail());
        Assert.assertEquals(employee.getUsername(), capturedEmployee.getUsername());

    }

    /**
     * Test pentru stergerea unui anjat
     */
    @Test
    public void deleteEmployeeTest() {

        User employee = this.createaUser();

        doNothing().when(employeeDao).deleteEmployee(any(User.class));

        employeeService.deleteEmployee(employee);

        ArgumentCaptor<User> employeeCaptor = ArgumentCaptor.forClass(User.class);
        verify(employeeDao, times(1)).deleteEmployee(employeeCaptor.capture());

        User capturedEmployee = employeeCaptor.getValue();

        Assert.assertEquals(employee.getFirstName(), capturedEmployee.getFirstName());
        Assert.assertEquals(employee.getLastName(), capturedEmployee.getLastName());
        Assert.assertEquals(employee.getHdCurrentYear(), capturedEmployee.getHdCurrentYear());
        Assert.assertEquals(employee.getHdReceivedCurrentYear(), capturedEmployee.getHdReceivedCurrentYear());
        Assert.assertEquals(employee.getSalary(), capturedEmployee.getSalary(),0);
        Assert.assertEquals(employee.getEmail(), capturedEmployee.getEmail());
        Assert.assertEquals(employee.getUsername(), capturedEmployee.getUsername());
    }

    private User createaUser() {
        User user = new User();

        user.setFirstName("FirstName");
        user.setLastName("LastName");
        user.setHdCurrentYear(10);
        user.setHdReceivedCurrentYear(21);
        user.setSalary(2000.0);
        user.setEmail("firstName.lastName@email.com");
        user.setUsername("firstname.lastname");
        return user;
    }
}
