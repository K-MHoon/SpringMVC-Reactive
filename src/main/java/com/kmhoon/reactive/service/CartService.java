package com.kmhoon.reactive.service;

import com.kmhoon.reactive.domain.Cart;
import com.kmhoon.reactive.domain.CartItem;
import com.kmhoon.reactive.domain.Item;
import com.kmhoon.reactive.repository.CartRepository;
import com.kmhoon.reactive.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CartService {

    private final ItemRepository itemRepository;
    private final CartRepository cartRepository;
//    private final ItemByExampleRepository exampleRepository;

    public Mono<Cart> addItemToCart(String cartId, String id) {
        return this.cartRepository.findById(cartId)
                .log("foundCart")
                .defaultIfEmpty(new Cart(cartId))
                .log("emptyCart")
                .flatMap(cart -> cart.getCartItems().stream()
                        .filter(cartItem -> cartItem.getItem()
                                .getId().equals(id))
                        .findAny()
                        .map(cartItem -> {
                            cartItem.increment();
                            return Mono.just(cart).log("newCartItem");
                        })
                        .orElseGet(() ->
                                this.itemRepository.findById(id)
                                        .log("fetchedItem")
                                        .map(CartItem::new)
                                        .log("cartItem")
                                        .doOnNext(cartItem ->
                                                cart.getCartItems().add(cartItem))
                                        .map(cartItem -> cart)))
                .log("cartWithAnotherItem")
                .flatMap(this.cartRepository::save)
                .log("savedCart");
    }

    public Flux<Item> searchByExample(String name, String description, boolean useAnd) {
        Item item = new Item(name, description, 0.0);

        ExampleMatcher matcher = (useAnd
                ? ExampleMatcher.matchingAll()
                : ExampleMatcher.matchingAny())
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase()
                .withIgnorePaths("price");

        Example<Item> probe = Example.of(item, matcher);

        return itemRepository.findAll(probe);
    }
}
