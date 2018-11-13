package app.display.poi;

import app.display.coordinate.Coordinate;
import app.models.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class POI extends BaseEntity {

	@OneToOne(cascade = CascadeType.ALL)
	public Coordinate coordinate;

	private String description;

	private Type type;
}
