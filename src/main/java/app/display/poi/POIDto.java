package app.display.poi;

import app.display.coordinate.Coordinate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public
class POIDto {
	private long id;
	private String name;
	private String tag;
	private String description;
	private Type type;
	private Coordinate coordinate;
}
