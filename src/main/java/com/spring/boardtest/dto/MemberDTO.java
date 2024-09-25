package com.spring.boardtest.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemberDTO {
    private String email;
    private String name;
    private String picture;
//    private String profileImageUrl;
    private String password;
}
