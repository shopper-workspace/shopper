package com.shopper.shared.outbox;

import java.io.Serializable;
import java.util.UUID;
import org.springframework.data.domain.Persistable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "processed_event")
@Getter
@Builder(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProcessedEventEntity implements Serializable, Persistable<UUID> {

    @Id
    @Column(name = "event_id")
    private UUID eventId;

    @Column(name = "timestamp")
    private long timestamp;

    @Transient
    @Override
    public UUID getId() {
        return eventId;
    }

    @Transient
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Ensures Hibernate always does an INSERT operation when save() is called.
     */
    @Transient
    @Override
    public boolean isNew() {
        return true;
    }
}
