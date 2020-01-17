package com.space.service;

import com.space.exception.BadRequestException;
import com.space.model.Ship;
import com.space.model.ShipType;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class ShipUtils {
    static List<Ship> filterShips(List<Ship> shipsToFiltered,
                                  String name,
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

        shipsToFiltered.removeIf(ship ->
                (Objects.nonNull(name) && !ship.getName().toLowerCase().contains(name.toLowerCase()))
                        || (Objects.nonNull(planet) && !ship.getPlanet().toLowerCase().contains(planet.toLowerCase()))
                        || (Objects.nonNull(shipType) && ship.getShipType() != ShipType.valueOf(shipType))
                        || (Objects.nonNull(after) && (!ship.getProdDate().after(new Date(after)) && !Objects.equals(ship.getProdDate(), new Date(after))))
                        || (Objects.nonNull(before) && (!ship.getProdDate().before(new Date(before-3600000))) && !Objects.equals(ship.getProdDate(), new Date(before)))
                        || (Objects.nonNull(isUsed) && !ship.isUsed().equals(isUsed))
                        || (Objects.nonNull(minSpeed) && ship.getSpeed() < minSpeed)
                        || (Objects.nonNull(maxSpeed) && ship.getSpeed() > maxSpeed)
                        || (Objects.nonNull(minCrewSize) && ship.getCrewSize() < minCrewSize)
                        || (Objects.nonNull(maxCrewSize) && ship.getCrewSize() > maxCrewSize)
                        || (Objects.nonNull(minRating) && ship.getRating() < minRating)
                        || (Objects.nonNull(maxRating) && ship.getRating() > maxRating)
        );

        return shipsToFiltered;
    }

    static void checkParameters(Ship ship) {
        if (Objects.isNull(ship.getName())
                || Objects.isNull(ship.getPlanet())
                || Objects.isNull(ship.getShipType())
                || Objects.isNull(ship.getProdDate())
                || Objects.isNull(ship.getSpeed())
                || Objects.isNull(ship.getCrewSize())
                || ship.getName().length() > 50
                || ship.getPlanet().length() > 50
                || ship.getName().isEmpty()
                || ship.getPlanet().isEmpty()
                || ship.getSpeed() < 0.01
                || ship.getSpeed() > 0.99
                || ship.getCrewSize() < 1
                || ship.getCrewSize() > 9999
                || ship.getProdDate().getTime() < 0
                || !isProdDate(ship.getProdDate())) throw new BadRequestException();
    }

    static boolean isProdDate(Date date) {
        Calendar minYear = Calendar.getInstance();
        minYear.set(Calendar.YEAR, 2800);
        Date minDate = minYear.getTime();

        Calendar maxYear = Calendar.getInstance();
        maxYear.set(Calendar.YEAR, 3019);
        Date maxDate = maxYear.getTime();

        return date.getTime() >= minDate.getTime()
                && date.getTime() <= maxDate.getTime();
    }

    static Double computeShipRating(Ship ship) {
        double ratio;

        if (Objects.nonNull(ship.isUsed()) ? ship.isUsed() : false)
            ratio = 0.5;
        else ratio = 1.0;

        //Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(ship.getProdDate().getTime()));
        //date.setTime(ship.getProdDate().getTime());

        double exactRating = 80 * ship.getSpeed() * ratio
                / (3019 - calendar.get(Calendar.YEAR) + 1);

        return Double.valueOf(String.format("%.2f", exactRating).replace(",", "."));
    }
}
