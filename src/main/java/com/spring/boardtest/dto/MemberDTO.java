package com.spring.boardtest.dto;

import lombok.*;

@Getter@Setter@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberDTO {
    private String email;
    private String name;
    private String picture;
//    private String profileImageUrl;
    private String password;
}
