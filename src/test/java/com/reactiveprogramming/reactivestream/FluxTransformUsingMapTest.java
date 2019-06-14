package com.reactiveprogramming.reactivestream;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

public class FluxTransformUsingMapTest {

    private List<String> namesList = Arrays.asList("Dhiren", "Kumar", "Disha");

    @Test
    public void transformFluxUsingMap_UpperCase() {

        Flux<String> flux = Flux.fromIterable(namesList).log()
                                .map(String::toUpperCase).log();

        StepVerifier.create(flux)
                    .expectNext("DHIREN","KUMAR","DISHA")
                    .verifyComplete();

    }

    @Test
    public void transformFluxUsingMap_Length() {

        Flux<Integer> flux = Flux.fromIterable(namesList).log()
                .map(String::length).log();

        StepVerifier.create(flux)
                    .expectNext(6,5,5)
                    .verifyComplete();

    }

    @Test
    public void transformFluxUsingMap_Length_Repeat() {

        Flux<Integer> flux = Flux.fromIterable(namesList).log()
                .map(String::length)
                .repeat(1)
                .log();

        StepVerifier.create(flux)
                .expectNext(6,5,5,6,5,5)
                .verifyComplete();

    }

    @Test
    public void transformFluxUsingMap_FilterLength_UpperCase() {

        Flux<String> flux = Flux.fromIterable(namesList).log()
                .filter(item -> item.length() >= 5)
                .filter(item -> item.startsWith("D"))
                .map(String::toUpperCase)
                .log();

        StepVerifier.create(flux)
                .expectNext("DHIREN", "DISHA")
                .verifyComplete();

    }

}
