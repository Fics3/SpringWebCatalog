package org.example.springwebcatalog.Model.User;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.springwebcatalog.Model.User.UserProducts.CartItem;
import org.example.springwebcatalog.Model.User.UserProducts.PurchasedItem;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Getter
@Setter

public class CustomUser implements UserDetails {

    @Getter
    @Setter
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true)
    private String username;

    private String password;
    private String firstname;
    private String lastname;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();

    @OneToMany(mappedBy = "customUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchasedItem> purchasedItems = new ArrayList<>();


    @Column(unique = true)
    private String email;


    @Enumerated(EnumType.STRING)
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(role);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }


}
