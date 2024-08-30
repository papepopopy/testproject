package com.spring.boardtest.repository;

import com.spring.boardtest.entity.Board;
import com.spring.boardtest.repository.search.BoardSearch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardSearch {

}
