package be.kdg.backendjava.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CheckpointForm {
    private String name;
    private String description;
    @Size(max = 225, message = "De korte beschrijving mag maximaal 225 karakters lang zijn")
    private String shortDescription;
    private String image;
    private int projectId;
    private double latitude;
    private double longitude;

    private boolean advanced;
    private MultipartFile zipFile;
    private double scale;
    private String defaultObjectname;
}
