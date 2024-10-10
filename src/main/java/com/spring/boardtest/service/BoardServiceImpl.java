package com.spring.boardtest.service;

import com.spring.boardtest.dto.*;
import com.spring.boardtest.entity.Board;
import com.spring.boardtest.repository.BoardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class BoardServiceImpl implements BoardService {
  private final ModelMapper modelMapper;
  private final BoardRepository boardRepository;

  @Override
  public long register(BoardDTO boardDTO) {
    // 1. DTO -> Entity : (entity와 dto 동일 구조일 경우 ) 첨부파일 없는 경우
    //Board board = modelMapper.map(boardDTO, Board.class);

    // 2. DTO -> Entity  첨부파일 추가한 경우
    Board board = dtoToEntity(boardDTO);

    // 3. board entity 저장
    Long bno = boardRepository.save(board).getBno();
    //Board savedBoard = boardRepository.save(board);

    return bno;
  }

  @Override
  public BoardDTO readOne(Long bno) {
    // 1. fetch = FetchType.LAZY 상태일 경우 boardImage 즉시로딩안됨
    // Optional<Board> result = boardRepository.findById(bno);

    // 2. fetch = FetchType.LAZY 상태인 경우에도 즉시로딩 (@EntityGraph)
    Optional<Board> result = boardRepository.findByIdWidthImages(bno);

    // optional -> entity
    Board board = result.orElseThrow();

    // entity -> dto 맵핑 (entity와 dto 동일 구조일 경우 )
    // BoardDTO boardDTO = modelMapper.map(board, BoardDTO.class);
    BoardDTO boardDTO = entityToDto(board);

    return boardDTO;
  }

  @Override
  public Board modify(BoardDTO boardDTO) {
    // 수정할 글번호 읽어오기
    Optional<Board> result = boardRepository.findById(boardDTO.getBno());
    Board board = result.orElseThrow();
    // entity값을 dto값으로 변경
    board.change(boardDTO.getTitle(), boardDTO.getContent());


    // 저장하기
    Board modBoard = boardRepository.save(board);

    return modBoard;// 저장된 entity
  }

  @Override
  public void remove(Long bno) {
    boardRepository.deleteById(bno);
  }

  @Override
  public PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO) {
    // 검색 조건에 대한 처리
    String[] types = pageRequestDTO.getTypes();
    String keyword = pageRequestDTO.getKeyword();
    Pageable pageable = pageRequestDTO.getPageable("bno");

    // 조건 검색 및 페이징한 결과값 가져오기
    Page<Board> result = boardRepository.searchAll(types, keyword, pageable);

    // Page객체 있는 내용을 List구조 가져오기
    List<BoardDTO> dtoList =
            result.getContent()
                    .stream()
                    // collection구조에 있는 entity를 하나씩 dto으로 변화하여 List구조에 저장
                    .map( board -> modelMapper.map(board,BoardDTO.class ) )
                    .collect(Collectors.toList());

    // 매개변수로 전달받은 객체(pageRequestDTO)를 가지고 PageResponseDTO.Builder()를 통해
    // PageRequestDTO객체 생성되어 필요시 스프링이 필요시점에 주입 시켜줌(list에서 pageRequestDTO객체 사용가능함 )
    return PageResponseDTO.<BoardDTO>withAll()
            .pageRequestDTO(pageRequestDTO)
            .dtoList(dtoList)
            .total((int)result.getTotalElements())
            .build();
  }

}