package com.shopper.shared.outbox;

import java.text.MessageFormat;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OutboxManager implements OutboxEventService {

    private static final Logger logger = LoggerFactory.getLogger(OutboxManager.class);

    private final Tracer tracer;

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
            logger.error("Error while persisting event log entry for {}: ", eventId, e.getMessage());
            throw e;
        }
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void publishEvent(OutboxEvent event) {
        try {
            OutboxEventEntity outboxEvent = OutboxEventEntity.fromEvent(event);

            // Propagate traceheader for otel (W3C)
            Span currentSpan = this.tracer.currentSpan();
            if (currentSpan != null) {
                String traceId = currentSpan.context().traceId();
                String spanId = currentSpan.context().spanId();
                String traceParent = MessageFormat.format("00-{0}-{1}-01", traceId, spanId);
                outboxEvent.setTraceParent(traceParent);
            }

            // Trigger CDC from tx logs, clear outbox
            UUID outboxEventId = outboxEventRepository.save(outboxEvent).getId();
            outboxEventRepository.delete(outboxEvent);

            logger.debug("Event persisted to transactional outbox with Id: {}.", outboxEventId);
        } catch (RuntimeException e) {
            logger.error("Error while persisting transactional outbox entry for {}: ", event.getAggregateId(), e.getMessage());
            throw e;
        }
    }
}
