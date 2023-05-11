package com.shopper.shared.outbox;

import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.micrometer.tracing.Tracer;

@Configuration
@AutoConfigurationPackage
public class OutboxConfiguration {

    @Bean
    OutboxEventService outboxService(Tracer tracer, OutboxEventRepository outboxEventRepository, ProcessedEventRepository processedEventRepository) {
        return new OutboxManager(tracer, outboxEventRepository, processedEventRepository);
    }
}
