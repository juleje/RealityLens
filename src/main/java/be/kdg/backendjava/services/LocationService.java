package be.kdg.backendjava.services;

import be.kdg.backendjava.domain.Location;
import be.kdg.backendjava.repositories.LocationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class LocationService {
    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public Location getLocationByCoordinates(double latitude, double longtitude) {
        Optional<Location> location = locationRepository.findByLatitudeAndLongitude(latitude, longtitude);
        return location.orElse(null);
    }

    public Location addLocation(Location location) {
        return locationRepository.save(location);
    }
}
