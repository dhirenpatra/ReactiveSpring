package com.reactiveprogramming.iteminventory.repository;

import com.reactiveprogramming.iteminventory.db.ItemModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@DataMongoTest
@RunWith(SpringRunner.class)
@DirtiesContext
public class ItemReactiveRepositoryTest {

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
                .doOnNext((item -> {
                    System.out.println("Inserted Item is :" + item);
                }))
                .blockLast();
    }

    @Test
    public void findAllTest() {
        Flux<ItemModel> itemModelFlux = itemReactiveRepository.findAll().log();

        StepVerifier.create(itemModelFlux)
                .expectSubscription()
                .expectNextCount(4)
                .verifyComplete();
    }

    @Test
    public void findByIdTest() {
        Mono<ItemModel> itemReactiveRepositoryById = itemReactiveRepository.findById("someRandomID");

        StepVerifier.create(itemReactiveRepositoryById.log())
                .expectSubscription()
                .assertNext(item -> "MacBook Pro".equals(item.getItemId()))
                .verifyComplete();
    }

    @Test
    public void findByIdTestExpectNext() {
        Mono<ItemModel> itemReactiveRepositoryById = itemReactiveRepository.findById("someRandomID");

        StepVerifier.create(itemReactiveRepositoryById.log())
                .expectSubscription()
                .expectNextMatches(item -> "someRandomID".equals(item.getItemId()))
                .verifyComplete();
    }

    //findByDescription

    @Test
    public void findByDescriptionTest() {

        String expectedItemId = "someRandomID";

        Mono<ItemModel> byDescription = itemReactiveRepository.findByDescription("MacBook Pro");

        StepVerifier.create(byDescription.log())
                .expectSubscription()
                .expectNextMatches(item -> expectedItemId.equals(item.getItemId()))
                .verifyComplete();
    }

    @Test
    public void saveItemTest() {
        ItemModel model = new ItemModel("123xcverew123@23#dwqewqe", "BOSE Headphone" ,
                new BigDecimal(233.43), "Best Quality Headphone");

        Mono<ItemModel> itemModelMono = itemReactiveRepository.save(model);

        StepVerifier.create(itemModelMono.log())
                .expectSubscription()
                .expectNextMatches(item -> "123xcverew123@23#dwqewqe".equals(item.getItemId()) && "BOSE Headphone".equals(item.getItemName()))
                .verifyComplete();
    }

    @Test
    public void updateItemTest() {

        String newDescription = "Samsung Phone with latest features";

        Mono<ItemModel> samsung_phone = itemReactiveRepository.findByDescription("Samsung Phone")
                .map(item -> {
                    item.setDescription(newDescription);
                    return item;
                })
                .flatMap(itemReactiveRepository::save);

        StepVerifier.create(samsung_phone.log())
                .expectSubscription()
                .expectNextMatches(item -> newDescription.equals(item.getDescription()))
                .verifyComplete();

    }

    @Test
    public void deleteByIdTest() {

        Mono<Void> macBook_pro = itemReactiveRepository.findByDescription("MacBook Pro")
                .map(ItemModel::getItemId)
                .flatMap(itemId -> itemReactiveRepository.deleteById(itemId));

        StepVerifier.create(macBook_pro.log())
                .expectSubscription()
                .verifyComplete();

        StepVerifier.create(itemReactiveRepository.findAll())
                .expectSubscription()
                .expectNextCount(3)
                .verifyComplete();

    }

    @Test
    public void deleteItemTest() {
        Mono<Void> macBook_pro = itemReactiveRepository.findByDescription("MacBook Pro")
                .flatMap(item -> itemReactiveRepository.delete(item));

        StepVerifier.create(macBook_pro.log())
                .expectSubscription()
                .verifyComplete();

        StepVerifier.create(itemReactiveRepository.findAll())
                .expectSubscription()
                .expectNextCount(3)
                .verifyComplete();
    }

}
