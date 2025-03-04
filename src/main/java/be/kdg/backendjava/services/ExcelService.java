package be.kdg.backendjava.services;

import be.kdg.backendjava.configuration.ExcelHelper;
import be.kdg.backendjava.domain.Checkpoint;
import be.kdg.backendjava.domain.CheckpointExcelReturn;
import be.kdg.backendjava.domain.Location;
import be.kdg.backendjava.domain.Project;
import be.kdg.backendjava.dtos.CheckpointExcel;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ExcelService {
    private final ProjectService projectService;
    private final CheckpointsService checkpointsService;
    private final LocationService locationService;

    public ExcelService(ProjectService projectService, CheckpointsService checkpointsService, LocationService locationService) {
        this.projectService = projectService;
        this.checkpointsService = checkpointsService;
        this.locationService = locationService;
    }

    public String save(int projectId, MultipartFile multipartFile) {
        try {
            CheckpointExcelReturn checkpointExcelsReturn = ExcelHelper.excelToCheckpoints(multipartFile.getInputStream());

            if (checkpointExcelsReturn.getCheckpoints() == null) {
                return checkpointExcelsReturn.getErrorMessage();
            }

            List<CheckpointExcel> checkpointExcels = checkpointExcelsReturn.getCheckpoints();
            Project project = projectService.getProjectById(projectId);
            if (project == null) {
                return "error";
            }
            for (CheckpointExcel c:checkpointExcels) {
                Location location = locationService.getLocationByCoordinates(c.getLatitude(), c.getLongitude());
                if (location == null) {
                    location = new Location(c.getLatitude(), c.getLongitude());
                    locationService.addLocation(location);
                }
                Checkpoint checkpoint = new Checkpoint(c.getName(), c.getDescription(), c.getShortDescription(), c.getImage(), location, project);
                checkpointsService.addCheckpoint(checkpoint);

                project.addCheckpoint(checkpoint);
            }
            projectService.editProject(project);
            return "success";
        } catch (IOException e) {
            throw new RuntimeException("fail to store excel data: " + e.getMessage());
        }
    }
}
