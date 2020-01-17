package com.space.controller;

import com.space.model.Ship;
import com.space.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

    @RestController
    public class ShipController {
        private final ShipService shipService;

        @Autowired
        public ShipController(ShipService shipService) {
            this.shipService = shipService;
        }

        @GetMapping(path = "/rest/ships")
        public List<Ship> getShipsList(@RequestParam(required = false) String name,
                                       @RequestParam(required = false) String planet,
                                       @RequestParam(required = false) String shipType,
                                       @RequestParam(required = false) Long after,
                                       @RequestParam(required = false) Long before,
                                       @RequestParam(required = false) Boolean isUsed,
                                       @RequestParam(required = false) Double minSpeed,
                                       @RequestParam(required = false) Double maxSpeed,
                                       @RequestParam(required = false) Integer minCrewSize,
                                       @RequestParam(required = false) Integer maxCrewSize,
                                       @RequestParam(required = false) Double minRating,
                                       @RequestParam(required = false) Double maxRating,
                                       @RequestParam(required = false) ShipOrder order,
                                       @RequestParam(required = false) Integer pageNumber,
                                       @RequestParam(required = false) Integer pageSize) {
            return shipService.getShipsList(
                    name, planet, shipType, after, before, isUsed, minSpeed, maxSpeed,
                    minCrewSize, maxCrewSize, minRating, maxRating, order, pageNumber, pageSize);
        }

        @GetMapping(path = "/rest/ships/count")
        public Integer getShipsCount(@RequestParam(required = false) String name,
                                     @RequestParam(required = false) String planet,
                                     @RequestParam(required = false) String shipType,
                                     @RequestParam(required = false) Long after,
                                     @RequestParam(required = false) Long before,
                                     @RequestParam(required = false) Boolean isUsed,
                                     @RequestParam(required = false) Double minSpeed,
                                     @RequestParam(required = false) Double maxSpeed,
                                     @RequestParam(required = false) Integer minCrewSize,
                                     @RequestParam(required = false) Integer maxCrewSize,
                                     @RequestParam(required = false) Double minRating,
                                     @RequestParam(required = false) Double maxRating) {

            return shipService.getShipsCount(
                    name, planet, shipType, after, before, isUsed, minSpeed,
                    maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating);
        }

        @PostMapping(path = "/rest/ships")
        public Ship createShip(@RequestBody Ship requestBody) {
            return shipService.createShip(requestBody);
        }

        @GetMapping(path = "/rest/ships/{id}")
        public Ship getShip(@PathVariable Long id) {
            return shipService.getShip(id);
        }

        @PostMapping(path = "/rest/ships/{id}")
        public Ship updateShip(@PathVariable Long id, @RequestBody Ship requestBody) {
            return shipService.updateShip(id, requestBody);
        }

        @DeleteMapping(path = "/rest/ships/{id}")
        public void deleteShip(@PathVariable Long id) {
            shipService.deleteShip(id);
        }
    }
