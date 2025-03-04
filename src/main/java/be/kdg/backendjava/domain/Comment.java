package be.kdg.backendjava.domain;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Table(name = "comments")
@Entity
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "checkpoint_id", nullable = false)
    private Checkpoint checkpoint;
    private String message;
    private LocalDateTime dateTime;


    public Comment(User user, Checkpoint checkpoint, String message, LocalDateTime dateTime) {
        this.user = user;
        this.checkpoint = checkpoint;
        this.message = message;
        this.dateTime = dateTime;
    }

    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Checkpoint getCheckpoint() {
        return checkpoint;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }
}
