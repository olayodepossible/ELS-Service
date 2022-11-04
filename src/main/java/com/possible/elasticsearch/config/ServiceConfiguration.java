package com.possible.elasticsearch.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;

@Configuration
public class ServiceConfiguration extends AbstractElasticsearchConfiguration {
    @Value("${elasticsearch.url}")
    private String elasticSearchUrl;
    @Value("${elasticsearch.port}")
    private int port;
    @Value("${elasticsearch.hostname}")
    private String hostname;

    @Bean
    @Override
    public RestHighLevelClient elasticsearchClient() {
        final ClientConfiguration config = ClientConfiguration.builder().connectedTo(elasticSearchUrl).build();

        return RestClients.create(config).rest();
    }

    @Bean
    public RestClient getRestClient() {
        return RestClient.builder(new HttpHost(hostname, port)).build();
    }

    @Bean
    public ElasticsearchClient getElasticSearchClient() {
        ElasticsearchTransport transport = new RestClientTransport(getRestClient(), new JacksonJsonpMapper());
        return new ElasticsearchClient(transport);
    }

}
