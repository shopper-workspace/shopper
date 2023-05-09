package com.shopper.shared.outbox;

import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OutboxManager implements OutboxEventService {

    private static final Logger logger = LoggerFactory.getLogger(OutboxManager.class);

    private final OutboxEventRepository outboxEventRepository;
    private final ProcessedEventRepository processedEventRepository;

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void processEvent(UUID eventId) {

        try {
            processedEventRepository.saveAndFlush(new ProcessedEventEntity(eventId, System.currentTimeMillis()));

            logger.debug("Event persisted to event log with Id: {}.", eventId);
        } catch (DataIntegrityViolationException e) {
            logger.debug("Event already processed: {}", eventId);
            throw new DuplicateEventException(eventId.toString());
        } catch (RuntimeException e) {
            logger.error("Error while persisting event log entry for {}: ", eventId, e.getCause());
            throw e;
        }
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void publishEvent(OutboxEvent event) {

        try {
            OutboxEventEntity outboxEvent = OutboxEventEntity.fromEvent(event);
            UUID outboxEventId = outboxEventRepository.save(outboxEvent).getId();

            // CDC transaction log captured, clear outbox
            outboxEventRepository.delete(outboxEvent);

            logger.debug("Event persisted to transactional outbox with Id: {}.", outboxEventId);
        } catch (RuntimeException e) {
            logger.error("Error while persisting transactional outbox entry for {}: ", event.getAggregateId(), e.getCause());
            throw e;
        }
    }
}
