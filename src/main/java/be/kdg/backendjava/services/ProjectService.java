package be.kdg.backendjava.services;

import be.kdg.backendjava.domain.Checkpoint;
import be.kdg.backendjava.domain.Project;
import be.kdg.backendjava.repositories.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final CheckpointsService checkpointsService;

    public ProjectService(ProjectRepository projectRepository, CheckpointsService checkpointsService) {
        this.projectRepository = projectRepository;
        this.checkpointsService = checkpointsService;
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }
    public Project getProjectById(int id) {
        return projectRepository.findById(id).orElse(null);
    }

    public Project addProject(Project project) {
        return projectRepository.save(project);
    }

    public Project editProject(Project projectToEdit) {
        return projectRepository.saveAndFlush(projectToEdit);
    }

    public void deleteProject(int projectId) {
        Project project = projectRepository.getById(projectId);
        for (Checkpoint c:project.getCheckpoints()) {
            checkpointsService.deleteCheckpoint(c.getId());
        }
        projectRepository.delete(project);
    }

    public Project getProjectByName(String name) {
        return projectRepository.findByName(name).orElse(null);
    }

    public Project getProjectByCheckpointId(int checkpointId) {
        Checkpoint checkpoint = checkpointsService.getCheckpointById(checkpointId);
        if (checkpoint != null) {
            return checkpoint.getProject();
        }
        return null;
    }
}
