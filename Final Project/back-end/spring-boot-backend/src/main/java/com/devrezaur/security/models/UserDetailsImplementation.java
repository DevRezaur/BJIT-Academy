package com.devrezaur.security.models;

import com.devrezaur.model.Role;
import com.devrezaur.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class UserDetailsImplementation implements UserDetails {

    private static final long serialVersionUID = 1L;

    private String userId;
    private String fullName;
    private String username;
    private Collection<? extends GrantedAuthority> authorities;

    @JsonIgnore
    private String password;

    public UserDetailsImplementation(String userId, String fullName, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        this.userId = userId;
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserDetailsImplementation build(User user) {
        List<GrantedAuthority> roles = new ArrayList<>();

        for (Role role : user.getRoles()) {
            roles.add(new SimpleGrantedAuthority(role.getRole()));
        }

        return new UserDetailsImplementation(user.getUserId(), user.getFullName(), user.getUsername(), user.getPassword(), roles);
    }

    public String getUserId() {
        return userId;
    }

    public String getFullName() {
        return fullName;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
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
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImplementation user = (UserDetailsImplementation) o;
        return Objects.equals(userId, user.userId);
    }
}
