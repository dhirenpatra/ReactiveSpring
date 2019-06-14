package com.reactiveprogramming.reactivestream;

import org.junit.Test;
import reactor.core.publisher.Flux;

public class ReactiveStreamFluxTest {

    @Test
    public void fluxTestWithoutError() {

        Flux<String> fluxString = Flux.just("Spring", "Spring Boot", "Reactive Stream");

        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        fluxString.subscribe(System.out::println);
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

    }

    @Test
    public void fluxTestWithError() {

        Flux<String> fluxString = Flux.just("Spring", "Spring Boot", "Reactive Stream")
                .concatWith(Flux.error(new RuntimeException("Exception Thrown")));

        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        fluxString.subscribe(System.out::println, (error) -> System.err.println("Error Occurred :" + error));
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

    }

    @Test
    public void fluxTestAfterComplete() {

        Flux<String> fluxString = Flux.just("Spring", "Spring Boot", "Reactive Stream");

        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        fluxString.subscribe(System.out::println,
                (error) -> System.err.println("Error Occurred :" + error),
                () -> System.out.println("Completed Successfully"));
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

    }

    @Test
    public void fluxTestAfterCompleteWithLog() {

        Flux<String> fluxString = Flux.just("Spring", "Spring Boot", "Reactive Stream").log();

        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        fluxString.subscribe(System.out::println,
                (error) -> System.err.println("Error Occurred :" + error),
                () -> System.out.println("Completed Successfully"));
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

    }

}
