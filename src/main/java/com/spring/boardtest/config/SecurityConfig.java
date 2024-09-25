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
                        .requestMatchers("/", "/css/**", "/js/**", "/google_login**", "/login/**",  "/oauth2/**").permitAll() // 리소스와 로그인 페이지 허용
                        .anyRequest().authenticated() // 그 외 모든 요청은 인증 필요
                )
                .oauth2Login(oauth -> oauth
                        .loginPage("/google_login") // 로그인 페이지
                        .defaultSuccessUrl("/profileForm", true) // 로그인 성공 시 리다이렉트할 URL
                        .failureUrl("/login?error=true") // 로그인 실패 시 리다이렉트할 URL
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // 로그아웃 처리 경로
                        .logoutSuccessUrl("/login?logout=true") // 로그아웃 성공 후 리다이렉트할 URL
                )
                .csrf(csrf -> csrf.disable()); // 필요시 CSRF 보호 비활성화 (API 사용 시)

        return http.build();
    }
    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        return authenticationManagerBuilder.build();
    }
}
