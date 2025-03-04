package be.kdg.backendjava.services;

import be.kdg.backendjava.domain.Checkpoint;
import be.kdg.backendjava.domain.Like;
import be.kdg.backendjava.domain.User;
import be.kdg.backendjava.repositories.CheckpointRepository;
import be.kdg.backendjava.repositories.LikeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserService userService;
    private final CheckpointsService checkpointsService;
    private final CheckpointRepository checkpointRepository;

    public LikeService(LikeRepository likeRepository, UserService userService, CheckpointsService checkpointsService, CheckpointRepository checkpointRepository) {
        this.likeRepository = likeRepository;
        this.userService = userService;
        this.checkpointsService = checkpointsService;
        this.checkpointRepository = checkpointRepository;
    }

    public void likeThisCheckpoint(int checkpointId){
        System.out.println("Like toevoegen");
        User u = userService.getLoggedInUser();
        Checkpoint cp = checkpointsService.getCheckpointById(checkpointId);
        Like like = new Like(u, cp);
        likeRepository.save(like);
        cp.getLikes().add(like);
        checkpointRepository.save(cp);
    }

    public void unlikeThisCheckpoint(int checkpointId){
        User u = userService.getLoggedInUser();
        likeRepository.deleteByCheckpoint_IdAndAndUser(checkpointId, u);
        System.out.println("Like verwijderen");
    }
}
