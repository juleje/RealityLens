package be.kdg.backendjava.repositories;

import be.kdg.backendjava.domain.Checkpoint;
import be.kdg.backendjava.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.expression.spel.support.ReflectivePropertyAccessor;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Integer> {
    List<Tag> findAllByCheckpoints(Checkpoint checkpoint);
    List<Tag> findAllByCheckpointsNotContains(Checkpoint checkpoint);
    Optional<Tag> findByName(String name);
}