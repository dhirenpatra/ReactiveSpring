package com.reactiveprogramming.reactivestream;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

public class FluxFilter {

    private List<String> namesList = Arrays.asList("Dhiren", "Kumar", "Disha");

    @Test
    public void fluxUsingFilterByName() {
        Flux<String> flux = Flux.fromIterable(namesList)
                                .filter(item -> item.startsWith("D")).log();

        StepVerifier.create(flux)
                .expectNext("Dhiren", "Disha")
                .verifyComplete();
    }

    @Test
    public void fluxUsingFilterByNameLength() {
        Flux<String> flux = Flux.fromIterable(namesList)
                .filter(item -> item.length() > 5).log();

        StepVerifier.create(flux)
                .expectNext("Dhiren")
                .verifyComplete();
    }

}
