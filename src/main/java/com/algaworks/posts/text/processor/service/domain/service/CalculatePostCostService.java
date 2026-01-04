package com.algaworks.posts.text.processor.service.domain.service;

import com.algaworks.posts.text.processor.service.domain.dto.CalculatePostCostDTO;
import com.algaworks.posts.text.processor.service.domain.dto.ResultPostCostDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

import static com.algaworks.posts.text.processor.service.infra.rabbitmq.RabbitMQConfig.DIRECT_EXCHANGE_POST_PROCESSING;
import static com.algaworks.posts.text.processor.service.infra.rabbitmq.RabbitMQConfig.ROUTING_KEY_POST_PROCESSING_RESULT;

@Slf4j
@Service
@RequiredArgsConstructor
public class CalculatePostCostService {

  private static final Double WORD_COST = 0.10;
  private final RabbitTemplate rabbitTemplate;

  public void execute(CalculatePostCostDTO input) {
    Integer wordCount = numberOfWords(input.getPostBody());
    BigDecimal postCost = calculateCost(wordCount);
    ResultPostCostDTO postCostResult = ResultPostCostDTO.builder()
      .postId(UUID.fromString(input.getPostId()))
      .wordCount(wordCount)
      .calculatedValue(postCost)
      .build();
    sendPostToProcessCost(postCostResult);
  }

  private int numberOfWords(String post) {
    return post.trim().split("\\s+").length;
  }

  private BigDecimal calculateCost(Integer words) {
    return BigDecimal.valueOf(words).multiply(BigDecimal.valueOf(WORD_COST));
  }

  private void sendPostToProcessCost(ResultPostCostDTO payload) {
    MessagePostProcessor messagePostProcessor = message -> {
      message.getMessageProperties().setHeader("postId", payload.getPostId());
      return message;
    };
    rabbitTemplate.convertAndSend(DIRECT_EXCHANGE_POST_PROCESSING, ROUTING_KEY_POST_PROCESSING_RESULT, payload, messagePostProcessor);
    log.info("Post id {} sent to queue", payload.getPostId());
  }

}
