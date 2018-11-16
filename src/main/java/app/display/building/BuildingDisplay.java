package app.display.building;

import app.display.coordinate.Coordinate;
import app.models.BaseEntity;
import app.models.building.Building;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class BuildingDisplay extends BaseEntity {

	@OneToOne
	public Building building;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "building_display_id")
	public List<Coordinate> coordinates;

	@OneToOne(cascade = CascadeType.ALL)
	public Coordinate center;
}
