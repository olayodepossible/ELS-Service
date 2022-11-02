package com.possible.elasticsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@SpringBootApplication
@EnableElasticsearchRepositories
public class SpringElasticSearchServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringElasticSearchServiceApplication.class, args);
	}

}
