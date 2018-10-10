package app.models.building;

import app.models.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BuildingSearchDto {
	public String description;
	private long id;
	private String name;
	private Tag tag;
}
