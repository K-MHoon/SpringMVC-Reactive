package com.kmhoon.reactive.controller;

import com.kmhoon.reactive.domain.Item;
import com.kmhoon.reactive.repository.ItemRepository;
import com.kmhoon.reactive.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.*;
import org.springframework.hateoas.mediatype.alps.Alps;
import org.springframework.hateoas.mediatype.alps.Type;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.swing.text.html.parser.Entity;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mediatype.alps.Alps.alps;
import static org.springframework.hateoas.mediatype.alps.Alps.descriptor;
import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.linkTo;
import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
public class AffordancesItemController {

    private final InventoryService service;

    @PutMapping("/affordances/items/{id}")
    public Mono<ResponseEntity<?>> updateItem(@RequestBody Mono<EntityModel<Item>> item,
                                              @PathVariable String id) {
        return item
                .map(EntityModel::getContent)
                .map(content -> new Item(id, content.getName(),
                        content.getDescription(), content.getPrice()))
                .flatMap(this.service::getItemSave)
                .then(findOne(id))
                .map(model -> ResponseEntity.noContent()
                        .location(model.getRequiredLink(IanaLinkRelations.SELF).toUri()).build());
    }

    @GetMapping("/affordances/items/{id}")
    Mono<EntityModel<Item>> findOne(@PathVariable String id) {
        AffordancesItemController controller = methodOn(AffordancesItemController.class);

        Mono<Link> selfLink = linkTo(controller.findOne(id))
                .withSelfRel()
                .andAffordance(controller.updateItem(null, id))
                .toMono();

        Mono<Link> aggregateLink = linkTo(controller.findAll())
                .withRel(IanaLinkRelations.ITEM)
                .toMono();

        return Mono.zip(service.getItemFindById(id), selfLink, aggregateLink)
                .map(o -> EntityModel.of(o.getT1(), Links.of(o.getT2(), o.getT3())));
    }

    @GetMapping("/affordances/items")
    Mono<CollectionModel<EntityModel<Item>>> findAll() {
        AffordancesItemController controller = methodOn(AffordancesItemController.class);

        Mono<Link> aggregateRoot = linkTo(controller.findAll())
                .withSelfRel()
                .andAffordance(controller.addNewItem(null))
                .toMono();

        return this.service.getInventory()
                .flatMap(item -> findOne(item.getId()))
                .collectList()
                .flatMap(models -> aggregateRoot
                        .map(selfLink -> CollectionModel.of(
                                models, selfLink)));
    }

    @PostMapping("/affordances/items")
    Mono<ResponseEntity<?>> addNewItem(@RequestBody Mono<EntityModel<Item>> item) {
        return item
                .map(EntityModel::getContent)
                .flatMap(this.service::getItemSave)
                .map(Item::getId)
                .flatMap(this::findOne)
                .map(newModel -> ResponseEntity.created(newModel
                        .getRequiredLink(IanaLinkRelations.SELF)
                        .toUri()).body(newModel.getContent()));
    }

    @GetMapping(value = "/affordances/items/profile", produces = MediaTypes.ALPS_JSON_VALUE)
    public Alps profile() {
        return alps() //
                .descriptor(Collections.singletonList(descriptor() //
                        .id(Item.class.getSimpleName() + "-representation") //
                        .descriptor( //
                                Arrays.stream(Item.class.getDeclaredFields()) //
                                        .map(field -> descriptor() //
                                                .name(field.getName()) //
                                                .type(Type.SEMANTIC) //
                                                .build()) //
                                        .collect(Collectors.toList())) //
                        .build())) //
                .build();
    }
}
