package be.kdg.backendjava.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;

@Getter
@Setter
@Table(name = "advancedcheckpoint")
@Entity
@NoArgsConstructor
public class AdvancedCheckpoint extends Checkpoint{
    private String objectName;
    private String gltfPath;
    private double scale;

    public AdvancedCheckpoint(String name, String description, String shortDescription,String image, Location location, Project project, String objName, String gltfPath, double scale) {
        super(name,description, shortDescription,image,location,project);
        scaleChecker(objName,scale);
        this.objectName = objName;
        pathPicker(objName,gltfPath);

    }

    private void pathPicker(String objectName, String gltfPath){
        switch (objectName){
            default:
                this.gltfPath= gltfPath;
                break;
            case "bench":
                this.gltfPath = "/gltf/modern_bench/scene.gltf";
                break;
            case "trashcan":
                this.gltfPath = "/gltf/green_city_trash_can/scene.gltf";
                break;
            case "tree":
                this.gltfPath ="/gltf/tree/scene.gltf";
                break;
            case "fontain":
                this.gltfPath ="/gltf/fontain/scene.gltf";
                break;
        }
    }

    private void scaleChecker(String name, double scale){
        switch (name){
            default:
                this.scale = scale;
                break;
            case "fontain":
                this.scale = 0.1;
                break;
            case "bench":
                this.scale = 4;
                break;
            case "trashcan":
                this.scale = 10;
                break;
            case "tree":
                this.scale =70;
                break;
        }
    }
}
