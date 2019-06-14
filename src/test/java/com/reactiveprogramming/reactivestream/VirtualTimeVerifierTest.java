package com.reactiveprogramming.reactivestream;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.scheduler.VirtualTimeScheduler;

import java.time.Duration;

public class VirtualTimeVerifierTest {

    @Test
    public void verifyWithoutVirtualTimeVerifier() {

        Flux<Long> longFlux = Flux.interval(Duration.ofSeconds(1))
                                .take(4).log();

        StepVerifier.create(longFlux)
                    .expectSubscription()
                    .expectNext(0l,1l,2l,3l)
                    .verifyComplete();

    }

    @Test
    public void verifyWithVirtualTimeVerifier() {

        VirtualTimeScheduler.getOrSet();
        Flux<Long> longFlux = Flux.interval(Duration.ofSeconds(1))
                .take(4).log();

        StepVerifier.withVirtualTime(() -> longFlux)
                    .expectSubscription()
                    .thenAwait(Duration.ofSeconds(4))
                    .expectNext(0l,1l,2l,3l)
                    .verifyComplete();
    }

}
