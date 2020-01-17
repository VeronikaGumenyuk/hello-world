package com.space.service;

import com.space.controller.ShipOrder;
import com.space.exception.BadRequestException;
import com.space.exception.NotFoundException;
import com.space.model.Ship;
import com.space.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
public class ShipServiceImpl implements ShipService {
    private final ShipRepository shipRepository;

    @Autowired
    public ShipServiceImpl(ShipRepository shipRepository) {
        this.shipRepository = shipRepository;
    }

    @Override
    @Transactional
    public List<Ship> getShipsList(String name,
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
                                   Integer pageSize) {

        List<Ship> shipsToFiltered = order != null ?
                shipRepository.findAll(Sort.by(order.getFieldName(), "id"))
                : shipRepository.findAll();

        List<Ship> filteredShips = ShipUtils.filterShips(
                shipsToFiltered, name, planet, shipType, after, before, isUsed, minSpeed,
                maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating);

        pageNumber = pageNumber == null ? 0 : pageNumber;
        pageSize = pageSize == null ? 3 : pageSize;

        List<Ship> sublistByPageNumber = filteredShips.subList(pageNumber * pageSize, filteredShips.size());

        return sublistByPageNumber.subList(0, Math.min(pageSize, sublistByPageNumber.size()));
    }

    @Override
    @Transactional
    public Integer getShipsCount(String name,
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
                                 Double maxRating) {

        return ShipUtils.filterShips(
                shipRepository.findAll(), name, planet, shipType, after, before, isUsed,
                minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating).size();
    }

    @Override
    @Transactional
    public Ship createShip(Ship body) {

        if (Objects.isNull(body)) return null;

        ShipUtils.checkParameters(body);

        Ship creatingShip = new Ship();

        creatingShip.setName(body.getName());
        creatingShip.setPlanet(body.getPlanet());
        creatingShip.setShipType(body.getShipType());
        creatingShip.setProdDate(body.getProdDate());
        creatingShip.setUsed(Objects.nonNull(body.isUsed()) ? body.isUsed() : false);
        creatingShip.setSpeed(body.getSpeed());
        creatingShip.setCrewSize(body.getCrewSize());
        creatingShip.setRating(ShipUtils.computeShipRating(body));

        shipRepository.save(creatingShip);

        return creatingShip;
    }

    @Override
    @Transactional
    public Ship getShip(Long id) {
        if (id <= 0) throw new BadRequestException();
        return shipRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    @Override
    @Transactional
    public Ship updateShip(Long id, Ship body) {
        Ship updatingShip = getShip(id);

        if (Objects.nonNull(body.getName()))
            if (!body.getName().isEmpty()
                    && body.getName().length() <= 50)
                updatingShip.setName(body.getName());
            else throw new BadRequestException();

        if (Objects.nonNull(body.getPlanet()))
            if (body.getPlanet().length() <= 50)
                updatingShip.setPlanet(body.getPlanet());
            else throw new BadRequestException();

        if (Objects.nonNull(body.getShipType()))
            updatingShip.setShipType(body.getShipType());

        if (Objects.nonNull(body.getProdDate())) {
            if (body.getProdDate().getTime() > 0
                    && ShipUtils.isProdDate(body.getProdDate()))
                updatingShip.setProdDate(body.getProdDate());
            else throw new BadRequestException();
        }

        if (Objects.nonNull(body.isUsed()))
            updatingShip.setUsed(body.isUsed());

        if (Objects.nonNull(body.getSpeed()))
            if (body.getSpeed() >= 0.01 || body.getSpeed() <= 0.99)
                updatingShip.setSpeed(body.getSpeed());
            else throw new BadRequestException();

        if (Objects.nonNull(body.getCrewSize()))
            if (body.getCrewSize() >= 1 && body.getCrewSize() <= 9999)
                updatingShip.setCrewSize(body.getCrewSize());
            else throw new BadRequestException();

        Double rating = ShipUtils.computeShipRating(updatingShip);

        updatingShip.setRating(rating);
        shipRepository.save(updatingShip);

        return updatingShip;
    }

    @Override
    @Transactional
    public void deleteShip(Long id) {
        shipRepository.delete(getShip(id));
    }
}
