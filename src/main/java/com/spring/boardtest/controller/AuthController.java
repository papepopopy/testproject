package com.spring.boardtest.controller;

import com.spring.boardtest.entity.GoogleAuth;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final JwtDecoder jwtDecoder;

    @PostMapping("/google")
    public ResponseEntity<GoogleAuth> googleLogin(@RequestParam("credential") String jwt) {
        GoogleAuth auth = jwtDecoder.decode(jwt, GoogleAuth.class);
        return ResponseEntity.ok(auth);
    }

}
