package com.kmhoon.reactive.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CartItem {

    private Item item;
    private int quantity;

    public CartItem(Item item) {
        this.item = item;
        this.quantity = 1;
    }
}
