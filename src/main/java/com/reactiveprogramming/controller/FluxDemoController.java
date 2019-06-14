package com.reactiveprogramming.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;

@RestController
public class FluxDemoController {

    @GetMapping("/regular")
    public Flux<Integer> getIntegerId() {
        return Flux.just(1,2,3,4);
    }

    @GetMapping("/delay")
    public Flux<Integer> getIntegerIdInDelay() {
        return Flux.just(1,2,3,4,5).delayElements(Duration.ofMillis(100));
    }

    @GetMapping(value = "/stream", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Integer> getIntegerInStream() {
        return Flux.just(1,2,3,4,5).delayElements(Duration.ofSeconds(1));
    }

    @GetMapping(value = "/stream/infinite", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Long> getIntegerInfiniteStream() {
        return Flux.interval(Duration.ofSeconds(1));
    }

}
