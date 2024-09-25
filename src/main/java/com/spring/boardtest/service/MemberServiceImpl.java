package com.spring.boardtest.service;

import com.spring.boardtest.dto.MemberDTO;
import com.spring.boardtest.entity.Member;
import com.spring.boardtest.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final AuthenticationManager authenticationManager;

    @Override
    public Member saveMember(MemberDTO memberDTO, HttpServletRequest request)  {
        //기존 회원
        Member findMember = memberRepository.findByEmail(memberDTO.getEmail()).orElse(null);

        if (findMember != null) {
            //기존 회원 로그인
            authenticateUserWithEmail(findMember.getEmail());
            return findMember;
        }

        // 새로운 회원 저장
        Member newMember = new Member();
        newMember.setEmail(memberDTO.getEmail());
        newMember.setName(memberDTO.getName());
//        newMember.setPassword(null); // 비밀번호 필요 없음

        //새로운 사용자
        memberRepository.save(newMember);

        //로그인 처리
        authenticateUserWithEmail(newMember.getEmail());
        return newMember;
    }

    private void authenticateUserWithEmail(String email) {
        // 비밀번호 없는 인증 로직
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email, null); // 고정된 비밀번호 사용
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication); // 세션에 인증 정보 저장
    }

}
