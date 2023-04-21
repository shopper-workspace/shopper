package com.shopper.orderservice.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class OrderPlacedEvent extends ApplicationEvent {
    private String orderNumber;

    public OrderPlacedEvent(Object source, String orderNumber) {
        super(source);
        this.setOrderNumber(orderNumber);
    }

    public OrderPlacedEvent(String orderNumber) {
        super(orderNumber);
        this.setOrderNumber(orderNumber);
    }
}
