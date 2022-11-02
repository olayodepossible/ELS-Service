package com.possible.elasticsearch.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.ExistsRequest;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import co.elastic.clients.util.ObjectBuilder;
import com.possible.elasticsearch.helper.Indices;
import com.possible.elasticsearch.helper.LoaderUtils;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.function.Function;

@Service
public class IndexService {
    private static  final Logger LOG = LoggerFactory.getLogger(IndexService.class)
    private final List<String> INDICES_TO_CREATE = List.of(Indices.VEHICLE_INDEX);
    private final RestHighLevelClient client;
    private final ElasticsearchClient esClient;

    @Value("${elasticsearch.port}")
    private int port;

    @Value("${elasticsearch.hostname}")
    private String hostname;

    public IndexService(RestHighLevelClient client, ElasticsearchClient esClient) {
        this.client = client;
        this.esClient = esClient;
    }

    @Bean
    public ElasticsearchClient getElasticSearchClient() {
        // Create the low-level client
        RestClient restClient = RestClient.builder(new HttpHost(hostname, port)).build();
        // Create the transport with a Jackson mapper
        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        // And create the API client
        return new ElasticsearchClient(transport);
    }



    @PostConstruct
    public void createIndices(){
        final String settings = LoaderUtils.loadAsString("static/es-settings.json");
        for (final String indexName: INDICES_TO_CREATE) {
            try{
                boolean indexExit = esClient.indices().exists(ExistsRequest.of(e -> e.index(indexName))).value();
                if (indexExit) {
                    continue;
                }
                final String mappings = LoaderUtils.loadAsString("static/mappings/" + indexName + ".json");
                if (settings == null || mappings == null) {
                    LOG.error("Failed to create index with name '{}'", indexName);
                    continue;
                }
                final CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);
                createIndexRequest.settings(settings, XContentType.JSON);
                createIndexRequest.mapping(mappings, XContentType.JSON);
                esClient.indices().create();
            }catch (Exception e){
                LOG.error(e.getMessage());
            }

        }

    }
}
