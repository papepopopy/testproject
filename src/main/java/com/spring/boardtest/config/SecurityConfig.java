package com.spring.boardtest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/css/**", "/js/**","/google_login**").permitAll()
                    .requestMatchers("/").authenticated()
                    .anyRequest().authenticated() //그 외의 요청의 경우 인증 피료
            )
            .oauth2Login(oauth -> oauth
                    .loginPage("/google_login") // 로그인 페이지 URL
                    .defaultSuccessUrl("/", true) //인증 성공시
                    .failureUrl("/login?error=true")
            ) // 인증 실패 시 리다이렉션할 URL
            .logout(logout -> logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login?logout=true")
            );// 로그아웃 성공 시 리디렉션할 URL
        return http.build();
    }

}