package app.models.faculty;

import app.models.department.Department;
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

    private List<Department> departments;
}
