package be.kdg.backendjava.controllers;

import be.kdg.backendjava.domain.Tag;
import be.kdg.backendjava.services.TagService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    private final TagService tagService;

    public UserController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping("/{projectname}/checkpoints")
    public ModelAndView userCheckpointsOverview(@PathVariable String projectname) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("userMapOverview");

        List<Tag> allTags = tagService.getAllTags();
        mav.addObject("tags", allTags);
        mav.addObject("projectName", projectname);
        return mav;
    }
}
