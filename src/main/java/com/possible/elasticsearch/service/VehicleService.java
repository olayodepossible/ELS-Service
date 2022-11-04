package com.possible.elasticsearch.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import com.possible.elasticsearch.document.Vehicle;
import com.possible.elasticsearch.helper.Indices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class VehicleService {

    private static final Logger LOG = LoggerFactory.getLogger(VehicleService.class);

    private final ElasticsearchClient esClient;

    public VehicleService(ElasticsearchClient esClient) {
        this.esClient = esClient;
    }

    public Boolean index(final Vehicle vehicle) {

        try {
            IndexRequest<Vehicle> req = IndexRequest.of(b -> b
                    .id(vehicle.getId())
                    .index("vehicle")
                    .document(vehicle)
            );

            IndexResponse response = esClient.index(req);
            return response != null;
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
            return Boolean.FALSE;
        }
    }

    public Vehicle getById(final String vehicleId) {
        Vehicle vehicle = null;
        try {
            GetResponse<Vehicle> response = esClient.get(g -> g
                            .index(Indices.VEHICLE_INDEX)
                            .id(vehicleId),
                    Vehicle.class
            );

            if (response.found()) {
                vehicle = response.source();
                assert vehicle != null;
                LOG.info("Vehicle Number: {}", vehicle.getNumber());

            } else {
                LOG.info("Vehicle with number {} not found", vehicleId);
            }
            return vehicle;
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return null;
        }


    }
}
