package com.spring.boardtest.service;

import com.spring.boardtest.dto.MemberDTO;
import com.spring.boardtest.entity.Member;
import jakarta.servlet.http.HttpServletRequest;

public interface MemberService {
    Member saveMember(MemberDTO memberDTO, HttpServletRequest request);
}
