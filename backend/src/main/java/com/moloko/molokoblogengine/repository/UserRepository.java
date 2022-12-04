package com.moloko.molokoblogengine.repository;

import com.moloko.molokoblogengine.model.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/** Reactive User MongoDB repository with basic operations. */
@Repository
public interface UserRepository extends ReactiveMongoRepository<User, String> {
  Mono<User> findByUsername(String username);
}
