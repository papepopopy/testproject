package com.spring.boardtest.service;


import com.spring.boardtest.dto.*;
import com.spring.boardtest.entity.Board;


public interface BoardService {

  // 1. 게시글 등록 서비스 인터페이스
  long register(BoardDTO boardDTO);
  // 2. 게시글 조회
  BoardDTO readOne(Long bno);
  // 3. 게시글 수정
  Board modify(BoardDTO boardDTO);
  // 4. 게시글 삭제
  void remove(Long bno);
  // 5. 게시글 목록: 페이징 처리를 한 게시글 목록
  PageResponseDTO<BoardDTO> list (PageRequestDTO pageRequestDTO);


  // 7. DTO -> entity 변환 : 등록 기능
  // List<Setring> fileName -> Board에서 Set<boardImage>타입으로 변환
  default Board dtoToEntity(BoardDTO boardDTO){
    // getter dto -> setter entity -> db table 저장
    Board board = Board.builder()
            .bno(boardDTO.getBno())
            .title(boardDTO.getTitle())
            .content(boardDTO.getContent())
            .email(boardDTO.getEmail())
            .writer(boardDTO.getWriter())
            .build();

    return board;
  } // end dtoToEntity


  //  8. entity -> dto : 조회 기능
  default BoardDTO entityToDto(Board board){
    // getter entity -> setter dto
    BoardDTO boardDTO = BoardDTO.builder()
            .bno(board.getBno())
            .title(board.getTitle())
            .email(board.getEmail())
            .writer(board.getWriter())
            .content(board.getContent())
            .regDate(board.getRegDate())
            .modDate(board.getModDate())
            .build();


    return boardDTO;
  }

}