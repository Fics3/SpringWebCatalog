package org.example.springwebcatalog.Services;

import jakarta.servlet.http.HttpServletRequest;
import org.example.springwebcatalog.Model.User.CustomUser;
import org.example.springwebcatalog.Model.User.Role;
import org.example.springwebcatalog.Mapper.UserRepository;
import org.example.springwebcatalog.Services.ServiceInterfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;


@Service
public class UserManager implements UserService {

    @Autowired
    private UserRepository users;

    @Override
    public void registerUser(CustomUser user, Role role) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setRole(role);
        users.save(user);
    }

    @Override
    public CustomUser findUserByName(String username) {
        return users.findCustomUserByUsername(username);
    }

    @Override
    public void logout(HttpServletRequest httpServletRequest) {
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            logoutHandler.logout(httpServletRequest, null, auth);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CustomUser user = users.findCustomUserByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getAuthorities()
        );
    }
}
