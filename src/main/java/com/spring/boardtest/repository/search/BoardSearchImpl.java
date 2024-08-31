package com.spring.boardtest.repository.search;


import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import com.spring.boardtest.dto.BoardListAllDTO;
import com.spring.boardtest.entity.Board;

import com.spring.boardtest.entity.QBoard;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class BoardSearchImpl extends QuerydslRepositorySupport implements BoardSearch {

  public BoardSearchImpl() {
    super(Board.class);
  }

  @Override
  public Page<Board> search(Pageable pageable) {
    QBoard board = QBoard.board;  // Entity -> QDomain
    // 1. where 추가
    JPQLQuery<Board> query = from(board);                      // select .. from board
    query.where(board.title.contains("1"));

    // 2. paging 추가
    this.getQuerydsl().applyPagination(pageable, query);
    // 3. 쿼리문 수행하여 결과 값을 List구조 반환
    List<Board> list = query.fetch();
    long count = query.fetchCount();

    return new PageImpl<>(list, pageable, count);
  }

  @Override
  public Page<Board> searchAll(String[] types, String keyword, Pageable pageable) {
    QBoard board = QBoard.board;  // Entity -> QDomain

    // 1. query 작성
    JPQLQuery<Board>  query = from(board);

    if ( (types != null && types.length > 0) && keyword != null){// 검색 키워드가 있으면
      BooleanBuilder booleanBuilder = new BooleanBuilder();

      for (String type : types){
          switch (type){
            case "t":
              booleanBuilder.or(board.title.contains(keyword));break;
            case "c":
              booleanBuilder.or(board.content.contains(keyword));break;
            case "w":
              booleanBuilder.or(board.writer.contains(keyword));break;
          }
      } // end for

      query.where(booleanBuilder);
    }// end if

    this.getQuerydsl().applyPagination(pageable, query);
    List<Board> list = query.fetch();
    long count = query.fetchCount();

    return new PageImpl<>(list, pageable, count);
  }

  // 게시물 조건 검색 조회 구현
  @Override
  public Page<BoardListAllDTO> searchWithAll(
                                String[] types,
                                String keyword,
                                Pageable pageable) {

    QBoard board = QBoard.board;

    // 1. 쿼리문 작성(댓글 기준으로 게시글 연결)
    JPQLQuery<Board> boardJPQLQuery = from(board);

    // 5. 검색 조건문 추가 : where 문 작성
    if ( (types != null && types.length > 0) && keyword != null){// 검색 키워드가 있으면
      BooleanBuilder booleanBuilder = new BooleanBuilder();

      for (String type : types){
        switch (type){
          case "t":
            booleanBuilder.or(board.title.contains(keyword));break;
          case "c":
            booleanBuilder.or(board.content.contains(keyword));break;
          case "w":
            booleanBuilder.or(board.writer.contains(keyword));break;
        }
      } // end for
      boardJPQLQuery.where(booleanBuilder);
    }// end if

    boardJPQLQuery.groupBy(board);
    getQuerydsl().applyPagination(pageable, boardJPQLQuery);

    List<BoardListAllDTO> dtoList = boardJPQLQuery.select(board)
            .fetch()
            .stream()
            .map(boardEntity -> BoardListAllDTO.builder()
                    .bno(boardEntity.getBno())
                    .title(boardEntity.getTitle())
                    .writer(boardEntity.getWriter())
                    .email(boardEntity.getEmail())
                    .regDate(boardEntity.getRegDate())
                    .build())
            .collect(Collectors.toList());

    long totalcount = boardJPQLQuery.fetchCount();

    return new PageImpl<>(dtoList, pageable, totalcount);

  }

}
