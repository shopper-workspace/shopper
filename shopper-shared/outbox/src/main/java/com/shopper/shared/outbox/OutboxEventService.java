package com.shopper.shared.outbox;

import java.util.UUID;

public interface OutboxEventService {

    void processEvent(UUID eventId);

    void publishEvent(OutboxEvent event);
}
