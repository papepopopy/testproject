package com.spring.boardtest.repository;

import com.spring.boardtest.entity.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    @EntityGraph(attributePaths = "roleSet")
    Member findByEmail(String email);
}
