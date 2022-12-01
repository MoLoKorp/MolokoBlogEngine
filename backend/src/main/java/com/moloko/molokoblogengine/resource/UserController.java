package com.moloko.molokoblogengine.resource;

import com.moloko.molokoblogengine.model.User;
import com.moloko.molokoblogengine.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/** User controller with basic CRUD operations. */
@CrossOrigin
@RestController
@EnableReactiveMethodSecurity
@RequestMapping("/user")
public class UserController {
  @Autowired UserRepository userRepository;
  @Autowired PasswordEncoder passwordEncoder;

  private static final String USER_NOT_FOUND = "User is not found";

  @GetMapping
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public Flux<User> getUsers() {
    return userRepository.findAll();
  }

  /**
   * Get user information by name.
   *
   * @return user info
   */
  @GetMapping("{id}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public Mono<User> getUser(@PathVariable String id) {
    return userRepository
        .findById(id)
        .switchIfEmpty(
            Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, USER_NOT_FOUND)));
  }

  @PostMapping
  @PreAuthorize("permitAll()")
  public Mono<User> createUser(@Validated @RequestBody User user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    return userRepository.save(user);
  }

  /** Delete user by name. Admin access only. */
  @DeleteMapping("{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public Mono deleteUser(@PathVariable String id) {
    return userRepository
        .findById(id)
        .switchIfEmpty(
            Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, USER_NOT_FOUND)))
        .doOnNext(user -> userRepository.deleteById(id).subscribe());
  }

  @DeleteMapping
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public void deleteUsers() {
    userRepository.deleteAll().subscribe();
  }
}
