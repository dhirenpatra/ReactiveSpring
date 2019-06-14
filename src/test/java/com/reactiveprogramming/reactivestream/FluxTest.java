package com.reactiveprogramming.reactivestream;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxTest {

    @Test
    public void fluxWithoutErrorVersion0() {

        Flux<String> fluxString = Flux.just("Spring", "Spring Boot", "Reactive Stream").log();

        StepVerifier.create(fluxString)
                .expectNext("Spring")
                .expectNext("Spring Boot")
                .expectNext("Reactive Stream")
                .verifyComplete();
    }

    @Test
    public void fluxWithoutErrorVersion1() {

        Flux<String> fluxString = Flux.just("Spring", "Spring Boot", "Reactive Stream").log();

        StepVerifier.create(fluxString)
                .expectNext("Spring", "Spring Boot", "Reactive Stream")
                .verifyComplete();
    }

    @Test
    public void fluxWithErrorClass() {
        Flux<String> fluxString = Flux.just("Spring", "Spring Boot", "Reactive Stream")
                .concatWith(Flux.error(new RuntimeException("Exception Thrown"))).log();

        StepVerifier.create(fluxString)
                .expectNext("Spring", "Spring Boot", "Reactive Stream")
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    public void fluxWithErrorMessage() {
        Flux<String> fluxString = Flux.just("Spring", "Spring Boot", "Reactive Stream")
                .concatWith(Flux.error(new RuntimeException("Exception Thrown"))).log();

        StepVerifier.create(fluxString)
                .expectNext("Spring", "Spring Boot", "Reactive Stream")
                .expectErrorMessage("Exception Thrown")
                .verify();
    }

    @Test
    public void fluxWithCountOfItems() {
        Flux<String> fluxString = Flux.just("Spring", "Spring Boot", "Reactive Stream").log();

        StepVerifier.create(fluxString)
                .expectNextCount(3)
                .verifyComplete();
    }
}
