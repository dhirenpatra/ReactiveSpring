package com.reactiveprogramming.runner;

import com.reactiveprogramming.iteminventory.db.ItemModel;
import com.reactiveprogramming.iteminventory.repository.ItemReactiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Component
@Profile("!test")
public class ItemInitializer implements CommandLineRunner {

    @Autowired
    private ItemReactiveRepository itemReactiveRepository;

    @Override
    public void run(String... args) throws Exception {
        initialDataInsertion();
    }

    private void initialDataInsertion() {
        itemReactiveRepository.deleteAll()
                .thenMany(Flux.fromIterable(createDataForTest()))
                .flatMap(itemReactiveRepository :: save)
                .thenMany(itemReactiveRepository.findAll())
                .subscribe(System.err::println);
    }

    private List<ItemModel> createDataForTest() {
        return Arrays.asList(
                new ItemModel(null, "Iphone Xr", new BigDecimal(550.0), "Apple Phone"),
                new ItemModel(null, "OnePlus 7pro", new BigDecimal(450.0), "OnePlus Phone"),
                new ItemModel(null, "Samsung s10", new BigDecimal(390.0), "Samsung Phone"),
                new ItemModel("someRandomID", "Mac Book PRO", new BigDecimal(100090.0), "MacBook Pro")
        );
    }

}
