package app.models.department;

import app.models.BaseEntity;
import app.models.faculty.Faculty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Department extends BaseEntity {

	@Column(columnDefinition = "text")
	private String description;

	@ManyToOne(fetch = FetchType.EAGER)
	private Faculty faculty;
}
