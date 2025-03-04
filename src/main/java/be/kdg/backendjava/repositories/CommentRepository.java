package be.kdg.backendjava.repositories;

import be.kdg.backendjava.domain.Checkpoint;
import be.kdg.backendjava.domain.Comment;
import be.kdg.backendjava.domain.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findAllByCheckpoint_Id(int checkpointId);
    void removeAllByCheckpoint(Checkpoint checkpoint);
}
