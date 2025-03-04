package be.kdg.backendjava.services;

import be.kdg.backendjava.domain.*;
import be.kdg.backendjava.repositories.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CheckpointsService {
    private final CheckpointRepository checkpointRepository;
    private final AdvancedCheckpointRepository advancedCheckpointRepository;
    private final TagRepository tagRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;

    public CheckpointsService(CheckpointRepository checkpointRepository, AdvancedCheckpointRepository advancedCheckpointRepository, TagRepository tagRepository, ProjectRepository projectRepository, UserRepository userRepository, LikeRepository likeRepository, CommentRepository commentRepository) {
        this.checkpointRepository = checkpointRepository;
        this.advancedCheckpointRepository = advancedCheckpointRepository;
        this.tagRepository = tagRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.likeRepository = likeRepository;
        this.commentRepository = commentRepository;
    }

    public List<Checkpoint> getAllCheckpoints() {
        return checkpointRepository.findAll();
    }

    public Checkpoint addCheckpoint(Checkpoint checkpoint) {
        return checkpointRepository.save(checkpoint);
    }

    public Checkpoint getCheckpointById(int checkpointId) {
        return checkpointRepository.findById(checkpointId).orElse(null);
    }

    public Checkpoint editCheckpoint(Checkpoint newCheckpoint) {
        return checkpointRepository.saveAndFlush(newCheckpoint);
    }

    public void deleteCheckpoint(int checkpointId) {
        Optional<Checkpoint> checkpointOptional = checkpointRepository.findById(checkpointId);
        if (checkpointOptional.isEmpty()) {
            return;
        }
        Checkpoint checkpoint = checkpointOptional.get();
        List<Tag> tags = tagRepository.findAllByCheckpoints(checkpoint);
        for (Tag tag : tags) {
            tag.removeCheckpoint(checkpoint);
        }
        commentRepository.removeAllByCheckpoint(checkpoint);
        likeRepository.removeAllByCheckpoint(checkpoint);
        checkpointRepository.delete(checkpoint);
    }

    public List<Checkpoint> getAllCheckpointsByProjectId(int projectId) {
        Optional<Project> projectOptional = projectRepository.findById(projectId);
        if (projectOptional.isPresent()) {
            Project project = projectOptional.get();
            return checkpointRepository.findAllByProject(project);
        }
        return null;
    }

    public List<Checkpoint> getAllCheckpointsFromProject(String projectname) {
        Optional<Project> projectOptional = projectRepository.findByName(projectname.toLowerCase());
        if (projectOptional.isPresent()) {
            Project project = projectOptional.get();
            return checkpointRepository.findAllByProject(project);
        }
        return null;
    }

    public List<Checkpoint> getAllCheckpointByIds(String ids) {
        List<String> stringIdList = new ArrayList<String>(Arrays.asList(ids.split(",")));
        List<Checkpoint> checkpoints = new ArrayList<>();
        for (String id : stringIdList) {
            Integer intId = Integer.parseInt(id);
            Optional<Checkpoint> checkpointOptional = checkpointRepository.findById(intId);
            checkpointOptional.ifPresent(checkpoints::add);
        }
        return checkpoints;
    }


    public int getNumberOfLikesOfCheckpoint(int checkpointId) {
        Optional<Checkpoint> cpOptional = checkpointRepository.findById(checkpointId);
        if (cpOptional.isPresent()) {
            Checkpoint cp = cpOptional.get();
            return cp.getLikes().size();
        }
        return 0;
    }

    public int getNumberOfCommentsOfCheckpoint(int checkpointId) {
        Optional<Checkpoint> cpOptional = checkpointRepository.findById(checkpointId);
        if (cpOptional.isPresent()) {
            Checkpoint cp = cpOptional.get();
            return cp.getComments().size();
        }
        return 0;
    }

    public boolean amILoggedIn() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getPrincipal().toString();
        return !username.equals("anonymousUser");
    }

    public boolean haveILikedCheckpoint(int checkpointId) {
        if (amILoggedIn()) {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username = ((UserDetails) principal).getUsername(); //email address
            Optional<User> userOpt = userRepository.findByEmail(username);
            Long loggedInUser = 0L;
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                System.out.println(user.getId());
                loggedInUser = user.getId();
            }
            List<Like> likesForThisCheckpoint = likeRepository.findAllByCheckpoint_IdAndUser_Id(checkpointId, loggedInUser);
            return likesForThisCheckpoint.size() > 0;
        }
        return false;
    }

    public void addRemoveTag(Checkpoint checkpoint, Tag tag) {
        if (checkpoint.getTags().contains(tag)) {
            //checkpoint has tag
            checkpoint.removeTag(tag);
            tag.removeCheckpoint(checkpoint);
            checkpointRepository.saveAndFlush(checkpoint);
            tagRepository.saveAndFlush(tag);
        } else {
            //checkpoint hasn't tag
            checkpoint.addTag(tag);
            tag.addCheckpoint(checkpoint);
            checkpointRepository.saveAndFlush(checkpoint);
            tagRepository.saveAndFlush(tag);
        }
    }

    public List<Checkpoint> getFilteredCheckpointsFromProject(String projectname, String tagIds) {
        List<Checkpoint> checkpoints = getAllCheckpointsFromProject(projectname);
        List<Integer> idList = new ArrayList<String>(Arrays.asList(tagIds.split(",")))
                .stream()
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        List<Checkpoint> filteredCheckpoints = new ArrayList<>();

        for (Checkpoint c:checkpoints) {
            for (Tag t:c.getTags()) {
                if (idList.contains(t.getId())) {
                    filteredCheckpoints.add(c);
                    break;
                }
            }
        }

        return filteredCheckpoints;
    }

    public AdvancedCheckpoint addAdvancedCheckpoint(AdvancedCheckpoint advancedCheckpoint) {
        return advancedCheckpointRepository.save(advancedCheckpoint);
    }

    public AdvancedCheckpoint getAdvancedCheckpointById(int id) {
        return advancedCheckpointRepository.getById(id);
    }

    public void editAdvancedCheckpoint(AdvancedCheckpoint advancedCheckpoint) {
        advancedCheckpointRepository.saveAndFlush(advancedCheckpoint);
    }

    public void deleteAdvancedCheckpoint(int checkpointId) {
        AdvancedCheckpoint checkpoint = getAdvancedCheckpointById(checkpointId);
        advancedCheckpointRepository.delete(checkpoint);
    }
}