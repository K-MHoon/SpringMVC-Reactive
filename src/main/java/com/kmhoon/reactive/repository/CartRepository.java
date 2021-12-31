package com.kmhoon.reactive.repository;

import com.kmhoon.reactive.domain.Cart;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface CartRepository extends ReactiveCrudRepository<Cart, String> {
}
