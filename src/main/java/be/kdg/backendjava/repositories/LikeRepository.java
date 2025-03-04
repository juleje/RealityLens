package be.kdg.backendjava.repositories;

import be.kdg.backendjava.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like, Integer> {
    List<Like> findAllByCheckpoint_IdAndUser_Id(int checkpointId, long userId);
    void deleteByCheckpoint_IdAndAndUser(int checkpointId, User user);
    void removeAllByCheckpoint(Checkpoint checkpoint);

}
