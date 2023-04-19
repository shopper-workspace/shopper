package com.shopper.orderservice.controller;

import com.shopper.orderservice.dto.OrderRequest;
import com.shopper.orderservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name = "inventory", fallbackMethod = "resiliencyFallback")
    @TimeLimiter(name = "inventory", fallbackMethod = "resiliencyFallback")
    @Retry(name = "inventory", fallbackMethod = "resiliencyFallback")
    public CompletableFuture<String> placeOrder(@RequestBody OrderRequest orderRequest) {
        return CompletableFuture.supplyAsync(() -> orderService.placeOrder(orderRequest));
    }

    public CompletableFuture<String> resiliencyFallback(RuntimeException runtimeException) {
        return CompletableFuture.supplyAsync(() -> "Unable to process your request. Please try again later.");
    }
}
