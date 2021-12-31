package com.kmhoon.reactive.repository;

import com.kmhoon.reactive.domain.Item;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ItemRepository extends ReactiveCrudRepository<Item, String> {
}
