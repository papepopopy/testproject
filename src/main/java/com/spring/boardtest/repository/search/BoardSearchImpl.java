package com.spring.boardtest.repository.search;


import com.querydsl.core.BooleanBuilder;
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

@Log4j2
public class BoardSearchImpl extends QuerydslRepositorySupport implements BoardSearch {

  public BoardSearchImpl() {
    super(Board.class);
  }

  @Override
  public Page<Board> search1(Pageable pageable) {
    log.info("=> Page...");

    return null;
  }


  @Override
  public Page<Board> search2(Pageable pageable) {
    log.info("=> 페이징2 ");

    QBoard board = QBoard.board;  // Entity -> QDomain

    // JPQLQuery<Board> => jsp: PreParseStatement 역할
    // 1. where 추가
    JPQLQuery<Board> query = from(board);                      // select .. from board
    query.where(board.title.contains("1"));

    this.getQuerydsl().applyPagination(pageable,query);

    List<Board> list = query.fetch();
    long count = query.fetchCount();
    log.info("=> "+count);

    BooleanBuilder booleanBuilder = new BooleanBuilder();

    booleanBuilder.or(board.title.contains("1"));
    booleanBuilder.or(board.content.contains("1"));

    JPQLQuery<Board>  query2 = from(board);
    query2.where(booleanBuilder);
    query2.where(board.bno.gt(0L));//and 연결

    this.getQuerydsl().applyPagination(pageable,query);
    // query
    List<Board> list2 = query.fetch();
    long count2 = query2.fetchCount();
    log.info("=> "+count2);


    return new PageImpl<>(list2, pageable, count);

  }

  @Override
  public Page<Board> searchAll(String[] types, String keyword, Pageable pageable) {

    QBoard board = QBoard.board;  // Entity -> QDomain

    // 1. query 작성
    JPQLQuery<Board>  query = from(board);

    if ( (types != null && types.length > 0) && keyword != null){// 검색 키워드가 있으면
      //  BooleanBuilder: 조건문을 작성하는 클래스
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
      }
      query.where(booleanBuilder);
    }


    this.getQuerydsl().applyPagination(pageable,query);
    List<Board> list = query.fetch();
    long count = query.fetchCount();

    return new PageImpl<>(list, pageable, count);
  }

  @Override
  public Page<BoardListAllDTO> searchWithAll(String[] types, String keyword, Pageable pageable) {

    return null;
  }

}