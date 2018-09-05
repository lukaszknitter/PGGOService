package app.models.building;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BuildingCreationDto {
	public String picture;
	public String description;
	private long id;
	private String name;
	private String tag;
	private List<Long> facultiesIds;

	public BuildingCreationDto(BuildingCreationDto buildingDto) {
		this.id = buildingDto.getId();
		this.name = buildingDto.getName();
		this.tag = buildingDto.getTag();
		this.picture = buildingDto.getPicture();
		this.description = buildingDto.getDescription();
		this.facultiesIds = buildingDto.getFacultiesIds();
	}
}
