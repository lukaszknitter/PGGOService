package app.models.building;

import app.models.BaseEntity;
import app.models.faculty.Faculty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Building extends BaseEntity {

    @Column(columnDefinition = "longblob")
    public String picture; // Inputstream

    @Column(columnDefinition = "text")
    public String description;

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<Faculty> faculties;
}
