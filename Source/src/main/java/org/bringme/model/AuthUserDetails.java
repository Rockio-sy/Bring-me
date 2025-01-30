package org.bringme.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Specific {@link UserDetails} class handles authentication operation
 */
public class AuthUserDetails implements UserDetails {
    private final Person person;

    public AuthUserDetails(Person person) {
        this.person = person;
    }


    public Long getId() {
        return person.getId();
    }


    public String getPhone() {
        return person.getPhone();
    }


    @Override
    public String getPassword() {
        return person.getPassword();
    }

    @Override
    public String getUsername() {
        return person.getEmail();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role = person.getRole();
        if (role == null || role.isEmpty()) {
            role = "USER";
        }
        return List.of(new SimpleGrantedAuthority("ROLE_" + person.getRole().toUpperCase()));
    }

    @Override
    public boolean isEnabled() {
        return person.getAccountStatus() == 1;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return person.getAccountStatus() < 2;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
}
