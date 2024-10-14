package com.example.blog.domain;

import jakarta.annotation.Generated;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cglib.core.Local;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
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

    @CreatedDate
    @Column(name = "created_at")
    @Getter
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    @Getter
    private LocalDateTime updatedAt;
}
