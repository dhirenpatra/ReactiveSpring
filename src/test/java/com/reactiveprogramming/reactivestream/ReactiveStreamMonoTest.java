package com.reactiveprogramming.reactivestream;

import org.junit.Test;
import reactor.core.publisher.Mono;

public class ReactiveStreamMonoTest {

    @Test
    public void fluxTestWithoutError() {

        Mono<String> fluxString = Mono.just("Spring");

        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        fluxString.subscribe(System.out::println);
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

    }

    @Test
    public void fluxTestWithError() {

        Mono<String> fluxString = Mono.error(new RuntimeException("Exception Thrown"));

        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        fluxString.subscribe(System.out::println, (error) -> System.err.println("Error Occurred :" + error));
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

    }

}
