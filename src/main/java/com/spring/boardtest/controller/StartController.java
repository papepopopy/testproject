package com.spring.boardtest.controller;

import com.spring.boardtest.config.GoogleProperties;
import com.spring.boardtest.dto.MemberDTO;
import com.spring.boardtest.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Controller
@Log4j2
@RequestMapping("/")
@RequiredArgsConstructor
public class StartController {

  private final GoogleProperties googleProperties;
  private final MemberService memberService;

  //메인
  @GetMapping("")
  public String memberRegisterForm() {
    log.info("====> index");
    return "index";
  }

  //프로필 페이지
  @GetMapping("/profileForm")
  public String profileForm(Model model) {
    return "profileForm"; // profileForm.html 페이지로 이동
  }
  
  //로그인 페이지
  @GetMapping("/google_login")
  public String googleLogin(@RequestParam(value = "code", required = false) String code, Model model) {

    if (code != null) {
      // Access Token 요청
      log.info("Received code: " + code);
      Map<String, String> tokens = getAccessToken(code);

      // Access Token 정상 수신시
      if (tokens != null && tokens.containsKey("access_token")) {
        String accessToken = tokens.get("access_token");
        log.info("Access Token: {}", accessToken);
        MemberDTO user = getUserInfo(accessToken); //사용자 정보 요청

        if (user != null && user.getEmail() != null) {
          //사용자 저장
          memberService.saveMember(user);
          //프로필 페이지로 이동
          return "redirect:/profileForm";
        }
      }
      return "redirect:/"; // 실패 시 메인페이지로 이동
    } else {
      // 인증 코드가 없는 경우 Google OAuth2 인증 페이지로 리디렉션
      String reqUrl = "https://accounts.google.com/o/oauth2/v2/auth?client_id=" + googleProperties.getClientId()
              + "&redirect_uri=" + googleProperties.getRedirectUri()
              + "&response_type=code&scope=email%20profile%20openid&access_type=offline";
      return "redirect:" + reqUrl;
    }
  }

  // Access Token 요청 메소드
  private Map<String, String> getAccessToken(String code) {
    try {
      String tokenUrl = "https://oauth2.googleapis.com/token";
      String params = "code=" + code +
              "&client_id=" + googleProperties.getClientId() +
              "&client_secret=" + googleProperties.getClientSecret() +
              "&redirect_uri=" + googleProperties.getRedirectUri() +
              "&grant_type=authorization_code";

      RestTemplate restTemplate = new RestTemplate();
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

      HttpEntity<String> entity = new HttpEntity<>(params, headers);
      ResponseEntity<Map<String, String>> response = restTemplate.exchange(
              tokenUrl, HttpMethod.POST, entity,
              new ParameterizedTypeReference<Map<String, String>>() {});

      log.info("HTTP Status Code: {}", response.getStatusCode());
      log.info("Response Body: {}", response.getBody());

      if (response.getStatusCode() != HttpStatus.OK || response.getBody().containsKey("error")) {
        log.error("Error retrieving access token: {}", response.getBody());
        return null;
      }

      return response.getBody();
    } catch (Exception e) {
      log.error("Failed to get access token", e);
      return null;
    }
  }

  // 사용자 정보 요청 메소드
  private MemberDTO getUserInfo(String accessToken) {
    log.info("====> getAccessToken : " + accessToken);
    try {
      String userInfoUrl = "https://www.googleapis.com/oauth2/v3/userinfo";

      RestTemplate restTemplate = new RestTemplate();
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(accessToken);

      HttpEntity<String> entity = new HttpEntity<>(headers);
      ResponseEntity<MemberDTO> response = restTemplate.exchange(userInfoUrl, HttpMethod.GET,
              entity, MemberDTO.class);

      return response.getBody();
    } catch (Exception e) {
      log.error("Failed to get user info", e);
      return null;
    }
  }
}
