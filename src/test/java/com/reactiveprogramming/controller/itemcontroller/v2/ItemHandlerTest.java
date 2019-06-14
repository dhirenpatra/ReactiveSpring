package com.reactiveprogramming.controller.itemcontroller.v2;

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
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static com.reactiveprogramming.controller.constants.ItemServiceConstants.ITEM_CONTROLLER_FUNCTIONAL_END_POINT_V1;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureWebTestClient
@DirtiesContext
@ActiveProfiles("test")
public class ItemHandlerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ItemReactiveRepository itemReactiveRepository;

    private List<ItemModel> createDataForTest() {
        return Arrays.asList(
                new ItemModel(null, "Iphone Xr", new BigDecimal(550.0), "Apple Phone"),
                new ItemModel(null, "OnePlus 7pro", new BigDecimal(450.0), "OnePlus Phone"),
                new ItemModel(null, "Samsung s10", new BigDecimal(390.0), "Samsung Phone"),
                new ItemModel("someRandomID", "Mac Book PRO", new BigDecimal(100090.0), "MacBook Pro")
        );
    }

    @Before
    public void initialSetup() {
        itemReactiveRepository.deleteAll()
                .thenMany(Flux.fromIterable(createDataForTest()))
                .flatMap(itemReactiveRepository :: save)
                .doOnNext( item -> {
                    System.err.println("Item Inserted for test is :" +item);
                })
                .blockLast();
    }

    @Test
    public void getAllItemsTest() {

        webTestClient.get()
                .uri(ITEM_CONTROLLER_FUNCTIONAL_END_POINT_V1)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ItemModel.class)
                .hasSize(4);
    }

    @Test
    public void getAllItemsWithDataTest() {

        webTestClient.get()
                .uri(ITEM_CONTROLLER_FUNCTIONAL_END_POINT_V1)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ItemModel.class)
                .consumeWith(items -> {
                    List<ItemModel> itemResponseBody = items.getResponseBody();
                    itemResponseBody.forEach( item -> {
                        assertThat(item.getItemId() != null);
                    });
                });
    }

    @Test
    public void getAllItemsWithDataCountTest() {

        Flux<ItemModel> itemModelFlux = webTestClient.get()
                .uri(ITEM_CONTROLLER_FUNCTIONAL_END_POINT_V1)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .returnResult(ItemModel.class)
                .getResponseBody();

        StepVerifier.create(itemModelFlux.log())
                .expectSubscription()
                .expectNextCount(4)
                .verifyComplete();
    }

    @Test
    public void getOneItemTest() {
        ItemModel itemModel = webTestClient.get()
                .uri(ITEM_CONTROLLER_FUNCTIONAL_END_POINT_V1.concat("/{id}"), "someRandomID")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ItemModel.class)
                .returnResult()
                .getResponseBody();

        assertThat(itemModel.getItemName().equals("Mac Book PRO"));

    }

    @Test
    public void getOneItemTest_withBody() {
        webTestClient.get()
                .uri(ITEM_CONTROLLER_FUNCTIONAL_END_POINT_V1.concat("/{id}"), "someRandomID")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.price", new BigDecimal(100090.0));

    }

    @Test
    public void getOneItemNotFoundTest() {
        webTestClient.get()
                .uri(ITEM_CONTROLLER_FUNCTIONAL_END_POINT_V1.concat("/{id}"), "someRandomIDWhichIsNotPresent")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isNotFound();

    }

}
