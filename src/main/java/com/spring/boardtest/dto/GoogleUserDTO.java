package com.spring.boardtest.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class GoogleUserDTO {
    private String sub; // 사용자 ID
    private String email; // 이메일
    private String name; // 이름
//    private String ProfileImageUrl; // 프로필 사진 URL

}
