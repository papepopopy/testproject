package com.spring.boardtest.controller;

import com.spring.boardtest.config.GoogleProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@Log4j2
@RequestMapping("/")
@RequiredArgsConstructor
public class StartController {

  // 하드코딩된 클라이언트 ID와 비밀 키
//  private final String clientId = "96181768947-eckgakvudg11uqfh4c07khmpkpgiq4i1.apps.googleusercontent.com";
//  private final String clientSecret = "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";

//  @Autowired
//  private final GoogleProperties googleProperties;

  @GetMapping("")
  public String memberRegisterForm(Model model){
    log.info("====> index");
    return "index";
  }

  @GetMapping("/google_login")
  public String googleLogin(GoogleProperties googleProperties) {
    log.info("====> google_login");
    log.info("googleProperties : " + googleProperties);
    // 인증 상태 확인
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    // 사용자가 인증되지 않았을 경우 Google 로그인 URL로 리디렉션
    if (authentication == null || !(authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken))) {
      log.info("User is already authenticated. Redirecting to the home page.");
//      log.info("clientId : " + clientId);
//      log.info("clientSecret : " + clientSecret);


      //구글 로그인 url
      String reqUrl = "https://accounts.google.com/o/oauth2/v2/auth?client_id=" + googleProperties.getClientId()
              + "&redirect_uri=http://localhost:5502/google_login"
              + "&response_type=code&scope=email%20profile%20openid&access_type=offline";
      return "redirect:" + reqUrl;
    }
    log.info("User is already authenticated. Redirecting to the home page.");
    return "redirect:/"; // 인증된 사용자라면 홈 페이지로 리디렉션
  }
}
