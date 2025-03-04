package be.kdg.backendjava.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Getter
@Setter
@Entity
@Table(name = "projects")
@NoArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true)
    private String name;
    private String description;

    @OneToMany(mappedBy = "project", cascade = CascadeType.DETACH)
    private List<Checkpoint> checkpoints;


    public Project(String name, String description) {
        setName(name);
        this.description = description;
        this.checkpoints = new ArrayList<>();
    }

    public void setName(String name) {
        this.name = name.replace(" ", "_").toLowerCase();
    }

    public void addCheckpoint(Checkpoint checkpoint) {
        this.checkpoints.add(checkpoint);
    }

    public void removeCheckpoint(Checkpoint checkpoint) {
        this.checkpoints.remove(checkpoint);
    }
}
