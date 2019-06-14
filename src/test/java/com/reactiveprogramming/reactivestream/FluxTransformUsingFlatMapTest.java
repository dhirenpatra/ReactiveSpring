package com.reactiveprogramming.reactivestream;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static reactor.core.scheduler.Schedulers.parallel;

public class FluxTransformUsingFlatMapTest {

    private List<String> namesList = Arrays.asList("Dhiren", "Kumar", "Disha");

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

    @Test
    public void transformUsingFlatmap() {

        Flux<String> namesFlux = Flux.fromIterable(Arrays.asList("A","B","C","D","E","F"))
                                    .flatMap(item -> {
                                        return Flux.fromIterable(saveToDB(item));
                                    });

        StepVerifier.create(namesFlux.log())
                    .expectNextCount(12)
                    .verifyComplete();

    }

    private List<String> saveToDB(String s) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Arrays.asList(s, "added");
    }

    @Test
    public void transformUsingFlatmap_Parallel() {

        Flux<String> namesFlux = Flux.fromIterable(Arrays.asList("A","B","C","D","E","F"))
                                     .window(2)
                                    .flatMap(item -> item.map(this::saveToDB).subscribeOn(parallel()))
                                    .flatMap(item -> Flux.fromIterable(item));

        StepVerifier.create(namesFlux.log())
                .expectNextCount(12)
                .verifyComplete();

    }

    @Test
    public void transformUsingFlatmap_Parallel_InOrder() {
        Flux<String> stringFlux = Flux.fromIterable(Arrays.asList("A", "B", "C", "D", "E", "F"))
                .window(2)
                .flatMapSequential(s -> s.map(this::saveToDB).subscribeOn(parallel()))
                .flatMap(each -> Flux.fromIterable(each));

        StepVerifier.create(stringFlux.log())
                .expectSubscription()
                .expectNextCount(12)
                .verifyComplete();
    }

    @Test
    public void transformUsingFlatmap_Parallel_InOrder_Slow() {
        Flux<String> stringFlux = Flux.fromIterable(Arrays.asList("A", "B", "C", "D", "E", "F"))
                .window(2)
                .concatMap(s -> s.map(this::saveToDB).subscribeOn(parallel()))
                .flatMap(each -> Flux.fromIterable(each));

        StepVerifier.create(stringFlux.log())
                .expectNextCount(12)
                .verifyComplete();
    }

}
