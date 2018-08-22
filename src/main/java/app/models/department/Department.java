package app.models.department;

import app.models.BaseEntity;
import app.models.faculty.Faculty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Department extends BaseEntity {

    @Column(columnDefinition = "text")
    public String description;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    public Faculty faculty;
}
