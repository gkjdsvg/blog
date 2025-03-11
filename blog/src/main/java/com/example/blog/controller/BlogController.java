package com.example.blog.controller;

import com.example.blog.DTO.AddArticleRequest;
import com.example.blog.DTO.ArticleResponse;
import com.example.blog.DTO.ArticleViewResponse;
import com.example.blog.DTO.UpdateArticleRequest;
import com.example.blog.domain.Article;
import com.example.blog.repository.BlogRepository;
import com.example.blog.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:8084")
public class BlogController {
    private final BlogService blogService;
    private final BlogRepository blogRepository;

    @PostMapping("/api/articles")
    public ResponseEntity<Article> addArticle(@RequestBody AddArticleRequest request, Principal principal) {
        Article savedArticle = blogService.save(request, principal.getName());

        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(savedArticle);
    }

    @GetMapping("/api/articles")
    public ResponseEntity<List<ArticleResponse>> findAllArticles() {
        List<ArticleResponse> articles = blogService.findAll()
                .stream()
                .map(ArticleResponse::new)
                .toList();

        return ResponseEntity.ok()
                .body(articles);
    }

    @GetMapping("/api/articles/{id}")
    public ResponseEntity<ArticleResponse> findArticle(@PathVariable("id") long id) { //원래 코드 : (@PathVariable long id)
        Article article = blogService.findById(id);
        return ResponseEntity.ok()
                .body(new ArticleResponse(article));
    }

    @DeleteMapping("/api/articles/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable("id") long id) {
        blogService.delete(id);

        return ResponseEntity.ok()
                .build();
    }

    @PutMapping("/api/articles/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable("id") long id, @RequestBody UpdateArticleRequest request ) {
        Article updatedArticle = blogService.update(id, request);

        return ResponseEntity.ok()
                .body(updatedArticle);
    }

    @GetMapping("/new-article")
    public ModelAndView newArticle(@RequestParam(required = false) Long id) {
        System.out.println("Received id: " + id);

        ModelAndView modelAndView = new ModelAndView();

        if (id == null) {
            // 새 기사 생성
            modelAndView.addObject("article", new ArticleViewResponse(Article.builder().build()));
        } else {
            // 기존 기사 조회
            Article article = blogRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid article ID: " + id));
            modelAndView.addObject("article", new ArticleViewResponse(article));
        }

        // 뷰 이름 설정
        modelAndView.setViewName("newArticle");

        return modelAndView;
    }
}