package com.moloko.molokoblogengine.resource;

import com.moloko.molokoblogengine.model.Article;
import com.moloko.molokoblogengine.repository.ArticleRepository;
import com.moloko.molokoblogengine.util.Shell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.core.Authentication;
import org.springframework.util.StreamUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.Date;
import java.util.UUID;

/**
 * Articles controller. Supports basic CRUD operation and export/import to JSON file via
 * mongoimport/mongoexport utilities from the mongodb-tools package.
 */
@CacheConfig(cacheNames = "articles")
@CrossOrigin
@RestController
@RequestMapping("/article")
public class ArticleController {
  private static final String MONGO_COLLECTION = "article";
  private static final String MONGOIMPORT_TEMP_FILE = "temp.json";
  private static final String ALL = "'all'";

  @Autowired ArticleRepository articleRepository;
  @Autowired Shell shell;

  @Value("${spring.data.mongodb.uri}")
  private String mongoUri;

  @Cacheable(key = ALL)
  @GetMapping
  public Flux<Article> getArticles() {
    return articleRepository.findAll().cache();
  }

  @Cacheable
  @GetMapping("{id}")
  public Mono<Article> getArticle(@PathVariable String id) {
    return articleRepository.findById(id).cache();
  }

  @CacheEvict(key = ALL)
  @PostMapping
  public Mono<Article> createArticle(@Validated @RequestBody Article article, Authentication authentication) {
      return articleRepository.save(new Article(UUID.randomUUID().toString(), article.text()));
  }

  @Caching(evict = {@CacheEvict, @CacheEvict(key = ALL)})
  @DeleteMapping("{id}")
  public void deleteArticle(@PathVariable String id, Authentication authentication) {
        articleRepository.deleteById(id).subscribe();
  }

  @CachePut(key = "#id")
  @CacheEvict(key = ALL)
  @PutMapping("{id}")
  public Mono<Article> updateArticle(@PathVariable String id, @Validated @RequestBody Article article, Principal user) {
        return articleRepository.save(new Article(id, article.text())).cache();
  }

  /**
   * Exports articles to JSON file.
   *
   * @return all articles in JSON format
   */
  @GetMapping("export")
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
}
