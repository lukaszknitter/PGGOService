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
	private String picture;

	@Convert(converter = AdministrativeNumbersConverter.class)
	@Column(columnDefinition = "text")
	private List<Integer> administrativeNumbers;

	@Column(columnDefinition = "text")
	private String description;

	@ManyToMany(fetch = FetchType.EAGER)
	private List<Faculty> faculties;
}
