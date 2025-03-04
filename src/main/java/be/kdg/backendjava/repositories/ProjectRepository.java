package be.kdg.backendjava.repositories;

import be.kdg.backendjava.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Integer> {
    Optional<Project> findByName(String name);
}