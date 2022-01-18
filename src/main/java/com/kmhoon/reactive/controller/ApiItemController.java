package com.kmhoon.reactive.controller;

import com.kmhoon.reactive.domain.Item;
import com.kmhoon.reactive.repository.ItemRepository;
import com.kmhoon.reactive.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class ApiItemController {

    private final InventoryService service;

    @GetMapping("/api/items")
    Flux<Item> findAll() {
        return this.service.getInventory();
    }

    @GetMapping("/api/items/{id}")
    Mono<Item> findOne(@PathVariable String id) {
        return this.service.getItemFindById(id);
    }

    @PostMapping("/api/items")
    Mono<ResponseEntity<?>> addNewItem(@RequestBody Mono<Item> item) {
        return item.flatMap(s -> this.service.getItemSave(s))
                .map(savedItem -> ResponseEntity
                        .created(URI.create("/api/items/" + savedItem.getId()))
                        .body(savedItem));
    }

    @PutMapping("/api/items/{id}")
    public Mono<ResponseEntity<?>> updateItem(@RequestBody Mono<Item> item,
                                              @PathVariable String id) {
        return item
                .map(content -> new Item(id, content.getName(), content.getDescription(), content.getPrice()))
                .flatMap(this.service::getItemSave)
                .map(ResponseEntity::ok);
    }

}
