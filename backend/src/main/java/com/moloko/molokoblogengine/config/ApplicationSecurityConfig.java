package com.moloko.molokoblogengine.config;

import com.moloko.molokoblogengine.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

/** Spring security config with the basic auth and keeping users in mongodb. */
@EnableWebFluxSecurity
public class ApplicationSecurityConfig {

  @Autowired private UserRepository userRepository;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public ReactiveUserDetailsService userDetailsService() {
    return (username) -> userRepository.findByUsername(username);
  }

  @Bean
  SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {

    http.csrf()
        .disable()
        .authorizeExchange()
        .pathMatchers("/**")
        .permitAll()
        .anyExchange()
        .authenticated()
        .and()
        .formLogin()
        .disable()
        .httpBasic();
    return http.build();
  }
}
