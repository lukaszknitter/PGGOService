package app.models.department;

import app.models.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDto {
	private long id;
	private String name;
	private Tag tag;
	private String description;
	private long facultyId;
}
