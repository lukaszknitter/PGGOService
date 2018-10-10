package app.models.faculty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;

@RestController
@RequestMapping("/faculty")
public class FacultyController {

	private final FacultyService service;

	@Autowired
	public FacultyController(FacultyService service) {
		this.service = service;
	}

	@GetMapping("/{id}")
	public FacultyDto getFaculty(@PathVariable long id) {
		return this.service.getFacultyDto(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public FacultyDto createFaculty(@RequestBody @Valid FacultyCreationDto dto) {
		return service.createFaculty(dto);
	}

	@GetMapping
	public ArrayList<FacultySearchDto> getFaculties(@RequestParam(value = "name", required = false) String name) {
		return service.getFaculties(name);
	}

	@PutMapping("/{id}")
	public FacultyDto updateFaculty(@PathVariable long id, @RequestBody @Valid FacultyCreationDto body) {
		return service.updateFaculty(id, body);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void deleteFaculty(@PathVariable long id) {
		service.deleteFaculty(id);
	}
}
