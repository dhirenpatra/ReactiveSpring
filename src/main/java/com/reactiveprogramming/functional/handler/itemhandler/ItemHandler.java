package com.reactiveprogramming.functional.handler.itemhandler;

import com.reactiveprogramming.iteminventory.db.ItemModel;
import com.reactiveprogramming.iteminventory.repository.ItemReactiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Optional;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;

@Component
public class ItemHandler {

    private static Mono<ServerResponse> notFoundResponse = ServerResponse.notFound().build();

    @Autowired
    private ItemReactiveRepository itemReactiveRepository;

    public Mono<ServerResponse> getAllItems(ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(
                        itemReactiveRepository.findAll(), ItemModel.class
                );
    }

    public Mono<ServerResponse> getItemByIdV1(ServerRequest serverRequest) {

        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(
                        itemReactiveRepository.findById(serverRequest.pathVariable("id"))
                                .defaultIfEmpty(new ItemModel(
                                        "someRandomID", "Mac Book PRO", new BigDecimal(100090.0), "MacBook Pro")
                                ), ItemModel.class);
    }

    public Mono<ServerResponse> getItemById(ServerRequest serverRequest) {

        Mono<ItemModel> itemReactiveRepositoryById = itemReactiveRepository.findById(serverRequest.pathVariable("id"));
        return itemReactiveRepositoryById.flatMap(
                item -> ServerResponse.ok()
                        .body(fromObject(item)))
                .switchIfEmpty(notFoundResponse);
    }
}
