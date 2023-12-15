package org.example.springwebcatalog.Services.ServiceInterfaces;

import jakarta.servlet.http.HttpServletRequest;
import org.example.springwebcatalog.Model.User.CustomUser;
import org.example.springwebcatalog.Model.User.Role;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    void registerUser(CustomUser user, Role role);

    CustomUser findUserByName(String name);

    void logout(HttpServletRequest httpServletRequest);
}
