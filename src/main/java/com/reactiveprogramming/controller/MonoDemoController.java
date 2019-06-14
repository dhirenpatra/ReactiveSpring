package com.reactiveprogramming.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Optional;

@RestController
@RequestMapping("/mono")
public class MonoDemoController {

    @GetMapping
    public Mono<Integer> getIntegerId() {
        return Mono.just(1);
    }


}
