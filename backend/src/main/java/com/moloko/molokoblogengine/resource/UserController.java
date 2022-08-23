package com.moloko.molokoblogengine.resource;

import com.moloko.molokoblogengine.model.User;
import com.moloko.molokoblogengine.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/** User controller with basic CRUD operations. */
@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {
  @Autowired UserRepository userRepository;

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
    return userRepository.save(user);
  }

  @DeleteMapping("{id}")
  public void deleteUser(@PathVariable String id) {
    userRepository.deleteById(id).subscribe();
  }
}