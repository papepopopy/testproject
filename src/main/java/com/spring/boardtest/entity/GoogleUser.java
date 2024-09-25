package com.spring.boardtest.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "google_users")
public class GoogleUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 고유 식별자

    private String sub;      // 사용자 ID
    private String email;    // 이메일
    private String name;     // 이름
    private String picture;  // 프로필 사진 URL
}
