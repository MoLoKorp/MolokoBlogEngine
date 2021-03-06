package com.moloko.molokoblogengine.repository;

import com.moloko.molokoblogengine.model.Article;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends ReactiveMongoRepository<Article, String> { }
