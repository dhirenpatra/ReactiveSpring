package com.reactiveprogramming.functional.handler;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class HandlerFunctionDemo {

    public Mono<ServerResponse> getIntegerIdsFlux(ServerRequest serverRequest) {

        System.err.println(serverRequest);

        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(
                        Flux.just(1,2,3,4,5)
                                .log(), Integer.class
                );
    }

    public Mono<ServerResponse> getIntegerIdsMono(ServerRequest serverRequest) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(
                        Mono.justOrEmpty(1).log(), Integer.class
                );
    }

}
