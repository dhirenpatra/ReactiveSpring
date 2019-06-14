package com.reactiveprogramming.reactivestream;

import org.junit.Test;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

public class HotAndColdSubscriberTest {

    @Test
    public void coldSubscriberTest() throws InterruptedException {

        Flux<String> stringFlux = Flux.just("A", "B", "C", "D", "E", "F").log();
        Thread.sleep(1000);
        stringFlux.log().subscribe(item -> System.err.println("SUBSCRIBER ONE : "+item));
        Thread.sleep(4000);
        stringFlux.log().subscribe(item -> System.out.println("SUBSCRIBER TWO : "+item));
    }

    @Test
    public void coldSubscriberSimpleTest() {

        Flux<String> stringFlux = Flux.just("A", "B", "C", "D", "E", "F").log();
        stringFlux.subscribe(item -> System.err.println("SUBSCRIBER ONE : "+item));
        stringFlux.subscribe(item -> System.out.println("SUBSCRIBER TWO : "+item));
    }

    @Test
    public void hotSubscriberTest() throws InterruptedException {

        Flux<String> stringColdFlux = Flux.just("A", "B", "C", "D", "E", "F").log();
        final ConnectableFlux<String> stringFlux = stringColdFlux.publish();
        stringFlux.connect();
        Thread.sleep(3000);
        stringFlux.subscribe(item -> System.err.println("SUBSCRIBER ONE : "+item));
        Thread.sleep(4000);
        stringFlux.subscribe(item -> System.out.println("SUBSCRIBER TWO : "+item));
    }

    @Test
    public void hotSubscriberSimpleTest() throws InterruptedException {

        Flux<String> stringColdFlux = Flux.just("A", "B", "C", "D", "E", "F").log();
        final ConnectableFlux<String> stringFlux = stringColdFlux.publish();
        stringFlux.connect();
        Thread.sleep(1000);
        stringFlux.subscribe(item -> System.err.println("SUBSCRIBER ONE : "+item));
        Thread.sleep(500);
        stringFlux.subscribe(item -> System.out.println("SUBSCRIBER TWO : "+item));
    }

}
