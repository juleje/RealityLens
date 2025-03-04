package be.kdg.backendjava.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Table(name = "checkpoint")
@Inheritance(strategy = InheritanceType.JOINED)
@Entity
@NoArgsConstructor
public class Checkpoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @Column(length = 4000)
    private String description;
    private String shortDescription;
    private String image;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "projectId", nullable = false)
    @JsonIgnore
    private Project project;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @JsonIgnore
    @OneToMany(mappedBy = "checkpoint", cascade = CascadeType.DETACH)
    private List<Like> likes;

    @JsonIgnore
    @OneToMany(mappedBy = "checkpoint", cascade = CascadeType.DETACH)
    private List<Comment> comments;

    @JsonIgnore
    @ManyToMany(mappedBy = "checkpoints")
    private List<Tag> tags;


    public Checkpoint(String name, String description, String shortDescription, String image, Location location, Project project) {
        this.name = name;
        this.description = description;
        this.shortDescription = shortDescription;
        this.image = image;
        this.location = location;
        this.project = project;
        this.tags = new ArrayList<>();
        this.comments= new ArrayList<>();
        this.likes = new ArrayList<>();
    }

    public void removeTag(Tag tag) {
        tags.remove(tag);
    }

    public void addTag(Tag tag) {
        tags.add(tag);
    }
}
