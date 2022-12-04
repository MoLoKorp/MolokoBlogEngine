package com.moloko.molokoblogengine.resource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import com.moloko.molokoblogengine.model.Article;
import com.moloko.molokoblogengine.repository.ArticleRepository;
import com.moloko.molokoblogengine.util.Shell;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.security.Principal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class ArticleControllerTest {
  @Mock private ArticleRepository articleRepositoryMock;

  @Mock private Principal principal;
  @Mock private Shell shellMock;
  @InjectMocks private ArticleController articleControllerMock;

  @Mock private Process processMock;
  @Mock private FilePart filePartMock;

  private static final Article article1 = new Article("test_id1", "test_text1", "test_owner1");
  private static final Article article2 = new Article("test_id2", "test_text2", "test_owner2");

  @Test
  void testGetArticles() {
    when(articleRepositoryMock.findAll()).thenReturn(Flux.just(article1, article2));

    var resultFlux = articleControllerMock.getArticles();

    StepVerifier.create(resultFlux).expectNext(article1).expectNext(article2).verifyComplete();
  }

  @Test
  void testGetArticle() {
    when(articleRepositoryMock.findById("test_id1")).thenReturn(Mono.just(article1));

    var resultMono = articleControllerMock.getArticle("test_id1");

    StepVerifier.create(resultMono).expectNext(article1).verifyComplete();
  }

  @Test
  void testCreateArticle() {
    when(articleRepositoryMock.save(any(Article.class))).thenReturn(Mono.just(article1));
    when(principal.getName()).thenReturn("test_owner1");

    var resultMono = articleControllerMock.createArticle(article1, principal);

    StepVerifier.create(resultMono).expectNext(article1).verifyComplete();
  }

  @Test
  void testDeleteArticle() {
    when(articleRepositoryMock.findById("test_id1")).thenReturn(Mono.just(article1));
    when(articleRepositoryMock.deleteById("test_id1")).thenReturn(Mono.empty());
    when(principal.getName()).thenReturn("test_owner1");

    var resultMono = articleControllerMock.deleteArticle("test_id1", principal);

    StepVerifier.create(resultMono).verifyComplete();
  }

  @Test
  void testUpdateArticle() {
    when(articleRepositoryMock.findById("test_id1")).thenReturn(Mono.just(article1));
    when(articleRepositoryMock.save(article1)).thenReturn(Mono.just(article1));
    when(principal.getName()).thenReturn("test_owner1");

    var resultMono = articleControllerMock.updateArticle("test_id1", article1, principal);

    StepVerifier.create(resultMono)
        .expectNext(new Article("test_id1", "test_text1", "test_owner1"))
        .verifyComplete();
  }

  @Test
  void testExportSuccess() throws IOException {
    when(shellMock.exec(any(String[].class))).thenReturn(processMock);
    when(processMock.getInputStream())
        .thenReturn(new ByteArrayInputStream("Process executed successfully".getBytes()));

    ResponseEntity<String> response = articleControllerMock.exportArticles();

    assertTrue(response.getHeaders().containsKey(HttpHeaders.CONTENT_DISPOSITION));
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Process executed successfully", response.getBody());
  }

  @Test
  void testExportException() throws IOException {
    when(shellMock.exec(any(String[].class))).thenThrow(new IOException("something happened!"));

    ResponseEntity<String> response = articleControllerMock.exportArticles();

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    assertEquals("Unexpected error during export: something happened!", response.getBody());
  }

  @Test
  void testImportSuccess() throws IOException {
    when(shellMock.exec(any(String[].class))).thenReturn(processMock);
    when(processMock.getInputStream())
        .thenReturn(new ByteArrayInputStream("Process executed successfully".getBytes()));
    when(filePartMock.transferTo(any(File.class))).thenReturn(Mono.empty());

    ResponseEntity<String> response = articleControllerMock.importArticles(filePartMock);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Process executed successfully", response.getBody());
  }

  @Test
  void testImportException() throws IOException {
    when(shellMock.exec(any(String[].class))).thenThrow(new IOException("something happened!"));
    when(filePartMock.transferTo(any(File.class))).thenReturn(Mono.empty());

    ResponseEntity<String> response = articleControllerMock.importArticles(filePartMock);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    assertEquals("Unexpected error during import: something happened!", response.getBody());
  }
}
