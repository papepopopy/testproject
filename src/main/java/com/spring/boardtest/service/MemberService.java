package com.spring.boardtest.service;

import com.spring.boardtest.dto.MemberDTO;
import com.spring.boardtest.entity.Member;

public interface MemberService {
    Member saveMember(MemberDTO memberDTO);
}
