package app.models.building;

import app.models.faculty.Faculty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import app.models.BaseEntity;

import javax.persistence.*;
import java.io.InputStream;
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

    @ManyToMany
    @JoinColumn(name = "buildings_faculties")
    private List<Faculty> faculties;
}
