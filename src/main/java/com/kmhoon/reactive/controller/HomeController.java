package com.kmhoon.reactive.controller;

import com.kmhoon.reactive.domain.Cart;
import com.kmhoon.reactive.domain.CartItem;
import com.kmhoon.reactive.repository.CartRepository;
import com.kmhoon.reactive.repository.ItemRepository;
import com.kmhoon.reactive.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ItemRepository itemRepository;
    private final CartRepository cartRepository;
    private final CartService cartService;

    @GetMapping
    Mono<Rendering> home() {
        return Mono.just(Rendering.view("home.html")
                .modelAttribute("items",
                        this.itemRepository.findAll().doOnNext(System.out::println))
                .modelAttribute("cart",
                        this.cartRepository.findById("My Cart")
                                .defaultIfEmpty(new Cart("My Cart")))
                .build());
    }

    @PostMapping("/add/{id}")
    Mono<String> addToCart(@PathVariable String id) {
        return this.cartService.addItemToCart("My Cart", id)
                .thenReturn("redirect:/");
    }

    @GetMapping("/search")
    Mono<Rendering> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam boolean useAnd) {
        return Mono.just(Rendering.view("home.html")
                .modelAttribute("results", this.cartService.searchByExample(name, description, useAnd))
                .build());
    }
}
