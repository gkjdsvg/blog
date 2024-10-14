package com.example.blog.domain;

import jakarta.annotation.Generated;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)

    private Long id;

    @Column(name = "title", nullable = false)
    @Getter
    private String title;

    @Column(name = "content", nullable = false)
    @Getter
    private String content;

    @Builder
    public Article(Long id, String title, String content){
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
