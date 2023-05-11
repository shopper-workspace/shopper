package com.shopper.notificationservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

@SpringBootApplication
@RequiredArgsConstructor
public class NotificationServiceApplication {

    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceApplication.class);

    private final ObjectMapper objectMapper;

    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }

    @KafkaListener(groupId = "notification-consumer", topics = "ORDER.events", containerFactory = "kafkaListenerContainerFactory")
    public void handleNotification(@Payload String payload) {

        try {
            OrderEvent.Order orderDto = objectMapper.readValue(payload, OrderEvent.Order.class);

            logger.info("Received Notification for Order: {}", orderDto.id());
        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
