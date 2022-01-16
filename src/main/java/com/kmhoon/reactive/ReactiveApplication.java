package com.kmhoon.reactive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;

@SpringBootApplication
@EnableAutoConfiguration(exclude = EmbeddedMongoAutoConfiguration.class)
public class ReactiveApplication {

	public static void main(String[] args) {
//		BlockHound.builder()
//				.allowBlockingCallsInside(TemplateEngine.class.getCanonicalName(), "process")
//				.install();
		SpringApplication.run(ReactiveApplication.class, args);
	}

}
