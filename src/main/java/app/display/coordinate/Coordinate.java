package app.display.coordinate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Coordinate {

	@Id
	@GeneratedValue
	public long id;

	@Column(precision = 10, scale = 2)
	private Double latitude;

	@Column(precision = 10, scale = 2)
	private Double longitude;
}
