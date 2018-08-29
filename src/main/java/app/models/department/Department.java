package app.models.department;

import app.models.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Department extends BaseEntity {

    @Column(columnDefinition = "text")
    public String description;

}
