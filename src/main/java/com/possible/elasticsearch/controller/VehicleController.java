package com.possible.elasticsearch.controller;

import com.possible.elasticsearch.document.Vehicle;
import com.possible.elasticsearch.service.VehicleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/vehicle")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping
    public ResponseEntity<Vehicle> saveDocument(@RequestBody Vehicle vehicle) {
        return new ResponseEntity(vehicleService.index(vehicle), HttpStatus.CREATED);

    }

    @GetMapping("/{id}")
    public Vehicle findDocumentById(@PathVariable String id) {
        return vehicleService.getById(id);

    }
}
