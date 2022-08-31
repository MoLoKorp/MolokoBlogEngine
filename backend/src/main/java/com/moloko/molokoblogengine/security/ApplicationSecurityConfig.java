package com.moloko.molokoblogengine.security;

import com.moloko.molokoblogengine.config.HttpRequestLoggingFilter;
import com.moloko.molokoblogengine.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter;

@EnableWebFluxSecurity
public class ApplicationSecurityConfig {

    @Autowired
    private UserRepository userRepository;

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

        http
                .csrf().disable()
                .authorizeExchange()
                .pathMatchers("/user", "/user/**").hasRole("ADMIN")
                .pathMatchers(HttpMethod.DELETE, "/article", "/article/**").hasAnyRole("ADMIN", "USER")
                .pathMatchers(HttpMethod.PUT, "/article", "/article/**").hasRole("ADMIN")
                .pathMatchers(HttpMethod.GET, "/article", "/article/**").permitAll()
                .pathMatchers(HttpMethod.POST, "/article", "/article/**").hasAnyRole("ADMIN", "USER")
                .anyExchange().authenticated()
                .and()
                .httpBasic();
        return http.build();
    }

}
