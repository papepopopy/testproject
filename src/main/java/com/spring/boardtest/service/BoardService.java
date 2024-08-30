package com.spring.boardtest.service;


import com.spring.boardtest.dto.BoardDTO;
import com.spring.boardtest.dto.PageRequestDTO;
import com.spring.boardtest.dto.PageResponseDTO;
import com.spring.boardtest.entity.Board;

public interface BoardService {
    // 게시글 등록 서비스 인터페이스
    long register(BoardDTO boardDTO);
    // 게시글 조회
    // 2. 게시글 조회
    BoardDTO readOne(Long bno);
    // 게시글 수정
    // 3. 게시글 수정
    Board modify(BoardDTO boardDTO);
    // 게시글 삭제
    // 4. 게시글 삭제
    void remove(Long bno);
    // 게시글 목록: 페이징 처리를 한 게시글 목록
    // 5. 게시글 목록: 페이징 처리를 한 게시글 목록
    PageResponseDTO<BoardDTO> list (PageRequestDTO pageRequestDTO);
}
