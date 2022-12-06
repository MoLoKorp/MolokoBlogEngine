package com.moloko.molokoblogengine.config;

import com.moloko.molokoblogengine.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

/** Spring security config with the basic auth and keeping users in mongodb. */
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class ApplicationSecurityConfig {

  @Autowired private UserRepository userRepository;

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  ReactiveUserDetailsService userDetailsService() {
    return username ->
        userRepository
            .findById(username)
            .map(
                user ->
                    User.withUsername(user.username())
                        .password(user.password())
                        .roles(user.role())
                        .build());
  }

  @Bean
  SecurityWebFilterChain securityConfig(ServerHttpSecurity http) {
    return http.httpBasic().and().csrf().disable().build();
  }

  @ControllerAdvice
  private class RestExceptionHandler {
    @ExceptionHandler(AccessDeniedException.class)
    private void handlePreAuthorize() {
      throw new ResponseStatusException(
          HttpStatus.FORBIDDEN, "User is not authorized to access the resource");
    }
  }
}
