package com.moloko.molokoblogengine.resource;

import com.moloko.molokoblogengine.model.Article;
import com.moloko.molokoblogengine.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.UUID;

@RestController
public class RootController {

    @Autowired
    ArticleRepository articleRepository;

    @GetMapping
    public Flux<Article> getArticles() {
        return articleRepository.findAll();
    }

    @PostMapping
    public void createArticle(@RequestParam String text) {
        articleRepository.insert(new Article(UUID.randomUUID().toString(), text));
    }
}
