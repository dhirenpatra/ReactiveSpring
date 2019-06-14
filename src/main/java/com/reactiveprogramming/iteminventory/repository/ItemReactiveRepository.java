package com.reactiveprogramming.iteminventory.repository;

import com.reactiveprogramming.iteminventory.db.ItemModel;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ItemReactiveRepository extends ReactiveMongoRepository<ItemModel,String> {
    public Mono<ItemModel> findByDescription(String desc);
}
