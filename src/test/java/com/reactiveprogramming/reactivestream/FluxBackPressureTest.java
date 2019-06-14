package com.reactiveprogramming.reactivestream;

import org.junit.Test;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxBackPressureTest {

    @Test
    public void backPressureTest_usingStepVerifier() {

        Flux<Integer> integerFlux = Flux.range(1, 10);

        StepVerifier.create(integerFlux.log())
                    .expectSubscription()
                    .thenRequest(1)
                    .expectNext(1)
                    .thenRequest(1)
                    .expectNext(2)
                    .thenRequest(2)
                    .expectNext(3,4)
                    .thenCancel()
                    .verify();

    }

    @Test
    public void backPressureSubscriberRequest() {

        Flux<Integer> integerFlux = Flux.range(1, 10);

        integerFlux.log().subscribe(element -> System.out.println(element),
                error -> System.err.println(error),
                () -> System.out.println("COMPLETED"),
                (subscription -> subscription.request(2)));
    }

    @Test
    public void backPressureSubscriberRequest_MethodRef() {

        Flux<Integer> integerFlux = Flux.range(1, 10);

        integerFlux.log().subscribe(System.out::println,
                System.err::println,
                () -> System.out.println("COMPLETED"),
                subscription -> subscription.request(2));
    }

    @Test
    public void backPressureSubscriberCancel() {

        Flux<Integer> integerFlux = Flux.range(1, 10);

        integerFlux.log().subscribe(System.out::println,
                System.err::println,
                () -> System.out.println("COMPLETED"),
                subscription -> {
                    subscription.request(5);
                    subscription.cancel();
                });
    }

    @Test
    public void backPressureCustomSubscriberCancel() {

        Flux<Integer> integerFlux = Flux.range(1, 10);

        integerFlux.log().subscribe(new BaseSubscriber<Integer>() {
            @Override
            protected void hookOnNext(Integer value) {
                request(1);
                System.out.println("Received Value is : "+value);
                if(value == 7)
                    cancel();
            }
        });
    }

}
