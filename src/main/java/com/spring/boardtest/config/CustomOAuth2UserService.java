package com.spring.boardtest.config;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 기본적으로 OAuth2UserService를 통해 사용자 정보를 가져옴
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate =
                new DefaultOAuth2UserService();

        // 사용자 정보 요청 후 처리
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // 여기서 추가적인 사용자 정보 처리 (예: DB에 저장하거나 추가적인 데이터 가져오기)
        // 예: 사용자 이름과 이메일을 가져와서 콘솔에 출력하는 예제
        String name = oAuth2User.getAttribute("name");
        String email = oAuth2User.getAttribute("email");
        System.out.println("OAuth2 User Name: " + name);
        System.out.println("OAuth2 User Email: " + email);

        // 기본적으로 가져온 사용자 정보를 반환
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                oAuth2User.getAttributes(),
                "name" // OAuth2 공급자가 사용하는 기본 속성 이름을 설정
        );
    }
}
