package ru.artemev.producer.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.json.CDL;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.artemev.producer.service.ProducerService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProducerServiceImpl implements ProducerService {

  private final KafkaTemplate<String, String> kafkaTemplate;

  @Value("${json.dir.path}")
  private Path jsonDir;

  @Value("${done.dir.path}")
  private Path jsonDirDone;

  @Value("${kafka.topic}")
  private String kafkaTopic;

  @Override
  @EventListener(ApplicationReadyEvent.class)
  public void addFileToTopic() {
    log.info("=== Start addFileToTopic ===");

    log.info("Get json dir path " + jsonDir);
    log.info("Get json done dir path " + jsonDirDone);

    if (!jsonDir.toFile().exists()) {
      jsonDir.toFile().mkdir();
    }

    if (!jsonDirDone.toFile().exists()) {
      jsonDirDone.toFile().mkdir();
    }
    log.info("Waiting files...");
    while (true) {
      List<File> files =
          Arrays.stream(Objects.requireNonNull(jsonDir.toFile().listFiles()))
              .filter(e -> !e.toString().equals(jsonDirDone.toString()))
              .filter(e -> !e.isHidden())
              .toList();

      if (files.isEmpty()) continue;

      File file = files.get(0);

      if (file.canRead()) sendFileToKafka(file);
    }
  }

  private void sendFileToKafka(File file) {
    switch (Objects.requireNonNull(FilenameUtils.getExtension(String.valueOf(file)))) {
      case "json" -> {
        log.info("Start send JSON file");

        sendLinesFromJsonFile(file.toPath());
        moveFileToDoneDir(file.toPath());

        log.info("Finish send JSON file");
      }
      case "csv" -> {
        log.info("Start send CSV file");

        sendLinesFromJsonFile(convertCsvFileToJsonFile(file.toPath()));
        // Json File сохранится в папке done вместо csv
        try {
          Files.delete(file.toPath());
        } catch (IOException e) {
          log.error("Error deleting " + file.getName());
          e.printStackTrace();
        }

        log.info("Finish send CSV file");
      }
      default -> {
        log.warn("I found an unknown file (" + file.getName() + ") and deleted it)");
        try {
          Files.delete(file.toPath());
        } catch (IOException e) {
          log.error("Error deleting " + file.getName());
          e.printStackTrace();
        }
      }
    }
  }

  private Path convertCsvFileToJsonFile(Path path) {
    log.info("=== Start convertCsvToPath");
    try {
      InputStream inputStream = new FileInputStream(path.toFile());
      String csvString =
          new BufferedReader(new InputStreamReader(inputStream))
              .lines()
              .collect(Collectors.joining("\n"));
      String json = CDL.toJSONArray(csvString).toString().replace("},{", "}\n{");
      Path pathJson =
          Path.of(jsonDirDone + "/" + FilenameUtils.getBaseName(String.valueOf(path)) + ".json");
      Files.writeString(pathJson, json);
      log.info("=== Finish convertCsvToPath");
      return pathJson;
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  private void sendLinesFromJsonFile(Path path) {
    log.info("=== Start send lines from " + path.toString());
    try {
      InputStream inputStream = new FileInputStream(path.toFile());
      Stream<String> clientModelStream =
          new BufferedReader(new InputStreamReader(inputStream))
              .lines()
              .map(e -> e.replace("}, {", "}\n{"))
              .map(e -> e.replaceAll("[\\[\\]]", ""));
      clientModelStream.forEach(
          e -> kafkaTemplate.send(kafkaTopic, UUID.randomUUID().toString(), e));
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
    log.info("Finish sendJsonLines");
  }

  private void moveFileToDoneDir(Path src) {
    Path doneDir = Path.of(jsonDirDone.toString() + "/" + src.getFileName());
    try {
      if (src.toFile().canRead()) {
        // Перезаписывает файл, если он уже есть в директории
        Files.move(src, doneDir, StandardCopyOption.REPLACE_EXISTING);
      } else {
        log.warn("I can't read src " + src + " or write to doneDir...");
      }
    } catch (IOException e) {
      log.error("Exception while moving file: " + e.getMessage());
      e.printStackTrace();
    }
  }
}
