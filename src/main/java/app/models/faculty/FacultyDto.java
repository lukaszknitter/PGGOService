package app.models.faculty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FacultyDto {
    private long id;
    private String name;
    private String tag;
    private List<Long> departments;

    public FacultyDto(FacultyDto facultyDto) {
        this.id = facultyDto.getId();
        this.name = facultyDto.getName();
        this.tag = facultyDto.getTag();
        this.departments = facultyDto.getDepartments();
    }
}
