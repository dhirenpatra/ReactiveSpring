package com.reactiveprogramming.functional.router;

import com.reactiveprogramming.functional.handler.HandlerFunctionDemo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class RouterFunctionConfig {

    @Bean
    public RouterFunction<ServerResponse> route(HandlerFunctionDemo handlerFunctionDemo) {
        return RouterFunctions.route(GET("/functional/flux").and(accept(MediaType.APPLICATION_JSON)), handlerFunctionDemo::getIntegerIdsFlux)
                                .andRoute(GET("/functional/mono").and(accept(MediaType.APPLICATION_JSON)), handlerFunctionDemo::getIntegerIdsMono);
    }

}
