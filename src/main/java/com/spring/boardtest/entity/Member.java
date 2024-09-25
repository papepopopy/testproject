package com.spring.boardtest.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "members") // 테이블 이름을 members로 설정
@Data // Lombok을 사용하여 getter/setter, toString 등을 자동 생성
@NoArgsConstructor // 기본 생성자
@AllArgsConstructor // 모든 필드를 매개변수로 받는 생성자
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
    private String password;

    @ElementCollection(fetch = FetchType.EAGER) // 권한 컬렉션
    private Set<String> roles = new HashSet<>();

    public void addRole(String role) {
        this.roles.add(role);
    }

    // 필요한 경우 역할을 초기화하는 메서드
    public void clearRoles() {
        this.roles.clear();
    }

}
