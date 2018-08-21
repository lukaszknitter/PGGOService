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

    @OneToMany(mappedBy="faculty", fetch=FetchType.EAGER, cascade=CascadeType.ALL)
    public List<Department> departments;

    @ManyToMany(cascade = CascadeType.ALL)
    public List<Building> buildings;
}
