package com.moloko.molokoblogengine.repository;

import com.moloko.molokoblogengine.model.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/** Reactive User MongoDB repository with basic operations. */
@Repository
public interface UserRepository extends ReactiveMongoRepository<User, String> {}
