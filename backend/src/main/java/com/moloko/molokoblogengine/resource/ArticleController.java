package com.moloko.molokoblogengine.resource;

import com.moloko.molokoblogengine.model.Article;
import com.moloko.molokoblogengine.repository.ArticleRepository;
import com.moloko.molokoblogengine.repository.UserRepository;
import com.moloko.molokoblogengine.util.Shell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.util.StreamUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.Date;
import java.util.UUID;
import java.util.function.Predicate;

/**
 * Articles controller. Supports basic CRUD operation and export/import to JSON file via
 * mongoimport/mongoexport utilities from the mongodb-tools package.
 */
@CacheConfig(cacheNames = "articles")
@CrossOrigin
@RestController
@EnableReactiveMethodSecurity
@RequestMapping("/article")
public class ArticleController {
  private static final String MONGO_COLLECTION = "article";
  private static final String MONGOIMPORT_TEMP_FILE = "temp.json";
  private static final String ALL = "'all'";

  @Autowired ArticleRepository articleRepository;
  @Autowired UserRepository userRepository;
  @Autowired Shell shell;

  @Value("${spring.data.mongodb.uri}")
  private String mongoUri;

  @Cacheable(key = ALL)
  @GetMapping
  @PreAuthorize("permitAll()")
  public Flux<Article> getArticles() {
    return articleRepository.findAll().cache();
  }

  @Cacheable
  @GetMapping("{id}")
  @PreAuthorize("permitAll()")
  public Mono<Article> getArticle(@PathVariable String id) {
    return articleRepository.findById(id).cache();
  }

  @CacheEvict(key = ALL)
  @PostMapping
  @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
  public Mono<Article> createArticle(@Validated @RequestBody Article article, Principal principal) {
      return articleRepository.save(new Article(UUID.randomUUID().toString(), article.text(), principal.getName()));
  }

  @Caching(evict = {@CacheEvict, @CacheEvict(key = ALL)})
  @DeleteMapping("{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
  public Mono deleteArticle(@PathVariable String id, Principal principal) {
    return articleRepository
            .findById(id)
            .switchIfEmpty(Mono.error(new NotFoundException()))
            .filter(isOwner(principal.getName()))
            .switchIfEmpty(Mono.error(new ForbiddenException()))
            .doOnNext(
                article -> {
                  articleRepository.deleteById(id).subscribe();
            });
  }

  @CachePut(key = "#id")
  @CacheEvict(key = ALL)
  @PutMapping("{id}")
  @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
  public Mono<Article> updateArticle(
      @PathVariable String id, @Validated @RequestBody Article updatedArticle, Principal principal) {
          return articleRepository
                  .findById(id)
                  .switchIfEmpty(Mono.error(new NotFoundException()))
                  .filter(isOwner(principal.getName()))
                  .switchIfEmpty(Mono.error(new ForbiddenException()))
                  .doOnNext(
                          _article -> {
                            articleRepository.save(new Article(id, updatedArticle.text(), principal.getName())).subscribe();
                          }).thenReturn(new Article(id, updatedArticle.text(), principal.getName())).cache();
  }

  /**
   * Exports articles to JSON file.
   *
   * @return all articles in JSON format
   */
  @GetMapping("export")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ResponseEntity<String> exportArticles() {
    String[] command = {"mongoexport", "--uri", mongoUri, "--collection", MONGO_COLLECTION};
    try {
      Process p = shell.exec(command);
      String body = StreamUtils.copyToString(p.getInputStream(), StandardCharsets.UTF_8);
      return ResponseEntity.ok()
          .header(
              HttpHeaders.CONTENT_DISPOSITION,
              "attachment; filename=\"articles" + new Date() + ".json\"")
          .body(body);
    } catch (IOException e) {
      return ResponseEntity.internalServerError()
          .body("Unexpected error during export: " + e.getMessage());
    }
  }

  /**
   * Imports articles from JSON file.
   *
   * @param file multipart JSON file with articles to import
   * @return message with import result
   */

  @PostMapping(
      value = "import",
      consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ResponseEntity<String> importArticles(@RequestPart("file") FilePart file) {
    String[] command = {
      "mongoimport",
      "--uri",
      mongoUri,
      "--collection",
      MONGO_COLLECTION,
      "--file",
      MONGOIMPORT_TEMP_FILE
    };
    file.transferTo(new File(MONGOIMPORT_TEMP_FILE)).subscribe();
    try {
      Process p = shell.exec(command);
      String body = StreamUtils.copyToString(p.getInputStream(), StandardCharsets.UTF_8);
      return ResponseEntity.ok(body);
    } catch (IOException e) {
      return ResponseEntity.internalServerError()
          .body("Unexpected error during import: " + e.getMessage());
    }
  }

  @ResponseStatus(HttpStatus.FORBIDDEN)
  class ForbiddenException extends RuntimeException {

  }

  @ResponseBody
  @ExceptionHandler(ForbiddenException.class)
  public Mono<ResponseEntity> handleForviddenException() {
    return Mono.just(new ResponseEntity("Access denied", HttpStatus.FORBIDDEN));
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)
  class NotFoundException extends RuntimeException { }

  private Predicate<Article> isOwner(String owner) {
    return article -> article.owner().equals(owner);
  }
}
