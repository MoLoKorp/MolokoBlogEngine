package com.moloko.molokoblogengine.resource;

import com.moloko.molokoblogengine.model.Article;
import com.moloko.molokoblogengine.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController("/article")
public class ArticleController {

    @Autowired
    ArticleRepository articleRepository;

    @GetMapping
    public Flux<Article> getArticles() {
        return articleRepository.findAll();
    }

    @GetMapping("{id}")
    public Mono<Article> getArticle(@PathVariable String id) {
        return articleRepository.findById(id);
    }

    @PostMapping
    public void createArticle(@Validated @RequestBody Article article) {
        articleRepository.save(new Article(UUID.randomUUID().toString(), article.text())).subscribe();
    }

    @DeleteMapping("{id}")
    public void deleteArticle(@PathVariable String id) {
        articleRepository.deleteById(id).subscribe();
    }
}
