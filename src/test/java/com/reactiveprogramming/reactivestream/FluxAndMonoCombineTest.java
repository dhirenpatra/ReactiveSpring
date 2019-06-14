package com.reactiveprogramming.reactivestream;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.scheduler.VirtualTimeScheduler;

import java.time.Duration;

public class FluxAndMonoCombineTest {

    @Test
    public void combineFluxUsingMerge() {
        Flux<String> fluxString = Flux.just("A","B","C");
        Flux<String> fluxString2 = Flux.just("AA","BB","CC");

        Flux<String> mergerFlux = Flux.merge(fluxString, fluxString2);

        StepVerifier.create(mergerFlux.log())
                    .expectSubscription()
                    .expectNext("A","B","C","AA","BB","CC")
                    .verifyComplete();
    }

    @Test
    public void combineFluxUsingMerge_withDelay() {
        Flux<String> fluxString = Flux.just("A","B","C").delayElements(Duration.ofSeconds(1));
        Flux<String> fluxString1 = Flux.just("AAA","BBB","CCC").delayElements(Duration.ofSeconds(1));
        Flux<String> fluxString2 = Flux.just("AA","BB","CC").delayElements(Duration.ofSeconds(1));

        Flux<String> mergerFlux = Flux.merge(fluxString, fluxString1, fluxString2);

        StepVerifier.create(mergerFlux.log())
                .expectSubscription()
                .expectNextCount(9)
                //.expectNext("A","AAA","AA","B","BBB","BB","C","CCC","CC")
                .verifyComplete();
    }

    @Test
    public void combineFluxUsingMerge_withDelay_withVirtualTime() {

        VirtualTimeScheduler.getOrSet();

        Flux<String> fluxString = Flux.just("A","B","C").delayElements(Duration.ofSeconds(1));
        Flux<String> fluxString1 = Flux.just("AAA","BBB","CCC").delayElements(Duration.ofSeconds(1));
        Flux<String> fluxString2 = Flux.just("AA","BB","CC").delayElements(Duration.ofSeconds(1));

        Flux<String> mergerFlux = Flux.merge(fluxString, fluxString1, fluxString2);

        StepVerifier.withVirtualTime(() -> mergerFlux.log())
                .expectSubscription()
                .thenAwait(Duration.ofSeconds(9))
                .expectNextCount(9)
                .verifyComplete();
    }

    @Test
    public void combineFluxUsingConcat() {
        Flux<String> fluxString = Flux.just("A", "B", "C");
        Flux<String> fluxString2 = Flux.just("AA", "BB", "CC");

        Flux<String> mergerFlux = Flux.concat(fluxString, fluxString2);

        StepVerifier.create(mergerFlux.log())
                .expectSubscription()
                .expectNext("A", "B", "C", "AA", "BB", "CC")
                .verifyComplete();
    }

    @Test
    public void combineFluxUsingConcat_withDelay() {
        Flux<String> fluxString = Flux.just("A","B","C").delayElements(Duration.ofSeconds(1));
        Flux<String> fluxString1 = Flux.just("AAA","BBB","CCC").delayElements(Duration.ofSeconds(1));
        Flux<String> fluxString2 = Flux.just("AA","BB","CC").delayElements(Duration.ofSeconds(1));

        Flux<String> mergerFlux = Flux.concat(fluxString, fluxString2, fluxString1);

        StepVerifier.create(mergerFlux.log())
                .expectSubscription()
                .expectNext("A", "B", "C", "AA", "BB", "CC","AAA","BBB","CCC")
                .verifyComplete();
    }

    @Test
    public void combineFluxUsingZip() {
        Flux<String> fluxString = Flux.just("A", "B", "C");
        Flux<String> fluxString2 = Flux.just("AA", "BB", "CC");

        Flux<String> mergerFlux = Flux.zip(fluxString, fluxString2, (t1,t2) -> t1.concat(t2));

        StepVerifier.create(mergerFlux.log())
                .expectSubscription()
                .expectNext("AAA", "BBB", "CCC")
                .verifyComplete();
    }

}
