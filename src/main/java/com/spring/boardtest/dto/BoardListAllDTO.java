package com.spring.boardtest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardListAllDTO {
  private Long bno;
  private String title;
  private String writer;
  private String email;
  private LocalDateTime regDate;
}
