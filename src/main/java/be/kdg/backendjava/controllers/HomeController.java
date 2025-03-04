package be.kdg.backendjava.controllers;

import be.kdg.backendjava.domain.Checkpoint;
import be.kdg.backendjava.domain.Comment;
import be.kdg.backendjava.domain.User;
import be.kdg.backendjava.dtos.CommentDto;
import be.kdg.backendjava.services.CheckpointsService;
import be.kdg.backendjava.services.CommentService;
import be.kdg.backendjava.services.LikeService;
import be.kdg.backendjava.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.List;

@Controller @RequestMapping("/user")
public class HomeController {

    private CheckpointsService checkpointsService;
    private final LikeService likeService;
    private CommentService commentService;
    private UserService userService;

    public HomeController(CheckpointsService checkpointsService, LikeService likeService, CommentService commentService, UserService userService) {
        this.checkpointsService = checkpointsService;
        this.likeService = likeService;
        this.commentService = commentService;
        this.userService = userService;
    }

    @GetMapping("")
    public ModelAndView welcome() {
        ModelAndView mav = new ModelAndView();
        boolean loggedIn = checkpointsService.amILoggedIn();
        mav.addObject("loggedIn", loggedIn);
        mav.setViewName("welcome");
        return mav;
    }


    @GetMapping("/ar")
    public ModelAndView home() {
        ModelAndView mav = new ModelAndView();
        mav.addObject("hasCheckpoints", false);
        mav.setViewName("home");
        return mav;
    }

    @GetMapping("/ar/{ids}")
    public ModelAndView home(@PathVariable String ids, @RequestParam(required = false) String activeCheckId) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("home");
        mav.addObject("hasCheckpoints", true);
        if (activeCheckId != null) {
            mav.addObject("activePopup", true);
            mav.addObject("activeCheckId", Integer.parseInt(activeCheckId));
        } else {
            mav.addObject("activePopup", false);
        }
        if (activeCheckId!=null){
            int checkpointId = Integer.parseInt(activeCheckId);
            String imageUrl = checkpointsService.getCheckpointById(checkpointId).getImage();
            mav.addObject("imageCheckpoint", imageUrl);
            System.out.println(checkpointId);
            System.out.println(imageUrl);
        }
        mav.addObject("checkpointsIds", ids);
        return mav;
    }

    @GetMapping("/comments/{params}/{id}")
    public ModelAndView comments(@PathVariable String params,@PathVariable String id) {
        int checkpointId = Integer.parseInt(id);
        List<CommentDto> allCommentsForCheckpoint = commentService.getAllCommentsByCheckpointId(checkpointId);
        Checkpoint checkpoint = checkpointsService.getCheckpointById(checkpointId);
        ModelAndView mav = new ModelAndView();
        mav.addObject("comments", allCommentsForCheckpoint);
        mav.addObject("checkpoint", checkpoint);
        mav.addObject("checkpointId", checkpointId);
        mav.addObject("params", params);
        mav.setViewName("comments");
        return mav;
    }

    @PostMapping("/comments/{params}/{id}")
    String sendComment(@RequestParam("messageComment") String messageComment,@PathVariable String id, @PathVariable String params) {
        System.out.println(messageComment);
        int checkpointId = Integer.parseInt(id);
        if(!messageComment.equals("")){
            Comment comment = new Comment();
            comment.setCheckpoint(checkpointsService.getCheckpointById(checkpointId));
            comment.setMessage(messageComment);
            comment.setUser(userService.getLoggedInUser());
            comment.setDateTime(LocalDateTime.now());
        commentService.addComment(comment);
        }
        return "redirect:/user/comments/" +params + "/" +checkpointId;
    }



}
