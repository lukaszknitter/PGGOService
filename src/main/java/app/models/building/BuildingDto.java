package app.models.building;

import app.models.faculty.Faculty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BuildingDto {
    private long id;

    private String name;

    private String tag;

    public String picture;

    public String description;

    private List<Faculty> faculties;
}
