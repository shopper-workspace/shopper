package com.shopper.shared.outbox;

import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigurationPackage
public class OutboxConfiguration {

    @Bean
    OutboxEventService outboxService(OutboxEventRepository outboxEventRepository,
            ProcessedEventRepository processedEventRepository) {
        return new OutboxManager(outboxEventRepository, processedEventRepository);
    }
}
