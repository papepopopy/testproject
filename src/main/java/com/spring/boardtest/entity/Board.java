package com.spring.boardtest.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity@Table(name="board")
@Getter@ToString
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;

    @Column(length = 500, nullable = false)
    private String title;
    @Column(length = 2000, nullable = false)
    private String content;
    @Column(length = 50, nullable = false)
    private String writer;

    @LastModifiedDate
    @Column(name="moddate")
    private LocalDateTime modDate;
    @CreatedDate
    @Column(name="regdate", updatable=false)  // updatable=false 수정되는 시점에 기능 off
    private LocalDateTime regDate;

    private String email;

    public void change(String title, String content) {
        this.title = title;
        this.content = content;
    }
}