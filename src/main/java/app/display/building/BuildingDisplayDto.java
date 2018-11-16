package app.display.building;

import app.display.coordinate.Coordinate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BuildingDisplayDto {
	public long buildingId;
	private long id;
	private String name;
	private String tag;
	private List<Coordinate> coordinates;
	private Coordinate center;
}
