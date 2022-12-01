package com.moloko.molokoblogengine.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * User of the api with particular role.
 *
 * @param username used as an ID
 * @param password not empty
 * @param role not empty
 */
@Document
public class User implements UserDetails {

  @Id private String username;

  @NotNull private String password;

  private String role;

  private Set<GrantedAuthority> roles = new HashSet<>();

  /** Only ADMIN and USER are valid values for the role. */
  public User(String username, String password, String role) {
    this.username = username;
    this.password = password;
    this.roles.add(new SimpleGrantedAuthority("ROLE_" + role));
  }

  public void addAuthority(String authority) {
    this.roles.add(new SimpleGrantedAuthority(authority));
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return roles;
  }

  public void setPassword(String password) {
    this.password = password;
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
