package com.spring.boardtest.service;

import com.spring.boardtest.dto.BoardDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@Log4j2
@TestPropertySource(locations = {"classpath:application.properties"})
class BoardServiceTest {

    @Autowired
    private BoardService boardService;

    @Test@DisplayName("생성")
    public void BoardRegisterTest() {
        log.info("=> " + boardService.getClass().getName());

        BoardDTO boardDTO = BoardDTO.builder()
                .bno(1L)
                .title("test title")
                .content("test content")
                .writer("test writer")
                .build();

        Long bno = boardService.register(boardDTO);
        log.info("board bno : " + bno);
    }
    
    @Test@DisplayName("조회")
    public void BoardReadTest() {
        Long testBno = 1L;
        BoardDTO result = boardService.readOne(testBno);
        log.info(result);
    }


}