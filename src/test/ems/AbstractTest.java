package ems;

import main.ems.model.Department;
import main.ems.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;


@RunWith(MockitoJUnitRunner.class)
public class AbstractTest {


    protected AuthenticationProvider appAuthenticationProvider = new AuthenticationProvider() {

        @Override
        public Authentication authenticate(Authentication authentication) throws AuthenticationException {

            return new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), authentication.getAuthorities());
        }

        @Override
        public boolean supports(Class<?> aClass) {
            return false;
        }
    };


    protected void authenticateUser() {
        User user;

        user = new User();
        user.setUsername("test.test");
        user.setPassword("test");
        user.setUserId(1);

        Department department = new Department();
        department.setDepartmentId(1);
        department.setDepartmentName("Department");
        user.setDepartment(department);

        Authentication usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user,user.getPassword());
        Authentication result = appAuthenticationProvider.authenticate(usernamePasswordAuthenticationToken);
        SecurityContextHolder.getContext().setAuthentication(result);
        User userDetails = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Assert.assertNotNull(userDetails);
        Assert.assertNotNull(userDetails.getUsername());
        Assert.assertEquals(user.getUserId(), userDetails.getUserId());

    }

    @Test
    public void start() {
        System.out.println("START");
    }
}
