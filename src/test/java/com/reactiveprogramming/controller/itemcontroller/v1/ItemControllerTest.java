package com.reactiveprogramming.controller.itemcontroller.v1;

import com.reactiveprogramming.iteminventory.db.ItemModel;
import com.reactiveprogramming.iteminventory.repository.ItemReactiveRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static com.reactiveprogramming.controller.constants.ItemServiceConstants.ITEM_CONTROLLER_END_POINT_V1;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
@DirtiesContext
@AutoConfigureWebTestClient
@ActiveProfiles("test")
public class ItemControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ItemReactiveRepository itemReactiveRepository;

    @Before
    public void run() {
        initialDataInsertion();
    }

    private void initialDataInsertion() {
        itemReactiveRepository.deleteAll()
                .thenMany(Flux.fromIterable(createDataForTest()))
                .flatMap(itemReactiveRepository :: save)
                .doOnNext(System.err::println)
                .blockLast();
    }

    private List<ItemModel> createDataForTest() {
        return Arrays.asList(
                new ItemModel(null, "Iphone Xr", new BigDecimal(550.0), "Apple Phone"),
                new ItemModel(null, "OnePlus 7pro", new BigDecimal(450.0), "OnePlus Phone"),
                new ItemModel(null, "Samsung s10", new BigDecimal(390.0), "Samsung Phone"),
                new ItemModel("someRandomID", "Mac Book PRO", new BigDecimal(100090.0), "MacBook Pro")
        );
    }

    @Test
    public void getAllItems() {
        webTestClient.get()
                .uri(ITEM_CONTROLLER_END_POINT_V1)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(ItemModel.class)
                .hasSize(4);
    }

    @Test
    public void getAllItemsWithData() {
        webTestClient.get()
                .uri(ITEM_CONTROLLER_END_POINT_V1)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(ItemModel.class)
                .consumeWith(
                        response -> {
                            List<ItemModel> items = response.getResponseBody();
                            items.forEach(item -> assertThat(item.getItemId() != null));
                        }
                );
    }

    @Test
    public void getAllItemsWithDataCount() {
        Flux<ItemModel> itemModelFlux = webTestClient.get()
                .uri(ITEM_CONTROLLER_END_POINT_V1)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .returnResult(ItemModel.class)
                .getResponseBody();

        StepVerifier.create(itemModelFlux.log())
                .expectSubscription()
                .expectNextCount(4)
                .verifyComplete();

    }

    @Test
    public void findById_SuccessScenario() {
        webTestClient.get()
                    .uri(ITEM_CONTROLLER_END_POINT_V1+"/{id}", "someRandomID")
                    .accept(MediaType.APPLICATION_JSON_UTF8)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .jsonPath("$.price", new BigDecimal(100090.0));
    }

    @Test
    public void findById_FailureScenario() {
        webTestClient.get()
                .uri(ITEM_CONTROLLER_END_POINT_V1+"/{id}", "someWrongRandomID")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void createItemTest() {
        ItemModel itemModel = new ItemModel(null, "iPhone X",
                new BigDecimal(999.99), "Latest Iphone");

        webTestClient.post()
                    .uri(ITEM_CONTROLLER_END_POINT_V1)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .body(Mono.just(itemModel), ItemModel.class)
                    .exchange()
                    .expectStatus().isCreated()
                    .expectBody()
                    .jsonPath("$.itemId").isNotEmpty()
                    .jsonPath("$.price").isEqualTo(999.99);

    }

    @Test
    public void deleteItemTest() {
        webTestClient.delete()
                    .uri(ITEM_CONTROLLER_END_POINT_V1+"/{id}", "someRandomID")
                    .accept(MediaType.APPLICATION_JSON_UTF8)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(Void.class);
    }

}
