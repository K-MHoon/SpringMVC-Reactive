package com.kmhoon.reactive.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

@Data
public class Cart {

    private @Id String id;
    private List<CartItem> cartItem;

    private Cart() {}

    public Cart(String id) {
        this(id, new ArrayList<>());
    }

    public Cart(String id, List<CartItem> cartItem) {
        this.id = id;
        this.cartItem = cartItem;
    }
}