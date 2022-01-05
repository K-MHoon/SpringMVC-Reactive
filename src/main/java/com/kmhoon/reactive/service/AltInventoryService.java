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
public class AltInventoryService {

    private final ItemRepository itemRepository;
    private final CartRepository cartRepository;

    public AltInventoryService(ItemRepository itemRepository, CartRepository cartRepository) {
        this.itemRepository = itemRepository;
        this.cartRepository = cartRepository;
    }

    public Mono<Cart> addItemToCart(String cartId, String id) {
        Cart myCart = this.cartRepository.findById(cartId)
                .defaultIfEmpty(new Cart(cartId))
                .block();// 블로킹 코드 호출

        return myCart.getCartItems().stream()
                .filter(cartItem -> cartItem.getItem()
                        .getId().equals(id))
                .findAny()
                .map(cartItem -> {
                    cartItem.increment();
                    return Mono.just(myCart).log("newCartItem");
                })
                .orElseGet(() ->
                        this.itemRepository.findById(id)
                                .map(CartItem::new)
                                .map(cartItem -> {
                                    myCart.getCartItems().add(cartItem);
                                    return myCart;
                                }))
                .flatMap(this.cartRepository::save);
    }
}
