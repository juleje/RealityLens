package be.kdg.backendjava.repositories;

import be.kdg.backendjava.domain.Checkpoint;
import be.kdg.backendjava.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CheckpointRepository extends JpaRepository<Checkpoint, Integer> {
    List<Checkpoint> findAllByProject(Project project);
}
