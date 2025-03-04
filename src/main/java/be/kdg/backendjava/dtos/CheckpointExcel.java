package be.kdg.backendjava.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CheckpointExcel {
    private String name;
    private String description;
    private String shortDescription;
    private String image;
    private double latitude;
    private double longitude;
}
