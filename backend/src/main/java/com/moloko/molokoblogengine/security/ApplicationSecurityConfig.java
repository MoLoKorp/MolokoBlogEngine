package com.moloko.molokoblogengine.security;

import com.moloko.molokoblogengine.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
public class ApplicationSecurityConfig {

    private final PasswordEncoder passwordEncoder;

    UserRepository userRepository;

    @Autowired
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder) { this.passwordEncoder = passwordEncoder; }

    @Bean
    public ReactiveUserDetailsService userDetailsService() {
        return (username) -> userRepository.findById(username);
    }

    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .authorizeExchange()

                .anyExchange().authenticated()
                .and()
                .httpBasic();
        return http.build();
    }
}
