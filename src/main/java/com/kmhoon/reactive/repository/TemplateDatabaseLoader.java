package com.kmhoon.reactive.repository;

import com.kmhoon.reactive.domain.Item;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;

@Component
public class TemplateDatabaseLoader {

//    @Bean
    CommandLineRunner initialize(MongoOperations mongo) {
        return args -> {
            mongo.save(new Item("id1", "name1", "desc1", 1.99));
            mongo.save(new Item("id2", "name2", "desc2", 9.99));
//            mongo.save(new Item("Alf alarm clock", 19.99));
//            mongo.save(new Item("Smurf TV tray", 24.99));
        };
    }
}
