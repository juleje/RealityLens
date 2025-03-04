package be.kdg.backendjava.services;

import be.kdg.backendjava.domain.Checkpoint;
import be.kdg.backendjava.domain.Tag;
import be.kdg.backendjava.repositories.CheckpointRepository;
import be.kdg.backendjava.repositories.TagRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TagService {
    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }


    public void addTag(String name) {
        Optional<Tag> tagOptional = tagRepository.findByName(name);
        if (tagOptional.isEmpty()) {
            Tag tag = new Tag(name);
            tagRepository.save(tag);
        }
    }

    public void deleteTag(int tagId) {
        Optional<Tag> tagOptional = tagRepository.findById(tagId);
        if (tagOptional.isPresent()) {
            Tag tag = tagOptional.get();
            tag.removeAllCheckpoints();
            tagRepository.delete(tag);
        }
    }

    public void editTag(int tagId, String name) {
        Optional<Tag> tagOptional = tagRepository.findById(tagId);
        if (tagOptional.isPresent()) {
            Tag tag = tagOptional.get();
            tag.setName(name);
            tagRepository.saveAndFlush(tag);
        }
    }

    public Tag getById(int tagId) {
        Optional<Tag> tagOptional = tagRepository.findById(tagId);
        return tagOptional.orElse(null);
    }

    public List<Tag> getAllTagsWhereNotCheckpoint(Checkpoint checkpoint) {
        return tagRepository.findAllByCheckpointsNotContains(checkpoint);
    }
}
