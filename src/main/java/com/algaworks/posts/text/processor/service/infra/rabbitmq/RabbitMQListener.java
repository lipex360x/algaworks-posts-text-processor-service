package com.algaworks.posts.text.processor.service.infra.rabbitmq;

import com.algaworks.posts.text.processor.service.domain.dto.CalculatePostCostDTO;
import com.algaworks.posts.text.processor.service.domain.service.CalculatePostCostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.algaworks.posts.text.processor.service.infra.rabbitmq.RabbitMQConfig.QUEUE_POST_PROCESSING;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQListener {

  private final CalculatePostCostService calculatePostCostService;

  @RabbitListener(queues = QUEUE_POST_PROCESSING, concurrency = "2-5")
  public void handle(
    @Payload CalculatePostCostDTO calculatePostCost,
    @Headers Map<String, Object> headers
  ) {
    calculatePostCostService.execute(calculatePostCost);
  }
}
