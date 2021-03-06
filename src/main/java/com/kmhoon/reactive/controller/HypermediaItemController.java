package com.kmhoon.reactive.controller;

import com.kmhoon.reactive.domain.Item;
import com.kmhoon.reactive.repository.ItemRepository;
import com.kmhoon.reactive.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.*;
import org.springframework.hateoas.mediatype.alps.Alps;
import org.springframework.hateoas.mediatype.alps.Type;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mediatype.alps.Alps.alps;
import static org.springframework.hateoas.mediatype.alps.Alps.descriptor;
import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.linkTo;
import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
public class HypermediaItemController {

    private final InventoryService service;

    @GetMapping("/hypermedia/items/{id}")
    Mono<EntityModel<Item>> findOne(@PathVariable String id) {
        HypermediaItemController controller = methodOn(HypermediaItemController.class);

        Mono<Link> selfLink = linkTo(controller.findOne(id)).withSelfRel().toMono();
        Mono<Link> aggregateLink = linkTo(controller.findAll()).withRel(IanaLinkRelations.ITEM).toMono();

        return Mono.zip(service.getItemFindById(id), selfLink, aggregateLink)
                .map(o -> EntityModel.of(o.getT1(), Links.of(o.getT2(), o.getT3())));
    }

    @GetMapping("/hypermedia/items")
    Mono<CollectionModel<EntityModel<Item>>> findAll() {

        return this.service.getInventory() //
                .flatMap(item -> findOne(item.getId())) //
                .collectList() //
                .flatMap(entityModels -> linkTo(methodOn(HypermediaItemController.class) //
                        .findAll()).withSelfRel() //
                        .toMono() //
                        .map(selfLink -> CollectionModel.of(entityModels, selfLink)));
    }

    @GetMapping(value = "/hypermedia/items/profile", produces = MediaTypes.ALPS_JSON_VALUE)
    public Alps profile() {
        return alps()
                .descriptor(Collections.singletonList(descriptor()
                        .id(Item.class.getSimpleName() + "-repr")
                        .descriptor(Arrays.stream(
                                Item.class.getDeclaredFields())
                                .map(field -> descriptor()
                                        .name(field.getName())
                                        .type(Type.SEMANTIC)
                                        .build())
                                .collect(Collectors.toList()))
                        .build()))
                .build();
    }
}
