package com.reactiveprogramming.controller.itemcontroller.v1;

import com.reactiveprogramming.iteminventory.db.ItemModel;
import com.reactiveprogramming.iteminventory.repository.ItemReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.reactiveprogramming.controller.constants.ItemServiceConstants.ITEM_CONTROLLER_END_POINT_V1;

@RestController
@Slf4j
public class ItemController {

    @Autowired
    private ItemReactiveRepository itemReactiveRepository;

    @GetMapping(ITEM_CONTROLLER_END_POINT_V1)
    public Flux<ItemModel> getAllItems() {
        return itemReactiveRepository.findAll();
    }

    @GetMapping(ITEM_CONTROLLER_END_POINT_V1+"/{id}")
    public Mono<ResponseEntity<ItemModel>> getIndividualItem(@PathVariable String id) {
        return itemReactiveRepository.findById(id)
                .map(item -> new ResponseEntity<>(item, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(ITEM_CONTROLLER_END_POINT_V1)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ItemModel> createItem(@RequestBody ItemModel item) {
        return itemReactiveRepository.save(item);
    }

    @DeleteMapping(ITEM_CONTROLLER_END_POINT_V1+"/{id}")
    public Mono<Void> deleteItem(@PathVariable String id) {
        return itemReactiveRepository.deleteById(id);
    }

    @PutMapping(ITEM_CONTROLLER_END_POINT_V1+"/{id}")
    public Mono<ResponseEntity<ItemModel>> updateItem( @PathVariable String id,
                                                      @RequestBody ItemModel itemModel) {

        return itemReactiveRepository.findById(id)
                .flatMap(newItem -> {
                    if(itemModel.getDescription() != null)
                        newItem.setDescription(itemModel.getDescription());
                    if(itemModel.getItemId() != null)
                        newItem.setItemId(itemModel.getItemId());
                    if(itemModel.getItemName() != null)
                        newItem.setItemName(itemModel.getItemName());
                    if(itemModel.getPrice() != null)
                        newItem.setPrice(itemModel.getPrice());
                    return itemReactiveRepository.save(newItem);
                })
                .map(item -> new ResponseEntity<>(item, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.FORBIDDEN));

    }

}
