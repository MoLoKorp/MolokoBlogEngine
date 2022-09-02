package com.moloko.molokoblogengine.resource;

import com.moloko.molokoblogengine.model.User;
import com.moloko.molokoblogengine.repository.ArticleRepository;
import com.moloko.molokoblogengine.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/** User controller with basic CRUD operations. */
@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {
  @Autowired UserRepository userRepository;
  @Autowired ArticleRepository articleRepository;
  @Autowired PasswordEncoder passwordEncoder;

  @ResponseStatus(HttpStatus.NOT_FOUND)
  class NotFoundException extends RuntimeException { }

  @GetMapping
  public Flux<User> getUsers() {
    return userRepository.findAll();
  }

  @GetMapping("{id}")
  public Mono<User> getUser(@PathVariable String id) {
    return userRepository.findById(id);
  }

  @PostMapping
  public Mono<User> createUser(@Validated @RequestBody User user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    return userRepository.save(user);
  }

  @DeleteMapping("{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public Mono deleteUser(@PathVariable String id) {
    articleRepository.findAll().
            filter(article -> article.owner().equals(id))
            .doOnNext(article -> articleRepository.deleteById(article.id()).subscribe()).subscribe();

    return userRepository.findById(id)
            .switchIfEmpty(Mono.error(new NotFoundException()))
            .doOnNext(user -> userRepository.deleteById(id).subscribe());
  }

  @DeleteMapping
  public void deleteUsers() {
    userRepository.deleteAll().subscribe();
  }
}
