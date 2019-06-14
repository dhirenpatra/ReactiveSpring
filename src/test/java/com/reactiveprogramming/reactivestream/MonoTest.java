package com.reactiveprogramming.reactivestream;

import org.junit.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class MonoTest {

    @Test
    public void fluxWithoutError() {

        Mono<String> fluxString = Mono.just("Spring").log();

        StepVerifier.create(fluxString)
                .expectNext("Spring")
                .verifyComplete();
    }

    @Test
    public void fluxWithErrorClass() {

        StepVerifier.create(Mono.error(new RuntimeException("Mono Throws Exception")).log())
                .expectError(RuntimeException.class)
                .verify();
    }

}
