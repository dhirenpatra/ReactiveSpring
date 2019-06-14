package com.reactiveprogramming.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@WebFluxTest
public class FluxDemoControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void getIntegerIdInDelayTest() {
        Flux<Integer> integerFlux = webTestClient.get()
                .uri("/flux/delay")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Integer.class)
                .getResponseBody();

        StepVerifier.create(integerFlux.log())
                .expectSubscription()
                .expectNext(1,2,3,4,5)
                .verifyComplete();
    }

    @Test
    public void getIntegerIdInDelayCountTest() {
        webTestClient.get()
                .uri("/flux/delay")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Integer.class)
                .hasSize(5);
    }

    @Test
    public void getIntegerIdInDelayResponseTest() {

        List<Integer> expectList = Arrays.asList(1,2,3,4,5);

        EntityExchangeResult<List<Integer>> listEntityExchangeResult = webTestClient.get()
                .uri("/flux/delay")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Integer.class)
                .returnResult();

        assertEquals(expectList, listEntityExchangeResult.getResponseBody());
    }

    //@Test
    public void getIntegerIdInDelayResponseConsumeTest() {

        List<Integer> expectedList = Arrays.asList(1,2,3,4,5);

        EntityExchangeResult<List<Integer>> listEntityExchangeResult = webTestClient.get()
                .uri("/flux/delay")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Integer.class)
                .consumeWith((response) -> {
                    assertEquals(expectedList, response.getResponseBody());;
                });

    }

    @Test
    public void getIntegerInfiniteStreamTest() {

        Flux<Long> responseBody = webTestClient.get()
                .uri("/flux/stream/infinite")
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Long.class)
                .getResponseBody();

        StepVerifier.create(responseBody.log())
                    .expectSubscription()
                    .expectNext(0l,1l,2l)
                    .thenCancel()
                    .verify();
    }

}
