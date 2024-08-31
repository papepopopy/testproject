package com.spring.boardtest.repository.search;

import com.spring.boardtest.dto.BoardListAllDTO;
import com.spring.boardtest.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


// 단순 페이지 처리 기능 설게
public interface BoardSearch {

  Page<Board> search(Pageable pageable);

  // 검색어가 포함된 페이징, Pageable인자는 마지막에 위치할 것
  Page<Board> searchAll(String[] types, String keyword, Pageable pageable);

  // 게시물 조건 검색 조회 인터페이스
  Page<BoardListAllDTO> searchWithAll(String[] types, String keyword, Pageable pageable);

}
