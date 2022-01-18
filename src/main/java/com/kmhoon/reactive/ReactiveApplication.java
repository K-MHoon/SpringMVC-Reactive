package com.kmhoon.reactive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.hateoas.config.EnableHypermediaSupport;

import static org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType.HAL;
import static org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType.HAL_FORMS;

@SpringBootApplication
@EnableAutoConfiguration(exclude = EmbeddedMongoAutoConfiguration.class)
@EnableHypermediaSupport(type = { HAL, HAL_FORMS })
public class ReactiveApplication {

	public static void main(String[] args) {
//		BlockHound.builder()
//				.allowBlockingCallsInside(TemplateEngine.class.getCanonicalName(), "process")
//				.install();
		SpringApplication.run(ReactiveApplication.class, args);
	}

}
