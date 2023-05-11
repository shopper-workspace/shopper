package com.shopper.shared.outbox;

import java.util.UUID;
import org.hibernate.Length;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "outbox_event")
@Getter
@Builder(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OutboxEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String aggregateType;

    @Column(nullable = false)
    private UUID aggregateId;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private long timestamp;

    @Column(nullable = false, length = Length.LOB_DEFAULT)
    private String payload;

    @Setter
    @Column(nullable = true)
    private String traceParent;

    public static OutboxEventEntity fromEvent(OutboxEvent event) {
        return OutboxEventEntity.builder()
                .aggregateType(event.getAggregateType())
                .aggregateId(event.getAggregateId())
                .type(event.getType())
                .timestamp(System.currentTimeMillis())
                .payload(event.getPayload())
                .build();
    }
}
