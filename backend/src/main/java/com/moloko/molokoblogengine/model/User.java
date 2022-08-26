package com.moloko.molokoblogengine.model;

import javax.validation.constraints.NotNull;

import com.moloko.molokoblogengine.security.PasswordConfig;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * User of the api with particular role.
 *
 * @param username used as an ID
 * @param password not empty
 * @param role not empty
 */
@Document
public class User implements UserDetails {

    @Id
    private String username;

    @NotNull
    private String password;

    @NotNull
    private String role;


    private Set<GrantedAuthority> roles = new HashSet<>();

    public User(String username, String password) {
        this.username = username;
        this.password = new PasswordConfig().passwordEncoder().encode(password);
        roles.add(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
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
}
