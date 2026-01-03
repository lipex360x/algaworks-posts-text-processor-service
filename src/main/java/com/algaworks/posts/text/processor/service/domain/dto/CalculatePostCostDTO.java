package com.algaworks.posts.text.processor.service.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CalculatePostCostDTO {
  private String postId;
  private String postBody;
}
