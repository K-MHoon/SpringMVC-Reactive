package com.kmhoon.reactive.domain;

import lombok.Data;

@Data
public class Dish {

    private String description;
    private boolean delivered = false;

    public static Dish deliver(Dish dish) {
        Dish deliveredDish = new Dish(dish.description);
        deliveredDish.delivered = true;
        return deliveredDish;
    }

    public Dish(String descriptions) {
        this.description = descriptions;
    }

    public boolean isDelivered() {
        return delivered;
    }
}
