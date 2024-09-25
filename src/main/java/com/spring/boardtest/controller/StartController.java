package com.spring.boardtest.controller;

import com.spring.boardtest.config.GoogleProperties;
import com.spring.boardtest.dto.MemberDTO;
import com.spring.boardtest.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
  public String googleLogin(@RequestParam(value = "code", required = false) String code,
                            HttpServletRequest request) {
    HttpSession session = request.getSession();
    String existingAccessToken = (String) session.getAttribute("access_token");
    log.info("Existing Access Token from session: {}", existingAccessToken);

    // 기존 세션에 토큰이 있다면 바로 프로필 페이지로 이동
    if (existingAccessToken != null) {
      log.info("Existing Access Token found");
      return "redirect:/profileForm";  // 이미 로그인된 사용자라면 프로필 페이지로 이동
    }

    // 인증 코드가 없으면 Google 인증 서버로 리디렉션
    if (code == null) {
      log.info("No code found, redirecting to Google OAuth2");
      String reqUrl = "https://accounts.google.com/o/oauth2/v2/auth?client_id="
              + googleProperties.getClientId()
              + "&redirect_uri=" + googleProperties.getRedirectUri()
              + "&response_type=code&scope=email%20profile%20openid&access_type=offline";
      return "redirect:" + reqUrl;
    }

    // 코드가 있으면 Access Token을 요청하고 사용자 정보를 가져옴
    Map<String, String> tokens = getAccessToken(code);
    if (tokens != null && tokens.containsKey("access_token")) {
      String accessToken = tokens.get("access_token");
      session.setAttribute("access_token", accessToken);

      // 사용자 정보를 가져와 세션에 저장하거나 DB에 저장
      MemberDTO user = getUserInfo(accessToken);
      memberService.saveMember(user, request);

      return "redirect:/profileForm";  // 성공 시 프로필 페이지로 이동
    }
//    if (code != null) {
//      // Access Token 요청
//      log.info("Received code: " + code);
//      Map<String, String> tokens = getAccessToken(code);
//
//      // Access Token 정상 수신시
//      if (tokens != null && tokens.containsKey("access_token")) {
//        String accessToken = tokens.get("access_token");
//        log.info("Access Token: {}", accessToken);
//
//        //세션에 Access Token 저장
//        session.setAttribute("access_token", accessToken);
//        log.info("Access Token stored in session: {}", session.getAttribute("access_token"));
//
//        //사용자 정보 요청 및 저장
//        MemberDTO user = getUserInfo(accessToken);
//        memberService.saveMember(user, request);
//
//        //프로필 페이지로 이동
//        return "redirect:/profileForm";
//      }
//      return "redirect:/"; // 실패 시 메인페이지로 이동
//    } else {
//      log.info("No code found, redirecting to Google OAuth2");
//      // 인증 코드가 없는 경우 Google OAuth2 인증 페이지로 리디렉션
//      String reqUrl = "https://accounts.google.com/o/oauth2/v2/auth?client_id="
//              + googleProperties.getClientId()
//              + "&redirect_uri=" + googleProperties.getRedirectUri()
//              + "&response_type=code&scope=email%20profile%20openid&access_type=offline";
//      return "redirect:" + reqUrl;
//    }
    return "redirect:/";  // 실패 시 메인 페이지로 이동
  }

  // Access Token 요청 메소드
  // Google로부터 인증 코드를 받아 Access Token을 요청
  private Map<String, String> getAccessToken(String code) {

    try {
      log.info("Access Token =====>");
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
  // Access Token을 사용하여 요청 후 사용자 정보를 들고오는 페이지
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
