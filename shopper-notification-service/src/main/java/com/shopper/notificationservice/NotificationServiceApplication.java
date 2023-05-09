package com.shopper.notificationservice;

import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;

@SpringBootApplication
@RequiredArgsConstructor
public class NotificationServiceApplication {

    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceApplication.class);

    private final ObservationRegistry observationRegistry;
    private final Tracer tracer;
    private final ObjectMapper objectMapper;

    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }

    @KafkaListener(topics = "ORDER.events")
    public void handleNotification(@Payload String payload) {
        Observation.createNotStarted("on-message", this.observationRegistry).observe(() -> {
            String traceId = this.tracer.currentSpan().context().traceId();

            try {
                OrderEvent.Order orderDto = objectMapper.readValue(payload, OrderEvent.Order.class);
                UUID orderId = orderDto.id();

                logger.info("TraceId: {}, Received Notification for Order: {}", traceId, orderId);
            } catch (JsonMappingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JsonProcessingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
    }
}
