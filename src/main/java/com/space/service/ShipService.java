package com.space.service;

import com.space.controller.ShipOrder;
import com.space.model.Ship;

import java.util.List;

public interface ShipService {
    List<Ship> getShipsList(String name,
                            String planet,
                            String shipType,
                            Long after,
                            Long before,
                            Boolean isUsed,
                            Double minSpeed,
                            Double maxSpeed,
                            Integer minCrewSize,
                            Integer maxCrewSize,
                            Double minRating,
                            Double maxRating,
                            ShipOrder order,
                            Integer pageNumber,
                            Integer pageSize);

    Integer getShipsCount(String name,
                          String planet,
                          String shipType,
                          Long after,
                          Long before,
                          Boolean isUsed,
                          Double minSpeed,
                          Double maxSpeed,
                          Integer minCrewSize,
                          Integer maxCrewSize,
                          Double minRating,
                          Double maxRating);

    Ship createShip(Ship body);

    Ship getShip(Long id);

    Ship updateShip(Long id, Ship body);

    void deleteShip(Long id);
}
