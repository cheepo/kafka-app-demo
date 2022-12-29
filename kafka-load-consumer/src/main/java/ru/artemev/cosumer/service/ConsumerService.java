package ru.artemev.cosumer.service;

public interface ConsumerService {
  void takeMessageFromKafka(String message);
}
