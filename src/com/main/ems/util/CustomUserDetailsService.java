package main.ems.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import main.ems.service.UserService;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserService userService;


    public UserService getUserService() {
        return userService;
    }


    public void setUserService(UserService userService) {
        this.userService = userService;
    }


    public UserDetails loadUserByUsername(String username) {
        main.ems.model.User user = userService.getUserByUsername(username);
        boolean enabled = true;
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;
        return new User(user.getUsername(), user.getPassword(), enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, getAuthorities(user));
    }

    public Collection<? extends GrantedAuthority> getAuthorities(main.ems.model.User user) {
        Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
        authorities.add(new GrantedAuthorityImpl(user.getRole().getRoleName()));
        return authorities;
    }
}
