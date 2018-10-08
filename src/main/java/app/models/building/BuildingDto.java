package app.models.building;

import app.models.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BuildingDto {
	public String picture;
	public String description;
	private long id;
	private String name;
	private Tag tag;
	private List<String> facultiesNames;
	private List<Long> facultiesIds;

	public BuildingDto(BuildingDto buildingDto) {
		this.id = buildingDto.getId();
		this.name = buildingDto.getName();
		this.tag = buildingDto.getTag();
		this.picture = buildingDto.getPicture();
		this.description = buildingDto.getDescription();
		this.facultiesNames = buildingDto.getFacultiesNames();
		this.facultiesIds = buildingDto.getFacultiesIds();
	}
}
