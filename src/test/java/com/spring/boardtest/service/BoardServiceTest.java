package com.spring.boardtest.service;

import com.spring.boardtest.dto.BoardDTO;
import com.spring.boardtest.dto.PageRequestDTO;
import com.spring.boardtest.dto.PageResponseDTO;
import com.spring.boardtest.entity.Board;
import lombok.extern.log4j.Log4j2;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
class BoardServiceTest {
    @Autowired
    private BoardService boardService;
    @Test@DisplayName("게시글 등록 서비스 테스트")
    public void testBoardReggisterTest(){
        log.info("=> "+boardService.getClass().getName());
        // 게시글 더미 데이터 생성
        BoardDTO boardDTO = BoardDTO.builder()
                .title("Sample Title...")
                .content("Sample Content...")
                .writer("user00")
                .build();
        Long bno = boardService.register(boardDTO);
        log.info("=> bno: "+bno);
    }
    @Test@DisplayName("게시글 수정 서비스 테스트")
    public void testBoardModifyTest(){
        // 게시글 더미 데이터 생성
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(102L)
                .title("Sample Title...102")
                .content("Sample Content..102.")
                .build();
        Board modBoard = boardService.modify(boardDTO);
        // Assertions.assertThat(a).isEqualTo(b): 대상 a가  기대값 b와 같은지 확인
        Assertions.assertThat(modBoard.getTitle()).isEqualTo(boardDTO.getTitle());
        Assertions.assertThat(modBoard.getContent()).isEqualTo(boardDTO.getContent());
        // test 일치하지 않을 경우
        //Assertions.assertThat("100").isEqualTo(boardDTO.getContent());
    }
    @Test@DisplayName("게시글 삭제 서비스 테스트")
    public void testBoardDeleteTest(){
        long bno = 102L;
        boardService.remove(bno);
    }

    // 페이징 처리 List
    @Test@DisplayName("게시글 페이징 목록 서비스 테스트")
    public void testBoardListTest(){
        // 더미 페이징 설정
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .type("tcw")
                .keyword("1")
                .page(1)
                .size(10)
                .build();

        PageResponseDTO<BoardDTO> responseDTO = boardService.list(pageRequestDTO);
        log.info("\n=> "+responseDTO);
    }

}