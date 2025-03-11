package com.example.blog.DTO;

import com.example.blog.DTO.ArticleResponse;
import com.example.blog.domain.Article;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
public class ArticleListViewResponse {
    private final Long id;
    private final String title;
    private final String content;
    private final String author;
    private final LocalDateTime updatedAt;
    private final LocalDateTime createdAt;



    public ArticleListViewResponse(Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.content = article.getContent();
        this.createdAt = article.getCreatedAt();
        this.updatedAt = article.getUpdatedAt();
        this.author = article.getAuthor();
    }
}
