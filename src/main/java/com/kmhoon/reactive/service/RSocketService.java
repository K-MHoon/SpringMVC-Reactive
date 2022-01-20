package com.kmhoon.reactive.service;

import com.kmhoon.reactive.domain.Item;
import com.kmhoon.reactive.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Service;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
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

    @MessageMapping("newItems.request-stream")
    public Flux<Item> findItemsViaRSocketRequestStream() {
        return this.repository.findAll()
                .doOnNext(this.itemSink::next);
    }

    @MessageMapping("newItems.fire-and-forget")
    public Mono<Void> processNewItemsViaRSocketFilterAndForget(Item item) {
        return this.repository.save(item)
                .doOnNext(savedItem -> this.itemSink.next(savedItem))
                // Deprecated인 FluxProcessor, EmitterProcessor의 대체 구현
                // .doOnNext(savedItem -> this.itemsSink.tryEmitNext(savedItem))
                .then();
    }

    @MessageMapping("newItems.monitor")
    public Flux<Item> monitorNewItems() {
        return this.itemProcessor;
        // Deprecated인 FluxProcessor, EmitterProcessor의 대체 구현
        // return this.itemsSink.asFlux();
    }
}
