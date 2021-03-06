package app.models.faculty;

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
public class FacultyCreationDto {
	private long id;
	private String name;
	private Tag tag;
	private List<Long> departmentsIds;

	public FacultyCreationDto(FacultyCreationDto facultyDto) {
		this.id = facultyDto.getId();
		this.name = facultyDto.getName();
		this.tag = facultyDto.getTag();
		this.departmentsIds = facultyDto.getDepartmentsIds();
	}
}
