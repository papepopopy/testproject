package com.spring.boardtest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig implements WebMvcConfigurer {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/css/**", "/js/**", "/images/**", "/loginForm**", "/login/**", "/oauth2/**").permitAll() // 리소스와 로그인 관련 경로 허용
                        .anyRequest().authenticated() // 그 외 모든 요청은 인증 필요
                )
                .oauth2Login(oauth -> oauth
                        .loginPage("/loginForm") // 로그인 페이지 지정
                        .defaultSuccessUrl("/profileForm", true) // 로그인 성공 시 프로필 페이지로 리다이렉트
                        .failureUrl("/login?error=true") // 로그인 실패 시 리다이렉트할 URL
                        .userInfoEndpoint(userInfo -> userInfo
                            .userService(customOAuth2UserService()) // Custom OAuth2 사용자 서비스 사용
                        ) // 사용자 정보를 가져오는 엔드포인트
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // 로그아웃 처리 경로
                        .logoutSuccessUrl("/login?logout=true") // 로그아웃 성공 후 리다이렉트할 URL
                        .invalidateHttpSession(true) // 로그아웃 시 세션 무효화
                        .deleteCookies("JSESSIONID") // JSESSIONID 쿠키 삭제
                )
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/**") // API 경로에 대해 CSRF 비활성화
                );

        return http.build();
    }
    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        return authenticationManagerBuilder.build();
    }
    @Bean
    public CustomOAuth2UserService customOAuth2UserService() {
        return new CustomOAuth2UserService();
    }
}
