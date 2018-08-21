package app.models.faculty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import app.models.BaseEntity;
import app.models.building.Building;
import app.models.department.Department;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Faculty extends BaseEntity {

    @OneToMany(mappedBy = "facultyModel")
    private List<Department> departments;

    @ManyToMany
    @JoinColumn(name = "buildings_faculties")
    private List<Building> buildings;
}
