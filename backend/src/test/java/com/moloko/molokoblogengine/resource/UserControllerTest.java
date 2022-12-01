package com.moloko.molokoblogengine.resource;

import com.moloko.molokoblogengine.model.User;
import com.moloko.molokoblogengine.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
  @Mock private UserRepository userRepositoryMock;
  @Mock private PasswordEncoder passwordEncoder;
  @InjectMocks private UserController userControllerMock;

  private static final User user1 = new User("user1", "1234", "ADMIN");
  private static final User user2 = new User("user2", "5678", "USER");

  @Test
  void testGetUsers() {
    Mockito.when(userRepositoryMock.findAll()).thenReturn(Flux.just(user1, user2));

    var resultFlux = userControllerMock.getUsers();

    StepVerifier.create(resultFlux).expectNext(user1).expectNext(user2).verifyComplete();
  }

  @Test
  void testGetUser() {
    Mockito.when(userRepositoryMock.findById("user1")).thenReturn(Mono.just(user1));

    var resultMono = userControllerMock.getUser("user1");

    StepVerifier.create(resultMono).expectNext(user1).verifyComplete();
  }

  @Test
  void testCreateUser() {
    Mockito.when(userRepositoryMock.save(Mockito.any(User.class))).thenReturn(Mono.just(user1));
    Mockito.when(passwordEncoder.encode("1234")).thenReturn("encoded1234");

    var resultMono = userControllerMock.createUser(user1);

    StepVerifier.create(resultMono).expectNext(user1).verifyComplete();
  }

  @Test
  void testDeleteUser() {
    Mockito.when(userRepositoryMock.findById("user1")).thenReturn(Mono.just(user1));
    Mockito.when(userRepositoryMock.deleteById("user1")).thenReturn(Mono.empty());

    var resultMono = userControllerMock.deleteUser("user1");

    StepVerifier.create(resultMono).expectNext(user1).verifyComplete();
  }

  @Test
  void testDeleteUsers() {
    Mockito.when(userRepositoryMock.deleteAll()).thenReturn(Mono.empty());

    userControllerMock.deleteUsers();

    Mockito.verify(userRepositoryMock).deleteAll();
  }
}
