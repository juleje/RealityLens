package be.kdg.backendjava.services;

import be.kdg.backendjava.domain.Comment;
import be.kdg.backendjava.dtos.CommentDto;
import be.kdg.backendjava.repositories.CommentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;


    public CommentService(CommentRepository commentRepository, UserService userService) {
        this.commentRepository = commentRepository;
        this.userService = userService;
    }


    public List<CommentDto> getAllCommentsByCheckpointId(int i) {
      List<Comment> comments = commentRepository.findAllByCheckpoint_Id(i);
      List<CommentDto> dtos = new ArrayList<>();
        for (Comment comment : comments) {
            CommentDto dto = new CommentDto();
            dto.setMessage(comment.getMessage());
            dto.setUsername(userService.getUserName(comment.getUser().getId()));
            dto.setDate(comment.getDateTime().toLocalDate());
            dtos.add(dto);
        }
        return dtos;
    }

    public void addComment(Comment comment) {
        commentRepository.save(comment);
    }
}
