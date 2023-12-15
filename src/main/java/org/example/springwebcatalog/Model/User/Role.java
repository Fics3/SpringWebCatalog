package org.example.springwebcatalog.Model.User;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {

    USER,
    SELLER;

    @Override
    public String getAuthority() {
        return name();
    }
}
