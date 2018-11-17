package app.models.building;

import app.display.building.BuildingDisplay;
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

	@OneToOne
	private BuildingDisplay buildingDisplay;

	@Column(columnDefinition = "longblob")
	private String picture;

	@Convert(converter = AdministrativeNumbersConverter.class)
	@Column(columnDefinition = "text")
	private List<Integer> numbers;

	@Column(columnDefinition = "text")
	private String description;

	@ManyToMany(fetch = FetchType.EAGER)
	private List<Faculty> faculties;
}
