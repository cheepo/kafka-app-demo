package ru.artemev.cosumer.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.artemev.cosumer.entity.ClientEntity;
import ru.artemev.cosumer.mapper.ClientEntityMapper;
import ru.artemev.cosumer.model.ClientModel;
import ru.artemev.cosumer.repository.ClientRepository;
import ru.artemev.cosumer.service.ConsumerService;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConsumerServiceImpl implements ConsumerService {

  private final ObjectMapper objectMapper;
  private final ClientEntityMapper clientEntityMapper;
  private final ClientRepository clientRepository;

  @Override
  @KafkaListener(topics = {"load-test-kafka"})
  public void takeMessageFromKafka(String message) {
    log.info("=== Start takeMessageFromKafka ===");
    try {
      ClientModel clientModel = objectMapper.readValue(message, ClientModel.class);
      // +++ CPU UTILIZATION +++
      //      log.info("Received model -> " + clientModel);
      ClientEntity clientEntity = clientEntityMapper.toClientEntity(clientModel);
      log.info("Created clientEntity -> " + clientEntity);
      clientRepository.save(clientEntity);

    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }

    log.info("=== Finish takeMessageFromKafka ===");
  }
}
