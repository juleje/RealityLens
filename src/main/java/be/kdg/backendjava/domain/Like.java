package be.kdg.backendjava.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Getter
@Setter
@Table(name = "likes")
@Entity
@NoArgsConstructor
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "checkpoint_id", nullable = false)
    private Checkpoint checkpoint;

    public Like(User user, Checkpoint checkpoint) {
        this.user = user;
        this.checkpoint = checkpoint;
    }
}
