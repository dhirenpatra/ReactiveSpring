package com.reactiveprogramming.reactivestream;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

public class FluxAndMonoTimeTest {

    @Test
    public void infiniteSequence() throws InterruptedException {
        Flux<Long> infiniteFlux = Flux.interval(Duration.ofMillis(100));

        infiniteFlux.log().subscribe(element -> System.out.println("Value is : "+element));

        Thread.sleep(3000);

    }

    @Test
    public void infiniteSequenceTest() {
        Flux<Long> finiteFlux = Flux.interval(Duration.ofMillis(100)).take(3);

        StepVerifier.create(finiteFlux.log())
                .expectSubscription()
                .expectNext(0l,1l,2l)
                .verifyComplete();
    }

    @Test
    public void infiniteSequenceTest_withMap() {
        Flux<Integer> finiteFlux = Flux.interval(Duration.ofMillis(100))
                                    .map(e -> new Integer(e.intValue()))
                                    .take(3);

        StepVerifier.create(finiteFlux.log())
                .expectSubscription()
                .expectNext(0,1,2)
                .verifyComplete();
    }

    @Test
    public void infiniteSequenceTest_withMap_andDelay() {
        Flux<Integer> finiteFlux = Flux.interval(Duration.ofMillis(100))
                .delayElements(Duration.ofSeconds(1))
                .map(e -> new Integer(e.intValue()))
                .take(3);

        StepVerifier.create(finiteFlux.log())
                .expectSubscription()
                .expectNext(0,1,2)
                .verifyComplete();
    }

    @Test
    public void infiniteSequenceTest_withMap_andDelaySub() {
        Flux<Integer> finiteFlux = Flux.interval(Duration.ofMillis(100))
                .delaySubscription(Duration.ofSeconds(1))
                .map(e -> new Integer(e.intValue()))
                .take(3);

        StepVerifier.create(finiteFlux.log())
                .expectSubscription()
                .expectNext(0,1,2)
                .verifyComplete();
    }

}
