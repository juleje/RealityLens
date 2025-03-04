package be.kdg.backendjava.controllers;

import be.kdg.backendjava.domain.Checkpoint;
import be.kdg.backendjava.domain.Project;
import be.kdg.backendjava.domain.Tag;
import be.kdg.backendjava.dtos.LikesCommentsDto;
import be.kdg.backendjava.services.CheckpointsService;
import be.kdg.backendjava.services.ProjectService;
import be.kdg.backendjava.services.TagService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/checkpoints")
public class CheckpointApiController {
    private final CheckpointsService checkpointsService;
    private final TagService tagService;
    private final ProjectService projectService;

    public CheckpointApiController(CheckpointsService checkpointsService, TagService tagService, ProjectService projectService) {
        this.checkpointsService = checkpointsService;
        this.tagService = tagService;
        this.projectService = projectService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Checkpoint>> getAllCheckpoints() {
        List<Checkpoint> checkpoints = checkpointsService.getAllCheckpoints();
        if (checkpoints != null) {
            return new ResponseEntity<>(checkpoints, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("{projectId}/all")
    public ResponseEntity<List<Checkpoint>> getAllCheckpointsByProjectId(@PathVariable int projectId) {
        List<Checkpoint> checkpoints = checkpointsService.getAllCheckpointsByProjectId(projectId);
        if (checkpoints != null) {
            return new ResponseEntity<>(checkpoints, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/byName/{projectname}/all")
    public ResponseEntity<List<Checkpoint>> getAllCheckpointsByProjectName(@PathVariable String projectname) {
        List<Checkpoint> checkpoints = checkpointsService.getAllCheckpointsFromProject(projectname);
        if (checkpoints != null) {
            return new ResponseEntity<>(checkpoints, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/byName/{projectname}/filter/{tagIds}")
    public ResponseEntity<List<Checkpoint>> getFilteredCheckpointsByProjectName(@PathVariable String projectname, @PathVariable String tagIds) {
        List<Checkpoint> checkpoints = checkpointsService.getFilteredCheckpointsFromProject(projectname, tagIds);
        if (checkpoints != null) {
            return new ResponseEntity<>(checkpoints, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Checkpoint> getCheckpointById(@PathVariable int id) {
        Checkpoint checkpoint = checkpointsService.getCheckpointById(id);
        if (checkpoint != null) {
            return new ResponseEntity<>(checkpoint, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/likesandcomments/{id}")
    public ResponseEntity<LikesCommentsDto> getLikesAndCommentsById(@PathVariable int id) {
        int numberOfLikes = checkpointsService.getNumberOfLikesOfCheckpoint(id);
        int numberOfComments = checkpointsService.getNumberOfCommentsOfCheckpoint(id);
        boolean haveLiked = checkpointsService.haveILikedCheckpoint(id);
        boolean loggedIn = checkpointsService.amILoggedIn();
        LikesCommentsDto likesCommentsDto = new LikesCommentsDto();
        likesCommentsDto.setNumberOfComments(numberOfComments);
        likesCommentsDto.setAmIloggedIn(loggedIn);
        likesCommentsDto.setHaveLiked(haveLiked);
        likesCommentsDto.setNumberOfLikes(numberOfLikes);
        if (likesCommentsDto != null) {
            return new ResponseEntity<>(likesCommentsDto, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/getAll/{ids}")
    public ResponseEntity<List<Checkpoint>> getCheckpointById(@PathVariable String ids) {
        List<Checkpoint> checkpoints = checkpointsService.getAllCheckpointByIds(ids);
        if (checkpoints != null) {
            return new ResponseEntity<>(checkpoints, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{checkpointId}/tags/{tagId}")
    public ResponseEntity<Boolean> addRemoveTag(@PathVariable int checkpointId, @PathVariable int tagId) {
        Checkpoint checkpoint = checkpointsService.getCheckpointById(checkpointId);
        Tag tag = tagService.getById(tagId);
        if (checkpoint != null && tag != null) {
            checkpointsService.addRemoveTag(checkpoint, tag);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("{checkpointId}/project/getName")
    public ResponseEntity<String> getProjectByCheckpointId(@PathVariable int checkpointId) {
        Project project = projectService.getProjectByCheckpointId(checkpointId);

        if (project != null) {
            return new ResponseEntity<>(project.getName(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
