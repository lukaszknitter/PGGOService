package app.models.faculty;

import app.models.BaseEntity;
import app.models.building.Building;
import app.models.department.Department;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Faculty extends BaseEntity {

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "faculty_id")
    public List<Department> departments;

    @ManyToMany(cascade = CascadeType.ALL)
    public List<Building> buildings;
}
