package com.reactiveprogramming.reactivestream;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class FluxAndMonoFactories {

    private List<String> namesList = Arrays.asList("Dhiren", "Kumar");

    @Test
    public void fluxUsingIterables() {
        Flux<String> namesFlux = Flux.fromIterable(namesList)
                .concatWith(Flux.error(new RuntimeException("ERROR")))
                .log();

        StepVerifier.create(namesFlux)
                .expectNext("Dhiren")
                .expectNext("Kumar")
                .expectErrorMessage("ERROR")
                .verify();
    }

    @Test
    public void fluxUsingArrays() {
        Flux<String> namesFlux = Flux.fromArray(new String[] {"Dhiren", "Kumar"})
                .concatWith(Flux.error(new RuntimeException("ERROR")))
                .log();

        StepVerifier.create(namesFlux)
                .expectNext("Dhiren")
                .expectNext("Kumar")
                .expectErrorMessage("ERROR")
                .verify();
    }

    @Test
    public void fluxUsingStream() {
        Flux<String> namesFlux = Flux.fromStream(namesList.stream())
                .concatWith(Flux.error(new RuntimeException("ERROR")))
                .log();

        StepVerifier.create(namesFlux)
                .expectNext("Dhiren","Kumar")
                .expectErrorMessage("ERROR")
                .verify();
    }

    @Test
    public void fluxUsingRange() {
        Flux<Integer> namesFlux = Flux.range(1,10)
                .concatWith(Flux.error(new RuntimeException("ERROR")))
                .log();

        StepVerifier.create(namesFlux)
                .expectNext(1,2,3,4,5,6,7,8,9,10)
                .expectErrorMessage("ERROR")
                .verify();
    }

    @Test
    public void monoUsingJustOrEmpty() {
        Mono<String> emptyMono = Mono.justOrEmpty(Optional.empty());
        StepVerifier.create(emptyMono.log())
                .verifyComplete();
    }

    @Test
    public void monoUsingSupplier() {

        Supplier<String> stringSupplier = () -> "Dhiren";

        Mono<String> emptyMono = Mono.fromSupplier(stringSupplier).log();
        StepVerifier.create(emptyMono)
                .expectNext("Dhiren")
                .verifyComplete();
    }



}

