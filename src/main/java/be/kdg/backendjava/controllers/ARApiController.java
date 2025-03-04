package be.kdg.backendjava.controllers;

import be.kdg.backendjava.services.LikeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ar")
public class ARApiController {
    private final LikeService likeService;

    public ARApiController(LikeService likeService) {
        this.likeService = likeService;
    }

    @GetMapping("/like/{checkpointId}")
    public ResponseEntity<Boolean> like(@PathVariable int checkpointId) {
        likeService.likeThisCheckpoint(checkpointId);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @GetMapping("/unlike/{checkpointId}")
    public ResponseEntity<Boolean> unlike(@PathVariable int checkpointId) {
        likeService.unlikeThisCheckpoint(checkpointId);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}
