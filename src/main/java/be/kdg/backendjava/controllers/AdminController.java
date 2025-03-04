package be.kdg.backendjava.controllers;

import be.kdg.backendjava.configuration.ExcelHelper;
import be.kdg.backendjava.domain.*;
import be.kdg.backendjava.dtos.CheckpointForm;
import be.kdg.backendjava.dtos.ExcelForm;
import be.kdg.backendjava.dtos.ProjectForm;
import be.kdg.backendjava.services.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final CheckpointsService checkpointsService;
    private final LocationService locationService;
    private final ProjectService projectService;
    private final TagService tagService;
    private final ExcelService excelService;
    private QRCodeService qrCodeService;

    public AdminController(CheckpointsService checkpointsService, LocationService locationService, ProjectService projectService, TagService tagService, ExcelService excelService, QRCodeService qrCodeService) {
        this.checkpointsService = checkpointsService;
        this.locationService = locationService;
        this.projectService = projectService;
        this.tagService = tagService;
        this.excelService = excelService;
        this.qrCodeService = qrCodeService;
    }


    @GetMapping("/checkpoints")
    public ModelAndView home() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("checkpoints");
        mav.addObject("allProjects", projectService.getAllProjects());
        return mav;
    }

    @GetMapping("/checkpoint/{id}")
    public ModelAndView checkpointDetail(@PathVariable int id) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("checkpointDetail");
        Checkpoint checkpoint = checkpointsService.getCheckpointById(id);
        List<Tag> otherTags = tagService.getAllTagsWhereNotCheckpoint(checkpoint);
        mav.addObject("otherTags", otherTags);
        mav.addObject("checkpoint", checkpoint);
        return mav;
    }

    @GetMapping("/addCheckpoint")
    public ModelAndView addCheckpoint(@ModelAttribute("checkpointForm") CheckpointForm checkpointForm) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("addCheckpoint");
        mav.addObject("addCheck", true);

        if (checkpointForm.getName() != null) {
            mav.addObject("checkpointForm", checkpointForm);
        } else {
            CheckpointForm newCheckpointForm = new CheckpointForm();
            newCheckpointForm.setScale(1.0);
            mav.addObject("checkpointForm", newCheckpointForm);
        }
        ;

        mav.addObject("editId", 0);
        mav.addObject("allProjects", projectService.getAllProjects());
        return mav;
    }

    @GetMapping("/editCheckpoint/{checkpointId}")
    public ModelAndView editCheckpoint(@PathVariable int checkpointId, @ModelAttribute("checkpointForm") CheckpointForm checkpointForm) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("addCheckpoint");
        mav.addObject("addCheck", false);

        Checkpoint checkpoint = checkpointsService.getCheckpointById(checkpointId);
        if (checkpoint == null) {
            mav.setViewName("checkpoints");
            return mav;
        }

        if (checkpointForm.getName() != null) {
            mav.addObject("checkpointForm", checkpointForm);
        } else {
            double scale = 1.0;
            if (checkpoint instanceof AdvancedCheckpoint) {
                scale = ((AdvancedCheckpoint) checkpoint).getScale();
            }
            CheckpointForm checkpointToEditForm = new CheckpointForm(checkpoint.getName(), checkpoint.getDescription(), checkpoint.getShortDescription(), checkpoint.getImage(),
                    checkpoint.getProject().getId()
                    , checkpoint.getLocation().getLatitude(), checkpoint.getLocation().getLongitude(), checkpoint instanceof AdvancedCheckpoint, null, scale, "/");
            mav.addObject("checkpointForm", checkpointToEditForm);
        }

        mav.addObject("editId", checkpoint.getId());


        mav.addObject("allProjects", projectService.getAllProjects());
        return mav;
    }

    @PostMapping("/addCheckpoint")
    public RedirectView addCheckpointPost(@ModelAttribute @Valid CheckpointForm checkpointForm, BindingResult bindingResult, RedirectAttributes attributes) {
        Location location = getOrCreateLocation(checkpointForm.getLatitude(), checkpointForm.getLongitude());
        if (
                (bindingResult.hasErrors()) ||
                        (checkpointForm.getLatitude() == 0 && checkpointForm.getLongitude() == 0) ||
                        (location == null) ||
                        (checkpointForm.isAdvanced() && (checkpointForm.getZipFile().isEmpty() && Objects.equals(checkpointForm.getDefaultObjectname(), "/")))
        ) {
            attributes.addFlashAttribute("checkpointForm", checkpointForm);
            return new RedirectView("/admin/addCheckpoint");
        }

        Project project = projectService.getProjectById(checkpointForm.getProjectId());
        AdvancedCheckpoint newAdvancedCheckpoint = null;
        Checkpoint newCheckpoint = null;
        if (checkpointForm.isAdvanced()) {
            try {
                AdvancedCheckpoint advancedCheckpoint = null;
                if (Objects.equals(checkpointForm.getDefaultObjectname(), "/")) {
                    String filePath = ZipFileService.unzipFile(checkpointForm.getZipFile());
                    advancedCheckpoint = new AdvancedCheckpoint(checkpointForm.getName(), checkpointForm.getDescription(), checkpointForm.getShortDescription(),
                            checkpointForm.getImage(), location, project, checkpointForm.getZipFile().getName(), filePath, checkpointForm.getScale());
                } else {
                    advancedCheckpoint = new AdvancedCheckpoint(checkpointForm.getName(), checkpointForm.getDescription(), checkpointForm.getShortDescription(),
                            checkpointForm.getImage(), location, project, checkpointForm.getDefaultObjectname(), "/", checkpointForm.getScale());
                }
                newAdvancedCheckpoint = checkpointsService.addAdvancedCheckpoint(advancedCheckpoint);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Checkpoint checkpoint = new Checkpoint(checkpointForm.getName(), checkpointForm.getDescription(), checkpointForm.getShortDescription(), checkpointForm.getImage(), location, project);
            newCheckpoint = checkpointsService.addCheckpoint(checkpoint);
        }

        if (newAdvancedCheckpoint == null && newCheckpoint == null) {
            attributes.addFlashAttribute("checkpointForm", checkpointForm);
            return new RedirectView("/admin/addCheckpoint");
        }

        return new RedirectView("/admin/checkpoints");
    }

    @PostMapping("/editCheckpoint/{checkpointId}")
    public RedirectView editCheckpointPost(@ModelAttribute @Valid CheckpointForm checkpointForm, BindingResult bindingResult, RedirectAttributes attributes, @PathVariable int checkpointId) {
        if ((bindingResult.hasErrors()) || (checkpointForm.isAdvanced() && (checkpointForm.getZipFile().isEmpty() && Objects.equals(checkpointForm.getDefaultObjectname(), "/")))
        ) {
            attributes.addFlashAttribute("checkpointForm", checkpointForm);
            return new RedirectView("/admin/editCheckpoint/" + checkpointId);
        }

        Location location = getOrCreateLocation(checkpointForm.getLatitude(), checkpointForm.getLongitude());
        if (location == null) {
            attributes.addFlashAttribute("checkpointForm", checkpointForm);
            return new RedirectView("/admin/editCheckpoint/" + checkpointId);
        }
        Project project = projectService.getProjectById(checkpointForm.getProjectId());


        AdvancedCheckpoint existingAdvancedCheckpoint = checkpointsService.getAdvancedCheckpointById(checkpointId);
        Checkpoint editedCheckpoint = null;
        //To new advanced
        if (checkpointForm.isAdvanced()) {

            //Previous was not advanced
            if (existingAdvancedCheckpoint == null) {
                AdvancedCheckpoint newAdvancedCheckpoint = null;
                try {
                    if (Objects.equals(checkpointForm.getDefaultObjectname(), "/")) {
                        String filePath = ZipFileService.unzipFile(checkpointForm.getZipFile());
                        newAdvancedCheckpoint = new AdvancedCheckpoint(
                                checkpointForm.getName(),
                                checkpointForm.getDescription(),
                                checkpointForm.getShortDescription(),
                                checkpointForm.getImage(),
                                location, project,
                                checkpointForm.getZipFile().getName(), filePath, checkpointForm.getScale());
                        checkpointsService.deleteCheckpoint(checkpointId);
                        checkpointsService.addAdvancedCheckpoint(newAdvancedCheckpoint);
                    } else {
                        newAdvancedCheckpoint = new AdvancedCheckpoint(
                                checkpointForm.getName(),
                                checkpointForm.getDescription(),
                                checkpointForm.getShortDescription(),
                                checkpointForm.getImage(),
                                location, project,
                                checkpointForm.getDefaultObjectname(), "/", checkpointForm.getScale());
                        checkpointsService.deleteCheckpoint(checkpointId);
                        checkpointsService.addAdvancedCheckpoint(newAdvancedCheckpoint);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    if (Objects.equals(checkpointForm.getDefaultObjectname(), "/")) {
                        String filePath = ZipFileService.unzipFile(checkpointForm.getZipFile());
                        checkpointsService.editAdvancedCheckpoint(new AdvancedCheckpoint(
                                checkpointForm.getName(),
                                checkpointForm.getDescription(),
                                checkpointForm.getShortDescription(),
                                checkpointForm.getImage(),
                                location, project,
                                checkpointForm.getZipFile().getName(), filePath, checkpointForm.getScale()));

                        checkpointsService.deleteCheckpoint(checkpointId);
                    } else {
                        checkpointsService.editAdvancedCheckpoint(new AdvancedCheckpoint(
                                checkpointForm.getName(),
                                checkpointForm.getDescription(),
                                checkpointForm.getShortDescription(),
                                checkpointForm.getImage(),
                                location, project,
                                checkpointForm.getDefaultObjectname(), "/", checkpointForm.getScale()));

                        checkpointsService.deleteCheckpoint(checkpointId);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            if (existingAdvancedCheckpoint == null) {
                Checkpoint checkpoint = checkpointsService.getCheckpointById(checkpointId);
                checkpoint.setName(checkpointForm.getName());
                checkpoint.setDescription(checkpointForm.getDescription());
                checkpoint.setShortDescription(checkpointForm.getShortDescription());
                checkpoint.setImage(checkpointForm.getImage());
                checkpoint.setLocation(location);
                editedCheckpoint = checkpointsService.editCheckpoint(checkpoint);
            } else {
                Checkpoint checkpoint = checkpointsService.getCheckpointById(checkpointId);
                checkpoint.setName(checkpointForm.getName());
                checkpoint.setDescription(checkpointForm.getDescription());
                checkpoint.setShortDescription(checkpointForm.getShortDescription());
                checkpoint.setImage(checkpointForm.getImage());
                checkpoint.setLocation(location);
                checkpointsService.deleteAdvancedCheckpoint(checkpointId);
                editedCheckpoint = checkpointsService.editCheckpoint(checkpoint);
            }
        }

        if (editedCheckpoint == null && existingAdvancedCheckpoint == null) {
            attributes.addFlashAttribute("checkpointForm", checkpointForm);
            return new RedirectView("/admin/editCheckpoint/" + checkpointId);
        }
        return new RedirectView("/admin/checkpoints");
    }

    private Location getOrCreateLocation(double latitude, double longitude) {
        Location location = locationService.getLocationByCoordinates(latitude, longitude);
        if (location == null) {
            Location newLocation = new Location(latitude, longitude);
            location = locationService.addLocation(newLocation);
        }
        return location;
    }

    @GetMapping("/deleteCheckpoint/{checkpointId}")
    public RedirectView deleteCheckpoint(@PathVariable int checkpointId) {
        ModelAndView mav = new ModelAndView();
        checkpointsService.deleteCheckpoint(checkpointId);

        return new RedirectView("/admin/checkpoints");
    }

    @GetMapping("/projects")
    public ModelAndView getAllProjects() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("projects");

        List<Project> projects = projectService.getAllProjects();
        mav.addObject("projects", projects);
        return mav;
    }

    @GetMapping("/addproject")
    public ModelAndView addProject(@ModelAttribute("projectForm") ProjectForm projectForm) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("addProject");


        mav.addObject("addProj", true);

        if (projectForm.getName() != null) {
            mav.addObject("projectForm", projectForm);
        } else {
            mav.addObject("projectForm", new ProjectForm());
        }
        mav.addObject("editId", 0);
        return mav;
    }

    @PostMapping("/addProject")
    public RedirectView addProjectPost(@ModelAttribute @Valid ProjectForm projectForm, BindingResult
            bindingResult, RedirectAttributes attributes) {

        if (bindingResult.hasErrors()) {
            attributes.addFlashAttribute("projectForm", projectForm);
            attributes.addFlashAttribute("bindingResult", bindingResult);
            return new RedirectView("/admin/addproject");
        }

        Project project = new Project(projectForm.getName(), projectForm.getDescription());
        Project existingProject = projectService.getProjectByName(projectForm.getName());
        if (existingProject != null) {
            attributes.addFlashAttribute("projectForm", projectForm);
            return new RedirectView("/admin/addproject");
        }
        Project createdProject = projectService.addProject(project);
        if (createdProject == null) {
            attributes.addFlashAttribute("projectForm", projectForm);
            return new RedirectView("/admin/addproject");
        }

        return new RedirectView("/admin/projects");
    }

    @GetMapping("/editProject/{projectId}")
    public ModelAndView editProject(@PathVariable int projectId,
                                    @ModelAttribute("projectForm") ProjectForm projectForm) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("addProject");

        Project project = projectService.getProjectById(projectId);

        mav.addObject("addProj", false);
        if (projectForm.getName() != null) {
            mav.addObject("projectForm", projectForm);
        } else {
            mav.addObject("projectForm", new ProjectForm(project.getName(), project.getDescription()));
        }
        mav.addObject("editId", projectId);

        return mav;
    }

    @PostMapping("/editproject/{projectId}")
    public RedirectView editProjectPost(@PathVariable int projectId,
                                        @ModelAttribute @Valid ProjectForm projectForm, BindingResult bindingResult, RedirectAttributes attributes) {

        if (bindingResult.hasErrors()) {
            attributes.addFlashAttribute("projectForm", projectForm);
            return new RedirectView("/admin/editProject/" + projectId);
        }

        Project projectToEdit = projectService.getProjectById(projectId);
        if (projectToEdit == null) {
            attributes.addFlashAttribute("projectForm", projectForm);
            return new RedirectView("/admin/editProject/" + projectId);
        }

        projectToEdit.setName(projectForm.getName());
        projectToEdit.setDescription(projectForm.getDescription());
        projectService.editProject(projectToEdit);


        return new RedirectView("/admin/projects");
    }

    @GetMapping("/deleteProject/{projectId}")
    public RedirectView deleteProject(@PathVariable int projectId) {
        ModelAndView mav = new ModelAndView();
        projectService.deleteProject(projectId);

        return new RedirectView("/admin/projects");
    }

    @GetMapping("/upload")
    public ModelAndView uploadFile(@ModelAttribute("errormessage") String errormessage) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("uploadExcel");
        mav.addObject("errormessage", errormessage);
        mav.addObject("excelForm", new ExcelForm());
        mav.addObject("allProjects", projectService.getAllProjects());
        return mav;
    }

    @PostMapping("/upload")
    public RedirectView uploadFilePost(@ModelAttribute ExcelForm excelForm, RedirectAttributes attributes) {
        if (ExcelHelper.hasExcelFormat(excelForm.getFile())) {
            try {
                String errormessage = excelService.save(excelForm.getProjectId(), excelForm.getFile());
                attributes.addFlashAttribute("errormessage", errormessage);
                return new RedirectView("/admin/upload");
            } catch (Exception e) {
                attributes.addFlashAttribute("errormessage", "error");
                return new RedirectView("/admin/upload");
            }
        }
        attributes.addFlashAttribute("errormessage", "error");
        return new RedirectView("/admin/upload");
    }

    @GetMapping("/tags")
    public ModelAndView tags() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("tags");
        mav.addObject("tags", tagService.getAllTags());
        return mav;
    }

    @PostMapping("/tags/add")
    public RedirectView addTag(@RequestParam("tagName") String name) {
        tagService.addTag(name);
        return new RedirectView("/admin/tags");
    }

    @GetMapping("/tags/{tagId}/edit")
    public ModelAndView editTag(@PathVariable int tagId) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("tags");
        Tag tag = tagService.getById(tagId);
        mav.addObject("editTag", tag);
        return mav;
    }

    @PostMapping("/tags/{tagId}/edit")
    public RedirectView editTagPost(@PathVariable int tagId, @RequestParam("tagName") String name) {
        tagService.editTag(tagId, name);
        return new RedirectView("/admin/tags");
    }

    @GetMapping("/tags/{tagId}/delete")
    public RedirectView deleteTag(@PathVariable int tagId) {
        tagService.deleteTag(tagId);

        return new RedirectView("/admin/tags");
    }

    @GetMapping("/showQRCode/{projectName}")
    public ModelAndView showQRCode(@PathVariable String projectName, Model model) {
        model.addAttribute("qrCodeContent", "/admin/generateQRCode?qrContent=" + "https://brogrammersreflect.ninja/user/" + projectName + "/checkpoints");
        ModelAndView mav = new ModelAndView();
        mav.setViewName("projects");
        mav.addObject("projectName", projectName);
        List<Project> projects = projectService.getAllProjects();
        mav.addObject("projects", projects);
        return mav;
    }

    @GetMapping("/generateQRCode")
    public void generateQRCode(String qrContent, HttpServletResponse response) throws IOException {
        response.setContentType("image/png");
        byte[] qrCode = qrCodeService.generateQRCode(qrContent, 350, 350);
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(qrCode);
    }

}
