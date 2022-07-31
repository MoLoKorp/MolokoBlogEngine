package com.moloko.molokoblogengine.resource;

import com.moloko.molokoblogengine.model.Article;
import com.moloko.molokoblogengine.repository.ArticleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ArticleControllerTest {

    @Mock
    private ArticleRepository articleRepository;

    @InjectMocks
    private ArticleController articleController;

    private static final Article article1 = new Article("test_id1", "test_text1");
    private static final Article article2 = new Article("test_id2", "test_text2");

    @Test
    void testGetArticles() {
        when(articleRepository.findAll()).thenReturn(Flux.just(article1, article2));

        var resultFlux = articleController.getArticles();

        StepVerifier.create(resultFlux)
                .expectNext(article1)
                .expectNext(article2)
                .verifyComplete();
    }

    @Test
    void testGetArticle() {
        when(articleRepository.findById("test_id1")).thenReturn(Mono.just(article1));

        var resultMono = articleController.getArticle("test_id1");

        StepVerifier.create(resultMono)
                .expectNext(article1)
                .verifyComplete();
    }

    @Test
    void testCreateArticle() {
        when(articleRepository.save(any(Article.class))).thenReturn(Mono.just(article1));

        var resultMono = articleController.createArticle(article1);

        StepVerifier.create(resultMono)
                .expectNext(article1)
                .verifyComplete();
    }

    @Test
    void testDeleteArticle() {
        when(articleRepository.deleteById("test_id1")).thenReturn(Mono.empty());

        articleController.deleteArticle("test_id1");

        verify(articleRepository).deleteById("test_id1");
    }

    @Test
    void testUpdateArticle() {
        when(articleRepository.save(article1)).thenReturn(Mono.just(article1));

        var resultMono = articleController.updateArticle("test_id1", article1);

        StepVerifier.create(resultMono)
                .expectNext(new Article("test_id1", "test_text1"))
                .verifyComplete();
    }
}