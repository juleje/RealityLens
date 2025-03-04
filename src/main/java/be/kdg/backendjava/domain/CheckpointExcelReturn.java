package be.kdg.backendjava.domain;

import be.kdg.backendjava.dtos.CheckpointExcel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CheckpointExcelReturn {
    private List<CheckpointExcel> checkpoints;
    private String errorMessage;
}
