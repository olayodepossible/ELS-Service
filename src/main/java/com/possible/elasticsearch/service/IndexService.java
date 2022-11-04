package com.possible.elasticsearch.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.ExistsRequest;
import com.possible.elasticsearch.helper.Indices;
import com.possible.elasticsearch.helper.LoaderUtils;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.util.List;

@Service
public class IndexService {
    private static final Logger LOG = LoggerFactory.getLogger(IndexService.class);
    private final List<String> indicesToCreate = List.of(Indices.VEHICLE_INDEX);
    private final RestHighLevelClient client;
    private final ElasticsearchClient esClient;

    public IndexService(RestHighLevelClient client, ElasticsearchClient esClient) {
        this.client = client;
        this.esClient = esClient;
    }

    @PostConstruct
    public void createIndices() {
        final String settings = LoaderUtils.loadAsString("static\\es-settings.json");
        for (final String indexName : indicesToCreate) {
            try {
                boolean indexExit = esClient.indices().exists(ExistsRequest.of(e -> e.index(indexName))).value();

                final String mappings = LoaderUtils.loadAsString("static\\mapping\\" + indexName + ".json");
                final InputStream inputStream = LoaderUtils.getFileFromResourceAsStream("static\\mapping\\" + indexName + ".json");
                if (indexExit || settings == null || mappings == null) {
                    LOG.error("Failed to create index with name '{}'", indexName);
                    continue;
                }

                CreateIndexResponse response = esClient.indices().create(CreateIndexRequest.of(e -> e.index(indexName)
                        .withJson(inputStream)
                ));
                LOG.info("Acknowledged {}", Boolean.TRUE.equals(response.acknowledged()));
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }

        }

    }
}
