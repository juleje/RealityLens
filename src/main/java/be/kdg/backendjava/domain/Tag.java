package be.kdg.backendjava.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Table(name = "tag")
@Entity
@NoArgsConstructor
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @ManyToMany
    @JoinTable(
            name = "checkpoint_tags",
            joinColumns = @JoinColumn(name = "tag_id"),
            inverseJoinColumns = @JoinColumn(name = "checkpoint_id"))
    private List<Checkpoint> checkpoints;

    public Tag(String name) {
        this.name = name;
        this.checkpoints = new ArrayList<>();
    }

    public void addCheckpoint(Checkpoint checkpoint) {
        this.checkpoints.add(checkpoint);
    }

    public void removeCheckpoint(Checkpoint checkpoint) {
        checkpoints.remove(checkpoint);
    }

    public void removeAllCheckpoints() {
        checkpoints.clear();
    }
}
