package com.spring.boardtest.repository.search;

import com.spring.boardtest.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardSearch {
    Page<Board> search1(Pageable pageable);
    Page<Board> search2(Pageable pageable);

    Page<Board> searchAll(String[] types, String keyword, Pageable pageable);
}
