package ru.artemev.producer.service;

import java.io.IOException;

public interface ProducerService {
  void addFileToTopic() throws IOException;
}
