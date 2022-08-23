package com.moloko.molokoblogengine.resource;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.moloko.molokoblogengine.model.User;
import com.moloko.molokoblogengine.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
  @Mock private UserRepository userRepositoryMock;
  @InjectMocks private UserController userControllerMock;

  private static final User user1 = new User("user1", "1234", "ADMIN");
  private static final User user2 = new User("user2", "5678", "USER");

  @Test
  void testGetUsers() {
    when(userRepositoryMock.findAll()).thenReturn(Flux.just(user1, user2));

    var resultFlux = userControllerMock.getUsers();

    StepVerifier.create(resultFlux).expectNext(user1).expectNext(user2).verifyComplete();
  }

  @Test
  void testGetUser() {
    when(userRepositoryMock.findById("user1")).thenReturn(Mono.just(user1));

    var resultMono = userControllerMock.getUser("user1");

    StepVerifier.create(resultMono).expectNext(user1).verifyComplete();
  }

  @Test
  void testCreateArticle() {
    when(userRepositoryMock.save(any(User.class))).thenReturn(Mono.just(user1));

    var resultMono = userControllerMock.createUser(user1);

    StepVerifier.create(resultMono).expectNext(user1).verifyComplete();
  }

  @Test
  void testDeleteArticle() {
    when(userRepositoryMock.deleteById("user1")).thenReturn(Mono.empty());

    userControllerMock.deleteUser("user1");

    verify(userRepositoryMock).deleteById("user1");
  }
}
