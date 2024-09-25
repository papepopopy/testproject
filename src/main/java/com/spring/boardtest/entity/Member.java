package com.spring.boardtest.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="member")
@Getter@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가 ID
    private Long id;

    @Column(nullable = false, unique = true) // 이메일은 필수이며 고유해야 함
    private String email;

    @Column(nullable = false) // 이름은 필수
    private String name;

//    @Column(name = "profile_image") // 프로필 이미지를 위한 컬럼
//    private String profileImageUrl;

//    @Column(nullable = false) // 비밀번호는 필수
//    private String password;

}
