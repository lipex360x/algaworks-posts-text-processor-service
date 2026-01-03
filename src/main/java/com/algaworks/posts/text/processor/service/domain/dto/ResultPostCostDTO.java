package com.algaworks.posts.text.processor.service.domain.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class ResultPostCostDTO {
  private UUID postId;
  private Integer wordCount;
  private BigDecimal calculatedValue;
}
