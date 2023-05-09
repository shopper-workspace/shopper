package com.shopper.shared.outbox;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OutboxEvent {

    private String aggregateType;
    private UUID aggregateId;
    private String type;
    private String payload;
}
