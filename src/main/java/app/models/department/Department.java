package app.models.department;

import app.models.BaseEntity;
import app.models.faculty.Faculty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Department extends BaseEntity {

    @ManyToOne
    public Faculty facultyModel;

    @Column(columnDefinition = "text")
    public String description;
}
