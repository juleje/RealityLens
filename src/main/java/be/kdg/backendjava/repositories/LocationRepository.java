package be.kdg.backendjava.repositories;

import be.kdg.backendjava.domain.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Integer> {
    Optional<Location> findByLatitudeAndLongitude(double latitude, double longtitude);
}
