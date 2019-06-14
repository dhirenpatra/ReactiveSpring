package com.reactiveprogramming.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@WebFluxTest
public class MonoDemoControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void getIntegerIdTest() {

        Integer expectedValue = new Integer(1);

        webTestClient.get().uri("/mono")
                    .accept(MediaType.APPLICATION_JSON_UTF8)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(Integer.class)
                    .consumeWith(
                            response -> assertEquals(expectedValue, response.getResponseBody())
                    );
    }

}
