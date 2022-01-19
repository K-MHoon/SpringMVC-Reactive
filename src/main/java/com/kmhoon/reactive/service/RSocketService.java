package com.kmhoon.reactive.service;

import com.kmhoon.reactive.domain.Item;
import com.kmhoon.reactive.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Service;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;

@Service
public class RSocketService {
    private final ItemRepository repository;
    private final EmitterProcessor<Item> itemProcessor;
    private final FluxSink<Item> itemSink;

    public RSocketService(ItemRepository repository) {
        this.repository = repository;
        this.itemProcessor = EmitterProcessor.create();
        this.itemSink = this.itemProcessor.sink();
    }

    @MessageMapping("newItems.request-response")
    public Mono<Item> processNewItemsViaRSocketRequestResponse(Item item) {
        return this.repository.save(item)
                .doOnNext(savedItem -> this.itemSink.next(savedItem));
    }
}
